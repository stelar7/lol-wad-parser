package no.stelar7.cdragon.types.rman.data;

import no.stelar7.cdragon.util.handlers.HashHandler;

import java.util.*;

public class RMANFileBodyFile
{
    private int          offset;
    private int          offsetTableOffset;
    private int          customNameOffset;
    private int          filetypeFlag;
    private int          nameOffset;
    private String       name;
    private int          structSize;
    private int          symlinkOffset;
    private String       symlink;
    private long         fileId;
    private long         directoryId;
    private int          fileSize;
    private int          permissions;
    private int          languageId;
    private int          unknown2;
    private int          unknown3;
    private boolean      isSingleChunk;
    private List<String> chunkIds;
    
    
    public String getFullFilepath(RMANFile manifest)
    {
        StringBuilder                   output = new StringBuilder(getName());
        Optional<RMANFileBodyDirectory> odir   = manifest.getBody().getDirectories().stream().filter(d -> d.getDirectoryId() == getDirectoryId()).findAny();
        if (odir.isPresent())
        {
            RMANFileBodyDirectory dir = odir.get();
            while (dir.getDirectoryId() != 0)
            {
                output.insert(0, dir.getName() + "/");
                
                RMANFileBodyDirectory finalDir = dir;
                dir = manifest.getBody().getDirectories().stream().filter(d -> d.getDirectoryId() == finalDir.getParentId()).findAny().get();
            }
            return output.toString();
        }
        
        System.out.println("Invalid directory id found!");
        return "";
        
    }
    
    public int getOffset()
    {
        return offset;
    }
    
    public void setOffset(int offset)
    {
        this.offset = offset;
    }
    
    public int getOffsetTableOffset()
    {
        return offsetTableOffset;
    }
    
    public void setOffsetTableOffset(int offsetTableOffset)
    {
        this.offsetTableOffset = offsetTableOffset;
    }
    
    public int getNameOffset()
    {
        return nameOffset;
    }
    
    public void setNameOffset(int nameOffset)
    {
        this.nameOffset = nameOffset;
    }
    
    public int getCustomNameOffset()
    {
        return customNameOffset;
    }
    
    public void setCustomNameOffset(int customNameOffset)
    {
        this.customNameOffset = customNameOffset;
    }
    
    public int getFiletypeFlag()
    {
        return filetypeFlag;
    }
    
    public void setFiletypeFlag(int filetypeFlag)
    {
        this.filetypeFlag = filetypeFlag;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public int getStructSize()
    {
        return structSize;
    }
    
    public void setStructSize(int structSize)
    {
        this.structSize = structSize;
    }
    
    public int getSymlinkOffset()
    {
        return symlinkOffset;
    }
    
    public void setSymlinkOffset(int symlinkOffset)
    {
        this.symlinkOffset = symlinkOffset;
    }
    
    public String getSymlink()
    {
        return symlink;
    }
    
    public void setSymlink(String symlink)
    {
        this.symlink = symlink;
    }
    
    public long getFileId()
    {
        return fileId;
    }
    
    public void setFileId(long fileId)
    {
        this.fileId = fileId;
    }
    
    public long getDirectoryId()
    {
        return directoryId;
    }
    
    public void setDirectoryId(long directoryId)
    {
        this.directoryId = directoryId;
    }
    
    public int getFileSize()
    {
        return fileSize;
    }
    
    public void setFileSize(int fileSize)
    {
        this.fileSize = fileSize;
    }
    
    public int getPermissions()
    {
        return permissions;
    }
    
    public void setPermissions(int permissions)
    {
        this.permissions = permissions;
    }
    
    public int getLanguageId()
    {
        return languageId;
    }
    
    public void setLanguageId(int languageId)
    {
        this.languageId = languageId;
    }
    
    public int getUnknown2()
    {
        return unknown2;
    }
    
    public void setUnknown2(int unknown2)
    {
        this.unknown2 = unknown2;
    }
    
    public int getUnknown3()
    {
        return unknown3;
    }
    
    public void setUnknown3(int unknown3)
    {
        this.unknown3 = unknown3;
    }
    
    public boolean isSingleChunk()
    {
        return isSingleChunk;
    }
    
    public void setSingleChunk(int singleChunk)
    {
        isSingleChunk = singleChunk > 0;
    }
    
    public List<String> getChunkIds()
    {
        return chunkIds;
    }
    
    public void setChunkIds(List<Long> chunkIds)
    {
        List<String> stringChunkIds = new ArrayList<>();
        for (Long chunkId : chunkIds)
        {
            stringChunkIds.add(HashHandler.toHex(chunkId, 16));
        }
        this.chunkIds = stringChunkIds;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        RMANFileBodyFile that = (RMANFileBodyFile) o;
        return offset == that.offset &&
               offsetTableOffset == that.offsetTableOffset &&
               customNameOffset == that.customNameOffset &&
               filetypeFlag == that.filetypeFlag &&
               nameOffset == that.nameOffset &&
               structSize == that.structSize &&
               symlinkOffset == that.symlinkOffset &&
               fileId == that.fileId &&
               directoryId == that.directoryId &&
               fileSize == that.fileSize &&
               permissions == that.permissions &&
               languageId == that.languageId &&
               unknown2 == that.unknown2 &&
               unknown3 == that.unknown3 &&
               isSingleChunk == that.isSingleChunk &&
               Objects.equals(name, that.name) &&
               Objects.equals(symlink, that.symlink) &&
               Objects.equals(chunkIds, that.chunkIds);
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    /*
    @Override
    public int hashCode()
    {
        return Objects.hash(offset, offsetTableOffset, customNameOffset, filetypeFlag, nameOffset, name, structSize, symlinkOffset, symlink, fileId, directoryId, fileSize, permissions, languageId, unknown2, unknown3, isSingleChunk, chunkIds);
    }
    
    @Override
    public String toString()
    {
        return "RMANFileBodyFile{" +
               "offset=" + offset +
               ", offsetTableOffset=" + offsetTableOffset +
               ", unknown1=" + unknown1 +
               ", nameOffset=" + nameOffset +
               ", name='" + name + '\'' +
               ", structSize=" + structSize +
               ", symlinkOffset=" + symlinkOffset +
               ", symlink='" + symlink + '\'' +
               ", fileId=" + fileId +
               ", directoryId=" + directoryId +
               ", fileSize=" + fileSize +
               ", permissions=" + permissions +
               ", languageId=" + languageId +
               ", unknown2=" + unknown2 +
               ", unknown3=" + unknown3 +
               ", chunkIds=" + chunkIds +
               '}';
    }
    */
}
