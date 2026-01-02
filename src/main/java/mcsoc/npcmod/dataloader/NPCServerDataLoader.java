package mcsoc.npcmod.dataloader;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import mcsoc.npcmod.NPCMod;
import mcsoc.npcmod.entities.npc.BasicNPC;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NPCServerDataLoader {
    public static record ModelData(UUID uuid) {
        private static final String UUID_KEY = "uuid";
        public UUID getUuid() {
            return uuid;
        }

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(UUID_KEY, uuid.toString());
            return json;
        }

        public static ModelData fromJson(JsonObject json) {
            UUID uuid = UUID.fromString(json.get(UUID_KEY).getAsString());
            return new ModelData(uuid);
        }


        public static class JsonSerialiser implements JsonSerializer<ModelData>, JsonDeserializer<ModelData>{
            @Override
            public JsonElement serialize(ModelData src, Type typeOfSrc, JsonSerializationContext context) {
                return src.toJson();
            }

            @Override
            public ModelData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return ModelData.fromJson(json.getAsJsonObject());
            }
        }
    }
    public static record DialogueData(Text display_name, List<Text> dialogue, Identifier voice) {
        private static final String DISPLAY_NAME_KEY = "display_name";
        private static final String DIALOGUE_KEY = "dialogue";
        private static final String VOICE_KEY = "voice";
        private static final String NAMESPACE_KEY = "namespace";
        private static final String PATH_KEY = "path";
        

        public Text getFormattedMessage() {
            return display_name.copy().append(": ").append(dialogue.get(NPCMod.rand.nextInt(dialogue.size())));
        }


        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(DISPLAY_NAME_KEY, display_name.getString());

            JsonArray dialogue_lines = new JsonArray();
            dialogue.forEach(line -> dialogue_lines.add(line.getString()));
            json.add(DIALOGUE_KEY, dialogue_lines);

            JsonObject voice_json = new JsonObject();
            voice_json.addProperty(NAMESPACE_KEY, voice.getNamespace());
            voice_json.addProperty(PATH_KEY, voice.getPath());
            json.add(VOICE_KEY, voice_json);

            return json;
        }

        public static DialogueData fromJson(JsonObject json) {
            Text name = Text.literal(json.get(DISPLAY_NAME_KEY).getAsString());
            List<Text> dialogue = json.get(DIALOGUE_KEY).getAsJsonArray().asList().stream()
                    .map(JsonElement::getAsString)
                    .map(Text::literal)
                    .collect(Collectors.toUnmodifiableList());
            JsonObject voice_json = json.get(VOICE_KEY).getAsJsonObject();
            Identifier voice = Identifier.of(voice_json.get(NAMESPACE_KEY).getAsString(), voice_json.get(PATH_KEY).getAsString());
            return new DialogueData(name, dialogue, voice);
        }

        public static class JsonSerialiser implements JsonSerializer<DialogueData>, JsonDeserializer<DialogueData>{
            @Override
            public JsonElement serialize(DialogueData src, Type typeOfSrc, JsonSerializationContext context) {
                return src.toJson();
            }

            @Override
            public DialogueData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return DialogueData.fromJson(json.getAsJsonObject());
            }
        }
    }
    protected enum Mode {
        BACKGROUND {
            @Override
            public String toString() {
                return "background";
            }
        },
        MAIN {
            @Override
            public String toString() {
                return "main";
            }
        };

        @Override
        public abstract String toString();

        @Nullable
        public static Mode fromString(String string) {
            for (Mode mode: Mode.values()) {
                if (mode.toString().equals(string)) return mode;
            }
            return null;
        }
    }
    protected static record NPCData(String model_id, String dialogue_id, Mode mode) {
        private static final String MODEL_ID_KEY = "model_id";
        private static final String DIALOGUE_ID_KEY = "dialogue_id";
        private static final String MODE_KEY = "mode";

        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(MODEL_ID_KEY, model_id);
            json.addProperty(DIALOGUE_ID_KEY, dialogue_id);
            json.addProperty(MODE_KEY, mode.toString());
            return json;
        }

        public static NPCData fromJson(JsonObject json) {
            String model_id = json.get(MODEL_ID_KEY).getAsString();
            String dialogue_id = json.get(DIALOGUE_ID_KEY).getAsString();
            Mode mode = Mode.fromString(json.get(MODE_KEY).getAsString());
            if (Objects.isNull(mode)) mode = Mode.BACKGROUND;

            return new NPCData(model_id, dialogue_id, mode);
        }

        public static class JsonSerialiser implements JsonSerializer<NPCData>, JsonDeserializer<NPCData>{
            @Override
            public JsonElement serialize(NPCData src, Type typeOfSrc, JsonSerializationContext context) {
                return src.toJson();
            }

            @Override
            public NPCData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return NPCData.fromJson(json.getAsJsonObject());
            }
        }
    }


    private static final NPCServerDataLoader INSTANCE = new NPCServerDataLoader();

    private static final ModelData MODEL_NOT_FOUND = new ModelData(UUID.fromString("e2d03233-e72a-4389-9b41-12a9ff98bf3d"));
    private static final DialogueData DIALOGUE_NOT_FOUND = new DialogueData(Text.of("DEFAULT"), List.of(Text.of("Dialogue not found.")), Identifier.ofVanilla("music_disc.far"));
    private static final String MISSING_KEY = "mssngn";
    private static final NPCData NPC_NOT_FOUND = new NPCData(MISSING_KEY, MISSING_KEY, Mode.BACKGROUND);

    private static final String MODEL_DATA_PATH = NPCMod.MOD_ID + "/model_data.json";
    private static final String DIALOGUE_DATA_PATH = NPCMod.MOD_ID + "/dialogue_data.json";
    private static final String NPC_DATA_PATH = NPCMod.MOD_ID + "/npc_data.json";

    private final Map<String, NPCData> npc_data_map = new HashMap<>();
    private final Map<String, ModelData> model_data_map = new HashMap<>();
    private final Map<String, DialogueData> dialogue_data_map = new HashMap<>();

    private final NPCJsonDataParser parser = NPCJsonDataParser.getInstance();

    private NPCServerDataLoader() {
        this.loadData();
    }

    public void loadData() {
        this.model_data_map.putAll(parser.loadModelData(MODEL_DATA_PATH));
        this.dialogue_data_map.putAll(parser.loadDialogueData(DIALOGUE_DATA_PATH));
        this.npc_data_map.putAll(parser.loadNPCData(NPC_DATA_PATH));      
    }

    public static NPCServerDataLoader getInstance() {
        return INSTANCE;
    }


    public void registerModelData(String id, UUID uuid) {
        this.model_data_map.put(id, new ModelData(uuid));
    }
    public void registerDialogueData(String id, Text display_name, List<Text> dialogue, Identifier voice) {
        this.dialogue_data_map.put(id, new DialogueData(display_name, dialogue, voice));
    }
    public void registerBackgroundNPC(String id, String model_id, String dialogue_id) {
        this.npc_data_map.put(id, new NPCData(model_id, dialogue_id, Mode.BACKGROUND));
    }
    public void registerStoryNPC(String id, String model_id, String dialogue_id) {
        this.npc_data_map.put(id, new NPCData(model_id, dialogue_id, Mode.MAIN));
    }

    public void saveData() {
        parser.saveModelData(MODEL_DATA_PATH, this.model_data_map);
        parser.saveDialogueData(DIALOGUE_DATA_PATH, this.dialogue_data_map);
        parser.saveNPCData(NPC_DATA_PATH, this.npc_data_map);
    }

    public DialogueData getDialogue(BasicNPC npc) {
        NPCData npc_data = this.npc_data_map.getOrDefault(npc.getID(), NPC_NOT_FOUND);
        return this.dialogue_data_map.getOrDefault(npc_data.dialogue_id(), DIALOGUE_NOT_FOUND);
    }
    public ModelData getModel(BasicNPC npc) {
        NPCData npc_data = this.npc_data_map.getOrDefault(npc.getID(), NPC_NOT_FOUND);
        return this.model_data_map.getOrDefault(npc_data.model_id(), MODEL_NOT_FOUND);
    }
}
