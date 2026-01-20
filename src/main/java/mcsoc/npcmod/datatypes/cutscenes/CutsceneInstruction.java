package mcsoc.npcmod.datatypes.cutscenes;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import mcsoc.npcmod.util.Instruction;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public sealed interface CutsceneInstruction extends Instruction {

    public static record FacingData(float pitch, float yaw) {}

    static final String TYPE_KEY = "type";
    static final String SPAWN_NPC_TYPE_NAME = "spawn_npc";
    static final String SPAWN_MOVING_NPC_TYPE_NAME = "spawn_moving_npc";
    static final String DIALOGUE_TYPE_NAME = "dialogue";
    static final String DELAY_TYPE_NAME = "wait";

    static final String PROPERTIES_KEY = "properties";
    static final String NPC_ID_PROPERTY_KEY = "npc_id";
    static final String POSITION_PROPERTY_KEY = "position";
    static final String FACING_PROPERTY_KEY = "facing";
    static final String TEXT_PROPERTY_KEY = "text";
    static final String TICKS_PROPERTY_KEY = "ticks";
    static final String SPEED_PROPERTY_KEY = "speed";

    record SpawnNPC(String id, BlockPos position, FacingData facing) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, SPAWN_MOVING_NPC_TYPE_NAME);

            JsonObject properties_json = new JsonObject();

            properties_json.addProperty(NPC_ID_PROPERTY_KEY, this.id);

            JsonObject pos_json = new JsonObject();
            pos_json.addProperty("x", position.getX());
            pos_json.addProperty("y", position.getY());
            pos_json.addProperty("z", position.getZ());
            properties_json.add(POSITION_PROPERTY_KEY, pos_json);

            JsonObject facing_json = new JsonObject();
            facing_json.addProperty("pitch", facing.pitch());
            facing_json.addProperty("yaw", facing.yaw());
            properties_json.add(FACING_PROPERTY_KEY, facing_json);

            json.add(PROPERTIES_KEY, properties_json);

            return json;
        }
    }
    record SpawnMovingNPC(String id, BlockPos position, FacingData facing) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, SPAWN_MOVING_NPC_TYPE_NAME);

            JsonObject properties_json = new JsonObject();

            properties_json.addProperty(NPC_ID_PROPERTY_KEY, this.id);

            JsonObject pos_json = new JsonObject();
            pos_json.addProperty("x", position.getX());
            pos_json.addProperty("y", position.getY());
            pos_json.addProperty("z", position.getZ());
            properties_json.add(POSITION_PROPERTY_KEY, pos_json);

            JsonObject facing_json = new JsonObject();
            facing_json.addProperty("pitch", facing.pitch());
            facing_json.addProperty("yaw", facing.yaw());
            properties_json.add(FACING_PROPERTY_KEY, facing_json);

            json.add(PROPERTIES_KEY, properties_json);

            return json;
        }
    }
    record Dialogue(Text text) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, DIALOGUE_TYPE_NAME);

            JsonObject properties = new JsonObject();

            properties.addProperty(TEXT_PROPERTY_KEY, text.getString());

            json.add(PROPERTIES_KEY, properties);

            return json;
        }
    }
    record Delay(int ticks) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, DELAY_TYPE_NAME);

            JsonObject properties = new JsonObject();

            properties.addProperty(TICKS_PROPERTY_KEY, ticks);

            json.add(PROPERTIES_KEY, properties);

            return json;
        }
    }

    public abstract JsonObject toJson();

    private static BlockPos getBlockPosFromJson(JsonObject block_pos_data) {
        return new BlockPos(
            block_pos_data.get("x").getAsInt(),
            block_pos_data.get("y").getAsInt(),
            block_pos_data.get("z").getAsInt()
        );
    }
    private static FacingData getFacingFromJson(JsonObject facing_data) {
        return new FacingData(
            (float) facing_data.get("pitch").getAsDouble(),
            (float) facing_data.get("yaw").getAsDouble()
        );
    }

    public static CutsceneInstruction fromJson(JsonObject json) throws JsonParseException {
        String type_name = json.get(TYPE_KEY).getAsString();
        JsonObject properties = json.get(PROPERTIES_KEY).getAsJsonObject();
        return (
            switch (type_name) {
                case SPAWN_NPC_TYPE_NAME -> new CutsceneInstruction.SpawnNPC(
                    properties.get(NPC_ID_PROPERTY_KEY).getAsString(), 
                    getBlockPosFromJson(properties.get(POSITION_PROPERTY_KEY).getAsJsonObject()),
                    getFacingFromJson(properties.get(FACING_PROPERTY_KEY).getAsJsonObject())
                );
                case SPAWN_MOVING_NPC_TYPE_NAME -> new CutsceneInstruction.SpawnMovingNPC(
                    properties.get(NPC_ID_PROPERTY_KEY).getAsString(), 
                    getBlockPosFromJson(properties.get(POSITION_PROPERTY_KEY).getAsJsonObject()),
                    getFacingFromJson(properties.get(FACING_PROPERTY_KEY).getAsJsonObject())
                );
                case DIALOGUE_TYPE_NAME -> new CutsceneInstruction.Dialogue(
                    Text.of(properties.get(TEXT_PROPERTY_KEY).getAsString())
                );
                case DELAY_TYPE_NAME -> new CutsceneInstruction.Delay(
                    properties.get(TICKS_PROPERTY_KEY).getAsInt()
                );

                default -> throw new JsonParseException(String.format("\"%s\" is not valid for MovementInstruction type name", type_name));
            }
        );
    }

    public static class JsonSerialiser implements JsonSerializer<CutsceneInstruction>, JsonDeserializer<CutsceneInstruction> {
        @Override
        public JsonElement serialize(CutsceneInstruction src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        }
        @Override
        public CutsceneInstruction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return CutsceneInstruction.fromJson(json.getAsJsonObject());
        }
    }
}