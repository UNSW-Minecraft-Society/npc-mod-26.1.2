package mcsoc.npcmod.dataloader.datastorage.datatypes;

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

import mcsoc.npcmod.NPCMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

public record DialogueData(Text display_name, List<Text> dialogue, Identifier voice) {
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

    public static final PacketCodec<RegistryByteBuf, DialogueData> PACKET_CODEC = PacketCodec.tuple(
        TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC, DialogueData::display_name,
        PacketCodecs.collection(ArrayList::new, TextCodecs.UNLIMITED_REGISTRY_PACKET_CODEC), DialogueData::dialogue,
        Identifier.PACKET_CODEC, DialogueData::voice,
        DialogueData::new
    );
}
