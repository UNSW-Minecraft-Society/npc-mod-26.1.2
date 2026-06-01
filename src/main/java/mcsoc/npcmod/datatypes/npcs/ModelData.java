package mcsoc.npcmod.datatypes.npcs;

import java.lang.reflect.Type;

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
import net.minecraft.util.Identifier;

public record ModelData(Identifier texture, boolean is_slim) {
    private static final String TEXTURE_KEY = "texture";
    private static final String NAMESPACE_KEY = "namespace";
    private static final String PATH_KEY = "path";
    private static final String SLIM_KEY = "is_slim";


    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        
        JsonObject texture_json = new JsonObject();
        texture_json.addProperty(NAMESPACE_KEY, this.texture.getNamespace());
        texture_json.addProperty(PATH_KEY, this.texture.getPath());
        json.add(TEXTURE_KEY, texture_json);

        json.addProperty(SLIM_KEY, is_slim);

        return json;
    }

    public static ModelData fromJson(JsonObject json) {
        JsonObject texture_json = json.get(TEXTURE_KEY).getAsJsonObject();
        Identifier texture = Identifier.of(texture_json.get(NAMESPACE_KEY).getAsString(), texture_json.get(PATH_KEY).getAsString());
        Boolean is_slim = json.get(SLIM_KEY).getAsBoolean();
        return new ModelData(texture, is_slim);
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
        PacketCodecs.STRING.xmap(Identifier::of, Identifier::toString), ModelData::texture,
        PacketCodecs.BOOL, ModelData::is_slim,
        ModelData::new
    );
}   