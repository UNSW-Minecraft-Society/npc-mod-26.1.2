package mcsoc.npcmod.datatypes.npcs;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ModelData(UUID uuid) {
    private static final String UUID_KEY = "uuid";

    public String getUuidAsString() {
        return uuid.toString();
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

    public static final PacketCodec<RegistryByteBuf, ModelData> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING.xmap(UUID::fromString, UUID::toString), ModelData::uuid,
        ModelData::new
    );
}   