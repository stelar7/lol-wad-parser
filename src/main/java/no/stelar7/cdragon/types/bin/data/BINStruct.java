package no.stelar7.cdragon.types.bin.data;

import lombok.Data;

import java.util.*;

@Data
public class BINStruct
{
    private int   hash;
    private int   entry;
    private short count;
    private List<Object> data = new ArrayList<>();
}
