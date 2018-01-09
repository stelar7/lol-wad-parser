package no.stelar7.cdragon.types.anm.data.versioned;

import lombok.Data;
import no.stelar7.cdragon.types.anm.data.ANMEntry;
import no.stelar7.cdragon.util.reader.types.Vector3;

import java.util.*;

@Data
public class ANMDataVersion1
{
    private int    dataSize;
    private String subMagic;
    private int    subVersion;
    private int    boneCount;
    private int    entryCount;
    private int    unknown1;
    
    private float animationLength;
    private float FPS;
    
    private int unknown2;
    private int unknown3;
    private int unknown4;
    private int unknown5;
    private int unknown6;
    private int unknown7;
    
    private Vector3<Float> minTranslation;
    private Vector3<Float> maxTranslation;
    private Vector3<Float> minScale;
    private Vector3<Float> maxScale;
    
    private int entryOffset;
    private int indexOffset;
    private int hashOffset;
    
    private List<ANMEntry> entries    = new ArrayList<>();
    private List<Short>    indecies   = new ArrayList<>();
    private List<Integer>  boneHashes = new ArrayList<>();
    
    
}