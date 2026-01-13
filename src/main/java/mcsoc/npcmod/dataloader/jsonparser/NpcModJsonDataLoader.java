package mcsoc.npcmod.dataloader.jsonparser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mcsoc.npcmod.dataloader.jsonparser.cutscenes.CutsceneJsonDataParser;
import mcsoc.npcmod.dataloader.jsonparser.npcdata.BasicNPCJsonDataParser;
import mcsoc.npcmod.dataloader.jsonparser.npcdata.MovingNPCJsonDataParser;


public class NpcModJsonDataLoader implements BasicNPCJsonDataParser, MovingNPCJsonDataParser, CutsceneJsonDataParser {

    private static final NpcModJsonDataLoader INSTANCE = new NpcModJsonDataLoader();

    static final Gson mapper = CutsceneJsonDataParser.registerGsonTypes(MovingNPCJsonDataParser.registerGsonTypes(BasicNPCJsonDataParser.registerGsonTypes(new GsonBuilder())))
        .setPrettyPrinting()
        .create();


    private NpcModJsonDataLoader() { /* delete */ }
    public static NpcModJsonDataLoader getInstance() {
        return INSTANCE;
    }

    @Override
    public Gson getGsonParser() {
        return mapper;
    }
}
