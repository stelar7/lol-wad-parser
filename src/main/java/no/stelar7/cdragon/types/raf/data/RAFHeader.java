package no.stelar7.cdragon.types.raf.data;

import lombok.*;

@Data
public class RAFHeader
{
    private int magic;
    private int version;
    private int managerIndex;
    private int filesOffset;
    private int pathsOffset;
}