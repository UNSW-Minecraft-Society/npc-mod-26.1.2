package veveddo.npcmod.datatypes.cutscenes;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import veveddo.npcmod.datatypes.PositionData;
import veveddo.npcmod.util.Instruction;


public sealed interface CutsceneInstruction extends Instruction {

    static final String TYPE_KEY = "type";

    static final String ERROR_TYPE_NAME = "error";
    static final String SPAWN_NPC_TYPE_NAME = "spawn_npc";
    static final String SPAWN_MOVING_NPC_TYPE_NAME = "spawn_moving_npc";
    static final String DIALOGUE_TYPE_NAME = "dialogue";
    static final String PLAYSOUND_TYPE_NAME = "play_sound";
    static final String PLAYSOUND_PLAYER_TYPE_NAME = "play_sound_at_players";
    static final String DELAY_TYPE_NAME = "wait";
    static final String CAMERA_POSITION_TYPE_NAME = "position_camera";
    static final String CAMERA_PAN_TYPE_NAME = "pan_camera";
    static final String CAMERA_MODE_TYPE_NAME = "modify_camera";

    static final String PROPERTIES_KEY = "properties";
    static final String NPC_ID_PROPERTY_KEY = "npc_id";
    static final String POSITION_PROPERTY_KEY = "position";
    static final String TEXT_PROPERTY_KEY = "text";
    static final String IDENTIFIER_PROPERTY_KEY = "identifier";
    static final String TICKS_PROPERTY_KEY = "ticks";
    static final String SPEED_PROPERTY_KEY = "speed";
    static final String MODE_PROPERTY_KEY = "mode";
    static final String SOUND_ID_KEY = "sound_id";
    static final String NAMESPACE_KEY = "namespace";
    static final String PATH_KEY = "path";


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
    record Dialogue(Component text) implements CutsceneInstruction {
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
    record PlaySound(Identifier sound_id, int x, int y, int z) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, PLAYSOUND_TYPE_NAME);

            JsonObject properties = new JsonObject();

            JsonObject sound_json = new JsonObject();
            sound_json.addProperty(NAMESPACE_KEY, sound_id.getNamespace());
            sound_json.addProperty(PATH_KEY, sound_id.getPath());
            properties.add(SOUND_ID_KEY, sound_json);

            JsonObject position_json = new JsonObject();
            position_json.addProperty("x", this.x());
            position_json.addProperty("y", this.y());
            position_json.addProperty("z", this.z());
            properties.add(POSITION_PROPERTY_KEY, position_json);

            json.add(PROPERTIES_KEY, properties);

            return json;
        }
    }
    record PlaySoundPlayers(Identifier sound_id) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, PLAYSOUND_PLAYER_TYPE_NAME);

            JsonObject properties = new JsonObject();
            JsonObject sound_json = new JsonObject();
            sound_json.addProperty(NAMESPACE_KEY, sound_id.getNamespace());
            sound_json.addProperty(PATH_KEY, sound_id.getPath());
            properties.add(SOUND_ID_KEY, sound_json);

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
    record PanCamera(PositionData from, PositionData to, int ticks) implements CutsceneInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, CAMERA_PAN_TYPE_NAME);

            JsonObject properties_json = new JsonObject();
            properties_json.add("from", from.toJson());
            properties_json.add("to", to.toJson());
            properties_json.addProperty(TICKS_PROPERTY_KEY, ticks);
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
            properties_json.addProperty(MODE_PROPERTY_KEY, mode.getSerializedName());
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
                        Component.literal(properties.get(TEXT_PROPERTY_KEY).getAsString())
                    );
                    case PLAYSOUND_TYPE_NAME -> {
                        JsonObject voice_json = json.get(SOUND_ID_KEY).getAsJsonObject();
                        yield new CutsceneInstruction.PlaySound(
                            Identifier.fromNamespaceAndPath(voice_json.get(NAMESPACE_KEY).getAsString(), voice_json.get(PATH_KEY).getAsString()),
                            properties.get("x").getAsInt(),
                            properties.get("y").getAsInt(),
                            properties.get("z").getAsInt()
                        );
                    }
                    case PLAYSOUND_PLAYER_TYPE_NAME -> {
                        JsonObject voice_json = json.get(SOUND_ID_KEY).getAsJsonObject();
                        yield new CutsceneInstruction.PlaySoundPlayers(
                            Identifier.fromNamespaceAndPath(voice_json.get(NAMESPACE_KEY).getAsString(), voice_json.get(PATH_KEY).getAsString())
                        );
                    }
                    case DELAY_TYPE_NAME -> new CutsceneInstruction.Delay(
                        properties.get(TICKS_PROPERTY_KEY).getAsInt()
                    );
                    case CAMERA_POSITION_TYPE_NAME -> new CutsceneInstruction.PositionCamera(
                        PositionData.fromJson(properties.get(POSITION_PROPERTY_KEY).getAsJsonObject())
                    );
                    case CAMERA_PAN_TYPE_NAME -> new CutsceneInstruction.PanCamera(
                        PositionData.fromJson(properties.get("from").getAsJsonObject()),
                        PositionData.fromJson(properties.get("to").getAsJsonObject()),
                        properties.get(TICKS_PROPERTY_KEY).getAsInt()
                    );
                    case CAMERA_MODE_TYPE_NAME -> new CutsceneInstruction.SetCameraMode(
                        CameraMode.fromString(properties.get(MODE_PROPERTY_KEY).getAsString()).orElseThrow()
                    );
                    case ERROR_TYPE_NAME -> new CutsceneInstruction.ParseError();
                    default -> throw new JsonParseException(String.format("\"%s\" is not valid for CutsceneInstruction type name", type_name));
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