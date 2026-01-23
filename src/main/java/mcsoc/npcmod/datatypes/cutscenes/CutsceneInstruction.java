package mcsoc.npcmod.datatypes.cutscenes;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.text.Text;
import mcsoc.npcmod.datatypes.PositionData;
import mcsoc.npcmod.util.Instruction;


public sealed interface CutsceneInstruction extends Instruction {

    static final String TYPE_KEY = "type";

    static final String ERROR_TYPE_NAME = "error";
    static final String SPAWN_NPC_TYPE_NAME = "spawn_npc";
    static final String SPAWN_MOVING_NPC_TYPE_NAME = "spawn_moving_npc";
    static final String DIALOGUE_TYPE_NAME = "dialogue";
    static final String DELAY_TYPE_NAME = "wait";
    static final String CAMERA_POSITION_TYPE_NAME = "position_camera";
    static final String CAMERA_MODE_TYPE_NAME = "modify_camera";

    static final String PROPERTIES_KEY = "properties";
    static final String NPC_ID_PROPERTY_KEY = "npc_id";
    static final String POSITION_PROPERTY_KEY = "position";
    static final String TEXT_PROPERTY_KEY = "text";
    static final String TICKS_PROPERTY_KEY = "ticks";
    static final String SPEED_PROPERTY_KEY = "speed";
    static final String MODE_PROPERTY_KEY = "mode";

    record ParseError() implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, ERROR_TYPE_NAME);
            JsonObject properties_json = new JsonObject();
            json.add(PROPERTIES_KEY, properties_json);

            return json;
        }
    }
    record SpawnNPC(String id, PositionData position_data) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, SPAWN_MOVING_NPC_TYPE_NAME);

            JsonObject properties_json = new JsonObject();
            properties_json.addProperty(NPC_ID_PROPERTY_KEY, this.id);
            properties_json.add(POSITION_PROPERTY_KEY, position_data.toJson());
            json.add(PROPERTIES_KEY, properties_json);

            return json;
        }
    }
    record SpawnMovingNPC(String id, PositionData position_data) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, SPAWN_MOVING_NPC_TYPE_NAME);

            JsonObject properties_json = new JsonObject();
            properties_json.addProperty(NPC_ID_PROPERTY_KEY, this.id);
            properties_json.add(POSITION_PROPERTY_KEY, position_data.toJson());
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

            JsonObject properties_json = new JsonObject();
            properties_json.addProperty(TICKS_PROPERTY_KEY, ticks);
            json.add(PROPERTIES_KEY, properties_json);

            return json;
        }
    }
    record PositionCamera(PositionData position_data) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, CAMERA_POSITION_TYPE_NAME);

            JsonObject properties_json = new JsonObject();
            properties_json.add(POSITION_PROPERTY_KEY, position_data.toJson());
            json.add(PROPERTIES_KEY, properties_json);

            return json;
        }
    }
    record SetCameraMode(CameraMode mode) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, CAMERA_MODE_TYPE_NAME);

            JsonObject properties_json = new JsonObject();
            properties_json.addProperty(MODE_PROPERTY_KEY, mode.asString());
            json.add(PROPERTIES_KEY, properties_json);

            return json;
        }
    }

    public abstract JsonObject toJson();


    public static CutsceneInstruction fromJson(JsonObject json) throws JsonParseException {
        String type_name = json.get(TYPE_KEY).getAsString();
        JsonObject properties = json.get(PROPERTIES_KEY).getAsJsonObject();
        try {
            return (
                switch (type_name) {
                    case SPAWN_NPC_TYPE_NAME -> new CutsceneInstruction.SpawnNPC(
                        properties.get(NPC_ID_PROPERTY_KEY).getAsString(), 
                        PositionData.fromJson(properties.get(POSITION_PROPERTY_KEY).getAsJsonObject())
                    );
                    case SPAWN_MOVING_NPC_TYPE_NAME -> new CutsceneInstruction.SpawnMovingNPC(
                        properties.get(NPC_ID_PROPERTY_KEY).getAsString(), 
                        PositionData.fromJson(properties.get(POSITION_PROPERTY_KEY).getAsJsonObject())
                    );
                    case DIALOGUE_TYPE_NAME -> new CutsceneInstruction.Dialogue(
                        Text.of(properties.get(TEXT_PROPERTY_KEY).getAsString())
                    );
                    case DELAY_TYPE_NAME -> new CutsceneInstruction.Delay(
                        properties.get(TICKS_PROPERTY_KEY).getAsInt()
                    );
                    case CAMERA_POSITION_TYPE_NAME -> new CutsceneInstruction.PositionCamera(
                        PositionData.fromJson(properties.get(POSITION_PROPERTY_KEY).getAsJsonObject())
                    );
                    case CAMERA_MODE_TYPE_NAME -> new CutsceneInstruction.SetCameraMode(
                        CameraMode.fromString(properties.get(MODE_PROPERTY_KEY).getAsString()).orElseThrow()
                    );
                    case ERROR_TYPE_NAME -> new CutsceneInstruction.ParseError();
                    default -> throw new JsonParseException(String.format("\"%s\" is not valid for MovementInstruction type name", type_name));
                }
            );
        } catch (Exception e) {
            return new CutsceneInstruction.ParseError();
        }
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