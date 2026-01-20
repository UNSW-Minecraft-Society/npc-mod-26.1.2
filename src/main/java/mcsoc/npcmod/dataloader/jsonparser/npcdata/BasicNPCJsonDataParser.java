package mcsoc.npcmod.dataloader.jsonparser.npcdata;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mcsoc.npcmod.dataloader.jsonparser.JsonDataParser;
import mcsoc.npcmod.datatypes.npcs.DialogueData;
import mcsoc.npcmod.datatypes.npcs.ModelData;
import mcsoc.npcmod.datatypes.npcs.NPCData;

import java.lang.reflect.Type;
import java.util.Map;


public interface BasicNPCJsonDataParser extends JsonDataParser {

    static GsonBuilder registerGsonTypes(GsonBuilder builder) {
        return builder.registerTypeAdapter(DialogueData.class, new DialogueData.JsonSerialiser())
        .registerTypeAdapter(ModelData.class, new ModelData.JsonSerialiser())
        .registerTypeAdapter(NPCData.class, new NPCData.JsonSerialiser());
    }

    static final Type model_map_type = new TypeToken<Map<String, ModelData>>(){}.getType();
    static final Type dialogue_map_type = new TypeToken<Map<String, DialogueData>>(){}.getType();
    static final Type npc_map_type = new TypeToken<Map<String, NPCData>>(){}.getType();


    public default void saveModelData(String filename, Map<String, ModelData> model_data_map) {
        this.writeToFileFromMap(filename, model_data_map, model_map_type);
    }
    public default Map<String, ModelData> loadModelData(String filename) {
        return this.readFromFileToMap(filename, model_map_type);
    }

    public default void saveDialogueData(String filename, Map<String, DialogueData> dialogue_data_map) {
        this.writeToFileFromMap(filename, dialogue_data_map, dialogue_map_type);
    }
    public default Map<String, DialogueData> loadDialogueData(String filename) {
        return this.readFromFileToMap(filename, dialogue_map_type);
    }

    public default void saveNPCData(String filename, Map<String, NPCData> npc_data_map) {
        this.writeToFileFromMap(filename, npc_data_map, npc_map_type);
    }
    public default Map<String, NPCData> loadNPCData(String filename) {
        return this.readFromFileToMap(filename, npc_map_type);
    }
}