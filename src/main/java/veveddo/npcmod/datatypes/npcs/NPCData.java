package veveddo.npcmod.datatypes.npcs;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record NPCData(String model_id, String dialogue_id, NPCMode mode) {
    private static final String MODEL_ID_KEY = "model_id";
    private static final String DIALOGUE_ID_KEY = "dialogue_id";
    private static final String MODE_KEY = "mode";

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty(MODEL_ID_KEY, model_id);
        json.addProperty(DIALOGUE_ID_KEY, dialogue_id);
        json.addProperty(MODE_KEY, mode.asString());
        return json;
    }

    public static NPCData fromJson(JsonObject json) {
        String model_id = json.get(MODEL_ID_KEY).getAsString();
        String dialogue_id = json.get(DIALOGUE_ID_KEY).getAsString();
        NPCMode mode = NPCMode.fromString(json.get(MODE_KEY).getAsString()).orElse(NPCMode.BACKGROUND);

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

    public static final PacketCodec<ByteBuf, NPCData> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, NPCData::model_id,
        PacketCodecs.STRING, NPCData::dialogue_id,
        NPCMode.PACKET_CODEC, NPCData::mode,
        NPCData::new
    );
}
