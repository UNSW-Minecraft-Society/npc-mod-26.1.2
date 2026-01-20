package mcsoc.npcmod.dataloader.jsonparser.npcdata;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import mcsoc.npcmod.dataloader.jsonparser.JsonDataParser;
import mcsoc.npcmod.datatypes.MovementData;
import mcsoc.npcmod.datatypes.npcs.MovementInstruction;
import mcsoc.npcmod.datatypes.npcs.MovingNPCData;


public interface MovingNPCJsonDataParser extends JsonDataParser {

    static GsonBuilder registerGsonTypes(GsonBuilder builder) {
        return builder
            .registerTypeAdapter(MovementData.class, new MovementData.JsonSerialiser())
            .registerTypeAdapter(MovementInstruction.class, new MovementInstruction.JsonSerialiser())
            .registerTypeAdapter(MovingNPCData.class, new MovingNPCData.JsonSerialiser())
        ;
    }

    static final Type movement_map_type = new TypeToken<Map<String, MovementData>>(){}.getType();
    static final Type moving_npc_map_type = new TypeToken<Map<String, MovingNPCData>>(){}.getType();


    public default void saveMovementData(String filename, Map<String, MovementData> movement_data_map) {
        this.writeToFileFromMap(filename, movement_data_map, movement_map_type);
    }
    public default Map<String, MovementData> loadMovementData(String filename) {
        return this.readFromFileToMap(filename, movement_map_type);
    }

    public default void saveMovingNPCData(String filename, Map<String, MovingNPCData> npc_data_map) {
        this.writeToFileFromMap(filename, npc_data_map, moving_npc_map_type);
    }
    public default Map<String, MovingNPCData> loadMovingNPCData(String filename) {
        return this.readFromFileToMap(filename, moving_npc_map_type);
    }

}
