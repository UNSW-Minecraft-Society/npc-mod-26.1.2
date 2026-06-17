package veveddo.npcmod.datatypes.npcs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import veveddo.npcmod.NpcMod;


public record MovementData(List<MovementInstruction> movements) {
    private static final String MOVEMENTS_KEY = "move_list";

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonArray movement_json = new JsonArray();
        movements.forEach(movement -> movement_json.add(movement.toJson()));
        json.add(MOVEMENTS_KEY, movement_json);
        return json;
    }

    public static MovementData fromJson(JsonObject json) {
        List<MovementInstruction> movements = new ArrayList<>(1);
        try {
            movements = json.get(MOVEMENTS_KEY).getAsJsonArray().asList().stream()
                    .map(JsonElement::getAsJsonObject)
                    .map(MovementInstruction::fromJson)
                    .toList();
        } catch (NullPointerException e) {
            NpcMod.LOGGER.error("Failed to read from movements file: ", e);
        }
        return new MovementData(movements);
    }


    public static class JsonSerialiser implements JsonSerializer<MovementData>, JsonDeserializer<MovementData>{
        @Override
        public JsonElement serialize(MovementData src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        }

        @Override
        public MovementData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return MovementData.fromJson(json.getAsJsonObject());
        }
    }
}