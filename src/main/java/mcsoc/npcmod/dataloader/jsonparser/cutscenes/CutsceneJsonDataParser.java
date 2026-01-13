package mcsoc.npcmod.dataloader.jsonparser.cutscenes;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mcsoc.npcmod.dataloader.datastorage.datatypes.CutsceneData;
import mcsoc.npcmod.dataloader.datastorage.datatypes.CutsceneInstruction;
import mcsoc.npcmod.dataloader.jsonparser.JsonDataParser;

import java.lang.reflect.Type;
import java.util.Map;


public interface CutsceneJsonDataParser extends JsonDataParser {

    static GsonBuilder registerGsonTypes(GsonBuilder builder) {
        return builder.registerTypeAdapter(CutsceneInstruction.class, new CutsceneInstruction.JsonSerialiser())
        .registerTypeAdapter(CutsceneData.class, new CutsceneData.JsonSerialiser());
    }

    static final Type cutscene_map_type = new TypeToken<Map<String, CutsceneData>>(){}.getType();


    public default void saveCutsceneData(String filename, Map<String, CutsceneData> cutscene_data_map) {
        this.writeToFileFromMap(filename, cutscene_data_map, cutscene_map_type);
    }
    public default Map<String, CutsceneData> loadCutsceneData(String filename) {
        return this.readFromFileToMap(filename, cutscene_map_type);
    }
}