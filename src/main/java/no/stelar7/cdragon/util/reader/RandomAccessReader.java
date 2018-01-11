package no.stelar7.cdragon.util.reader;


import no.stelar7.cdragon.util.reader.types.*;
import sun.nio.ch.DirectBuffer;

import java.io.*;
import java.nio.*;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;

public class RandomAccessReader implements AutoCloseable
{
    private ByteBuffer buffer;
    private Path       path;
    
    public RandomAccessReader(Path path, ByteOrder order)
    {
        try
        {
            this.path = path;
            RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r");
            
            this.buffer = raf.getChannel().map(MapMode.READ_ONLY, 0, raf.getChannel().size());
            this.buffer.order(order);
            raf.close();
        } catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Invalid file?");
        }
    }
    
    public RandomAccessReader(byte[] dataBytes, ByteOrder order)
    {
        this.buffer = ByteBuffer.wrap(dataBytes);
        this.buffer.order(order);
    }
    
    @Override
    public void close()
    {
        /*
         This is really hacky, but its a workaround to http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4715154
          */
        
        if (buffer != null && ((DirectBuffer) buffer).cleaner() != null)
        {
            ((DirectBuffer) buffer).cleaner().clean();
        }
    }
    
    public int pos()
    {
        return buffer.position();
    }
    
    
    public void seek(int pos)
    {
        buffer.position(pos);
    }
    
    public String readString(int length)
    {
        return new String(readBytes(length), StandardCharsets.UTF_8).trim();
    }
    
    
    /**
     * Reads untill 0x00 is read.
     */
    public String readString()
    {
        byte[] temp  = new byte[65536];
        byte   b;
        int    index = 0;
        while ((b = readByte()) != 0)
        {
            temp[index++] = b;
        }
        
        return new String(temp, StandardCharsets.UTF_8).trim();
    }
    
    /**
     * Reads untill EOF
     */
    public String readAsString()
    {
        byte[] temp  = new byte[buffer.remaining()];
        int    index = 0;
        while (buffer.hasRemaining())
        {
            temp[index++] = buffer.get();
        }
        
        return new String(temp, StandardCharsets.UTF_8).trim();
    }
    
    public long readLong()
    {
        return buffer.getLong();
    }
    
    public int readInt()
    {
        return buffer.getInt();
    }
    
    public short readShort()
    {
        return buffer.getShort();
    }
    
    public byte readByte()
    {
        return buffer.get();
    }
    
    public byte[] readBytes(int length)
    {
        byte[] tempData = new byte[length];
        buffer.get(tempData, 0, length);
        return Arrays.copyOf(tempData, length);
    }
    
    public Path getPath()
    {
        return path;
    }
    
    public float readFloat()
    {
        return buffer.getFloat();
    }
    
    
    public boolean readBoolean()
    {
        return buffer.get() > 0;
    }
    
    /**
     * Reads untill 0x00 is read.
     */
    public String readFromOffset(int offset)
    {
        int pos = buffer.position();
        buffer.position(0);
        byte[] tempData = new byte[buffer.remaining()];
        buffer.get(tempData, 0, buffer.remaining());
        buffer.position(pos);
        
        byte[] temp  = new byte[65536];
        byte   b;
        int    index = offset;
        while ((b = tempData[index++]) != 0)
        {
            temp[index - offset] = b;
        }
        
        return new String(temp, StandardCharsets.UTF_8).trim();
    }
    
    public void printBuffer()
    {
        int pos = buffer.position();
        while (buffer.hasRemaining())
        {
            System.out.print(buffer.get() + ", ");
        }
        System.out.println();
        buffer.position(pos);
    }
    
    public Vector3 readVec3F()
    {
        Vector3<Float> vector = new Vector3<>();
        vector.setX(buffer.getFloat());
        vector.setY(buffer.getFloat());
        vector.setZ(buffer.getFloat());
        return vector;
    }
    
    public Vector3 readVec3S()
    {
        Vector3<Short> vector = new Vector3<>();
        vector.setX(buffer.getShort());
        vector.setY(buffer.getShort());
        vector.setZ(buffer.getShort());
        return vector;
    }
    
    public Vector4<Float> readQuaternion()
    {
        Vector4<Float> vector = new Vector4();
        vector.setX(buffer.getFloat());
        vector.setY(buffer.getFloat());
        vector.setZ(buffer.getFloat());
        vector.setW(buffer.getFloat());
        return vector;
    }
    
    public Vector2<Integer> readVec2I()
    {
        Vector2<Integer> vector = new Vector2<>();
        vector.setX(buffer.getInt());
        vector.setY(buffer.getInt());
        return vector;
    }
    
    public Vector2<Float> readVec2F()
    {
        Vector2<Float> vector = new Vector2<>();
        vector.setX(buffer.getFloat());
        vector.setY(buffer.getFloat());
        return vector;
    }
    
    public Vector4<Byte> readVec4B()
    {
        Vector4<Byte> vector = new Vector4();
        vector.setX(buffer.get());
        vector.setY(buffer.get());
        vector.setZ(buffer.get());
        vector.setW(buffer.get());
        return vector;
    }
    
    public Vector4<Float> readVec4F()
    {
        Vector4<Float> vector = new Vector4<>();
        vector.setX(buffer.getFloat());
        vector.setY(buffer.getFloat());
        vector.setZ(buffer.getFloat());
        vector.setW(buffer.getFloat());
        return vector;
    }
    
    public Matrix4x4<Float> readMatrix4x4()
    {
        Matrix4x4<Float> vector = new Matrix4x4<>();
        
        vector.setM00(buffer.getFloat());
        vector.setM01(buffer.getFloat());
        vector.setM02(buffer.getFloat());
        vector.setM03(buffer.getFloat());
        
        vector.setM10(buffer.getFloat());
        vector.setM11(buffer.getFloat());
        vector.setM12(buffer.getFloat());
        vector.setM13(buffer.getFloat());
        
        vector.setM20(buffer.getFloat());
        vector.setM21(buffer.getFloat());
        vector.setM22(buffer.getFloat());
        vector.setM23(buffer.getFloat());
        
        vector.setM30(buffer.getFloat());
        vector.setM31(buffer.getFloat());
        vector.setM32(buffer.getFloat());
        vector.setM33(buffer.getFloat());
        
        return vector;
    }
}
