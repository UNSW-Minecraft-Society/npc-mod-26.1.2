package veveddo.npcmod.dataloader.jsonparser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import veveddo.npcmod.dataloader.jsonparser.cutscenes.CutsceneJsonDataParser;
import veveddo.npcmod.dataloader.jsonparser.npcdata.BasicNPCJsonDataParser;
import veveddo.npcmod.dataloader.jsonparser.npcdata.MovingNPCJsonDataParser;


public class NpcModJsonDataLoader implements BasicNPCJsonDataParser, MovingNPCJsonDataParser, CutsceneJsonDataParser {

    private static final NpcModJsonDataLoader INSTANCE = new NpcModJsonDataLoader();

    static final Gson mapper;
    static {
        GsonBuilder builder = new GsonBuilder();
        builder = BasicNPCJsonDataParser.registerGsonTypes(builder);
        builder = MovingNPCJsonDataParser.registerGsonTypes(builder);
        builder = CutsceneJsonDataParser.registerGsonTypes(builder);
        mapper = builder.setPrettyPrinting().create();
    }


    private NpcModJsonDataLoader() { /* delete */ }
    public static NpcModJsonDataLoader getInstance() {
        return INSTANCE;
    }

    @Override
    public Gson getGsonParser() {
        return mapper;
    }
}