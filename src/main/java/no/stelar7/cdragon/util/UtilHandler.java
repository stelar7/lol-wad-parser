package no.stelar7.cdragon.util;

import com.google.gson.reflect.TypeToken;
import net.jpountz.xxhash.*;
import no.stelar7.api.l4j8.basic.utils.Utils;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public final class UtilHandler
{
    
    private UtilHandler()
    {
        // Hide public constructor
    }
    
    private static       Map<String, Map<String, String>> hashNames     = new HashMap<>();
    private static       XXHashFactory                    xxHashFactory = XXHashFactory.fastestInstance();
    private static final char[]                           hexArray      = "0123456789ABCDEF".toCharArray();
    public static final  Path                             HASH_STORE    = Paths.get("src\\main\\java\\no\\stelar7\\cdragon\\types\\wad\\hashes");
    
    
    public static Map<String, String> getKnownFileHashes(String pluginName)
    {
        if (hashNames.get(pluginName) != null)
        {
            return hashNames.get(pluginName);
        }
        
        try
        {
            StringBuilder sb    = new StringBuilder();
            List<String>  lines = Files.readAllLines(HASH_STORE.resolve(pluginName + ".json"));
            lines.forEach(sb::append);
            
            Map<String, String> pluginData = Utils.getGson().fromJson(sb.toString(), new TypeToken<Map<String, String>>() {}.getType());
            hashNames.put(pluginName, pluginData);
            
            System.out.println("Loaded known hashes for " + pluginName);
        } catch (IOException e)
        {
            hashNames.put(pluginName, Collections.emptyMap());
            System.err.println("File not found: " + e.getMessage());
        }
        
        return hashNames.get(pluginName);
    }
    
    public static String pathToFilename(Path path)
    {
        return path.getFileName().toString().substring(0, path.getFileName().toString().lastIndexOf('.'));
    }
    
    public static BitSet longToBitSet(long value)
    {
        BitSet bits  = new BitSet();
        int    index = 0;
        while (value != 0L)
        {
            if (value % 2L != 0)
            {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }
    
    public static long bitsetToLong(BitSet bits)
    {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i)
        {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return value;
    }
    
    public static int getLongFromIP(String ipAddress)
    {
        long     result           = 0;
        String[] ipAddressInArray = ipAddress.split("\\.");
        
        for (int i = 3; i >= 0; i--)
        {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            
            //left shifting 24,16,8,0 and bitwise OR
            //1. 192 << 24
            //1. 168 << 16
            //1. 1   << 8
            //1. 2   << 0
            result |= ip << (i * 8);
        }
        
        return (int) result;
    }
    
    public static String getIPFromLong(long ip)
    {
        return String.format("%d.%d.%d.%d", (ip >> 24) & 0xFF, (ip >> 16) & 0xFF, (ip >> 8) & 0xFF, ip & 0xFF);
    }
    
    
    public static String getHash(String text)
    {
        try
        {
            byte[]               data = text.getBytes(StandardCharsets.UTF_8);
            ByteArrayInputStream in   = new ByteArrayInputStream(data);
            
            StreamingXXHash64 hash64 = xxHashFactory.newStreamingHash64(0);
            byte[]            buf    = new byte[8];
            int               read;
            while ((read = in.read(buf)) != -1)
            {
                hash64.update(buf, 0, read);
            }
            return String.format("%016X", hash64.getValue()).toLowerCase(Locale.ENGLISH);
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String[] getMaxVersion(String url, int min, int max)
    {
        String[] urlEnds = {"/default-assets.wad.compressed", "/assets.wad.compressed"};
        for (int i = max; i >= min; i--)
        {
            for (String endPart : urlEnds)
            {
                String versionAsIP = getIPFromLong(i);
                String finalUrl    = String.format(url, versionAsIP) + endPart;
                System.out.println("Looking for " + finalUrl);
                
                if (checkIfURLExists(finalUrl))
                {
                    return new String[]{finalUrl, versionAsIP};
                }
            }
        }
        return null;
    }
    
    private static boolean checkIfURLExists(String finalUrl)
    {
        try
        {
            HttpURLConnection con = (HttpURLConnection) new URL(finalUrl).openConnection();
            if (con.getResponseCode() == 200)
            {
                System.out.println("Found version: " + finalUrl);
                return true;
            }
            con.disconnect();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    public static void downloadEfficient(Path output, String url)
    {
        try
        {
            if (Files.exists(output))
            {
                System.err.println("This file already exists: " + output.toString());
                return;
            }
            
            Files.createDirectories(output.getParent());
            try (ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());
                 FileOutputStream fos = new FileOutputStream(output.toFile()))
            {
                Files.createDirectories(output.getParent());
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static String readAsString(Path path)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            List<String> lines = Files.readAllLines(path);
            lines.forEach(sb::append);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    
    public static String toHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
