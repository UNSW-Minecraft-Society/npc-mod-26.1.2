package mcsoc.npcmod.dataloader.jsonparser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mcsoc.npcmod.dataloader.datastorage.datatypes.DialogueData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.ModelData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovementData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovementInstruction;
import mcsoc.npcmod.dataloader.datastorage.datatypes.MovingNPCData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.NPCData;

import java.lang.reflect.Type;
import java.util.Map;


public class NPCJsonDataParser extends JsonDataParser {
    // With default settings can use
    private static final Gson mapper = new GsonBuilder()
            .registerTypeAdapter(DialogueData.class, new DialogueData.JsonSerialiser())
            .registerTypeAdapter(ModelData.class, new ModelData.JsonSerialiser())
            .registerTypeAdapter(NPCData.class, new NPCData.JsonSerialiser())
            .registerTypeAdapter(MovementInstruction.class, new MovementInstruction.JsonSerialiser())
            .registerTypeAdapter(MovementData.class, new MovementData.JsonSerialiser())
            .registerTypeAdapter(MovingNPCData.class, new MovingNPCData.JsonSerialiser())
            .setPrettyPrinting()
            .create();

    private static final Type model_map_type = new TypeToken<Map<String, ModelData>>(){}.getType();
    private static final Type dialogue_map_type = new TypeToken<Map<String, DialogueData>>(){}.getType();
    private static final Type npc_map_type = new TypeToken<Map<String, NPCData>>(){}.getType();

    private static final Type movement_map_type = new TypeToken<Map<String, MovementData>>(){}.getType();
    private static final Type moving_npc_map_type = new TypeToken<Map<String, MovingNPCData>>(){}.getType();

    private static final NPCJsonDataParser INSTANCE = new NPCJsonDataParser(); 

    private NPCJsonDataParser() {}
    public static NPCJsonDataParser getInstance() {
        return INSTANCE;
    }

    @Override
    protected Gson getGsonParser() {
        return mapper;
    }

    public void saveModelData(String filename, Map<String, ModelData> model_data_map) {
        this.writeToFileFromMap(filename, model_data_map, model_map_type);
    }
    public Map<String, ModelData> loadModelData(String filename) {
        return this.readFromFileToMap(filename, model_map_type);
    }

    public void saveDialogueData(String filename, Map<String, DialogueData> dialogue_data_map) {
        this.writeToFileFromMap(filename, dialogue_data_map, dialogue_map_type);
    }
    public Map<String, DialogueData> loadDialogueData(String filename) {
        return this.readFromFileToMap(filename, dialogue_map_type);
    }

    public void saveNPCData(String filename, Map<String, NPCData> npc_data_map) {
        this.writeToFileFromMap(filename, npc_data_map, npc_map_type);
    }
    public Map<String, NPCData> loadNPCData(String filename) {
        return this.readFromFileToMap(filename, npc_map_type);
    }

    
    public void saveMovementData(String filename, Map<String, MovementData> movement_data_map) {
        this.writeToFileFromMap(filename, movement_data_map, movement_map_type);
    }
    public Map<String, MovementData> loadMovementData(String filename) {
        return this.readFromFileToMap(filename, movement_map_type);
    }

    public void saveMovingNPCData(String filename, Map<String, MovingNPCData> npc_data_map) {
        this.writeToFileFromMap(filename, npc_data_map, moving_npc_map_type);
    }
    public Map<String, MovingNPCData> loadMovingNPCData(String filename) {
        return this.readFromFileToMap(filename, moving_npc_map_type);
    }
}