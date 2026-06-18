package mcsoc.npcmod.datatypes.npcs;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;


public record MovingNPCData(NPCData base_data, String movement_id) {
    private static final String MOVEMENT_ID_KEY = "movement_id";
    public JsonObject toJson() {
        JsonObject json = base_data.toJson();
        json.addProperty(MOVEMENT_ID_KEY, this.movement_id);
        return json;
    }

    public static MovingNPCData fromJson(JsonObject json) {
        NPCData base = NPCData.fromJson(json);
        String movement_id = json.get(MOVEMENT_ID_KEY).getAsString();
        return new MovingNPCData(base, movement_id);
    }

    public static class JsonSerialiser implements JsonSerializer<MovingNPCData>, JsonDeserializer<MovingNPCData>{
        @Override
        public JsonElement serialize(MovingNPCData src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        }

        @Override
        public MovingNPCData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return MovingNPCData.fromJson(json.getAsJsonObject());
        }
    }

    public static final StreamCodec<ByteBuf, MovingNPCData> PACKET_CODEC = StreamCodec.composite(
        NPCData.PACKET_CODEC, MovingNPCData::base_data,
        ByteBufCodecs.STRING_UTF8, MovingNPCData::movement_id,
        MovingNPCData::new
    );
}