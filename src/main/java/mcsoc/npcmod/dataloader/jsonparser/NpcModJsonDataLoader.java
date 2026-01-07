package mcsoc.npcmod.dataloader.jsonparser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mcsoc.npcmod.dataloader.datastorage.datatypes.CutsceneData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.CutsceneInstruction;
import mcsoc.npcmod.dataloader.datastorage.datatypes.DialogueData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.ModelData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovementData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovementInstruction;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovingNPCData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.NPCData;
import mcsoc.npcmod.dataloader.jsonparser.cutscenes.CutsceneJsonDataParser;
import mcsoc.npcmod.dataloader.jsonparser.npcdata.BasicNPCJsonDataLoader;
import mcsoc.npcmod.dataloader.jsonparser.npcdata.MovingNPCJsonDataLoader;


public class NpcModJsonDataLoader implements BasicNPCJsonDataLoader, MovingNPCJsonDataLoader, CutsceneJsonDataParser {

    private static final NpcModJsonDataLoader INSTANCE = new NpcModJsonDataLoader();

    static final Gson mapper = new GsonBuilder()
        .registerTypeAdapter(DialogueData.class, new DialogueData.JsonSerialiser())
        .registerTypeAdapter(ModelData.class, new ModelData.JsonSerialiser())
        .registerTypeAdapter(NPCData.class, new NPCData.JsonSerialiser())
        .registerTypeAdapter(MovementInstruction.class, new MovementInstruction.JsonSerialiser())
        .registerTypeAdapter(MovementData.class, new MovementData.JsonSerialiser())
        .registerTypeAdapter(MovingNPCData.class, new MovingNPCData.JsonSerialiser())
        .registerTypeAdapter(CutsceneInstruction.class, new CutsceneInstruction.JsonSerialiser())
        .registerTypeAdapter(CutsceneData.class, new CutsceneData.JsonSerialiser())
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
