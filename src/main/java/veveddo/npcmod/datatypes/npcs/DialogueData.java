package veveddo.npcmod.datatypes.npcs;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import veveddo.npcmod.NpcMod;

public record DialogueData(Component display_name, List<Component> dialogue, ResourceLocation voice) {
    private static final String DISPLAY_NAME_KEY = "display_name";
    private static final String DIALOGUE_KEY = "dialogue";
    private static final String VOICE_KEY = "voice";
    private static final String NAMESPACE_KEY = "namespace";
    private static final String PATH_KEY = "path";
    

    public Component getFormattedMessage() {
        return Component.literal("<").append(display_name.copy()).append("> ").append(dialogue.get(NpcMod.rand.nextInt(dialogue.size())));
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
        Component name = Component.literal("");
        List<Component> dialogue = new ArrayList<>(1);
        ResourceLocation voice = ResourceLocation.withDefaultNamespace("block.anvil.break");
        try {
            name = Component.literal(json.get(DISPLAY_NAME_KEY).getAsString());
            dialogue = json.get(DIALOGUE_KEY).getAsJsonArray().asList().stream()
                    .map(JsonElement::getAsString)
                    .map(Component::literal)
                    .collect(Collectors.toUnmodifiableList());
            JsonObject voice_json = json.get(VOICE_KEY).getAsJsonObject();
            voice = ResourceLocation.fromNamespaceAndPath(voice_json.get(NAMESPACE_KEY).getAsString(), voice_json.get(PATH_KEY).getAsString());
        } catch (NullPointerException e) {
            NpcMod.LOGGER.error("Failed to read from dialogue file: ", e);
        }
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

    public static final StreamCodec<RegistryFriendlyByteBuf, DialogueData> PACKET_CODEC = StreamCodec.composite(
        ComponentSerialization.TRUSTED_STREAM_CODEC, DialogueData::display_name,
        ByteBufCodecs.collection(ArrayList::new, ComponentSerialization.TRUSTED_STREAM_CODEC), DialogueData::dialogue,
        ResourceLocation.STREAM_CODEC, DialogueData::voice,
        DialogueData::new
    );
}
