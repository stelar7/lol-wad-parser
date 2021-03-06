package types.util;

import no.stelar7.cdragon.util.handlers.UtilHandler;
import no.stelar7.cdragon.util.hashguessing.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@TestMethodOrder(OrderAnnotation.class)
public class TestCDTBHashGuessing
{
    private final Path dataPath = UtilHandler.CDRAGON_FOLDER.resolve("pbe");
    
    @Test
    public void doTests()
    {
        doBINTest();
        doGameTest();
        doLCUTest();
    }
    
    @Test
    @Order(1)
    public void doBINTest()
    {
        Set<String> unknowns = new HashSet<>();
        try
        {
            Files.find(UtilHandler.CDRAGON_FOLDER.resolve("hashbins"), 2, (path, attr) -> path.toString().contains("UnknownBinHash")).forEach(p -> {
                unknowns.addAll(HashGuesser.unknownFromExportBIN(p));
            });
        } catch (IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
        
        BINHashGuesser guesser = new BINHashGuesser(unknowns, dataPath);
        guesser.pullCDTB();
        //guesser.guessFromFile(UtilHandler.CDRAGON_FOLDER.resolve("sorted_real.txt"), "(.*)");
        guesser.guessFromPets(dataPath);
        guesser.guessTFTItems();
        guesser.guessSpellNames();
        guesser.guessTFTParticles();
        guesser.guessNewCharacters();
        guesser.guessNewAnimations();
        guesser.guessFromFontFiles();
        guesser.saveToBackup();
    }
    
    @Test
    @Order(10)
    public void doGameTest()
    {
        GameHashGuesser guesser = new GameHashGuesser(HashGuesser.unknownFromExportWAD(UtilHandler.CDRAGON_FOLDER.resolve("unknownsSorted.txt")));
        guesser.pullCDTB();
        guesser.guessHardcoded();
        guesser.guessStringTableFiles();
        guesser.guessScripts(dataPath);
        guesser.guessShaderFiles(dataPath);
        guesser.guessAssetsBySearch(dataPath);
        guesser.guessBinByLinkedFiles(dataPath);
        guesser.saveToBackup();
        
        //guesser.guessByExistingWords();
        //guesser.saveToBackup();
    }
    
    @Test
    @Order(20)
    public void doLCUTest()
    {
        LCUHashGuesser guesser = new LCUHashGuesser(HashGuesser.unknownFromExportWAD(UtilHandler.CDRAGON_FOLDER.resolve("unknownsSorted.txt")));
        guesser.pullCDTB();
        guesser.guessManifestFiles();
        guesser.guessAssetsBySearch(dataPath);
        guesser.guessSanitizerHashes();
        guesser.saveToBackup();
        
        guesser.substituteRegionLang();
        guesser.substituteBasenames();
        guesser.substitutePlugins();
        guesser.saveToBackup();
        
        guesser.substituteBasenameWords(null, null, null, 1);
        guesser.addBasenameWord();
        guesser.saveToBackup();
        
        //guesser.guessByExistingWords();
        //guesser.saveToBackup();
    }
    
}
