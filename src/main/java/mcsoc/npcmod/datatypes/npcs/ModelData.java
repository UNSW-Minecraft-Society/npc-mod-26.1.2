package mcsoc.npcmod.datatypes.npcs;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import mcsoc.npcmod.NpcMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;


public record ModelData(ResourceLocation texture, boolean is_slim) {
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
        ResourceLocation texture = ResourceLocation.withDefaultNamespace("textures/entity/player/slim/alex");
        Boolean is_slim = true;
        try {
            JsonObject texture_json = json.get(TEXTURE_KEY).getAsJsonObject();
            texture = ResourceLocation.fromNamespaceAndPath(texture_json.get(NAMESPACE_KEY).getAsString(), texture_json.get(PATH_KEY).getAsString());
            is_slim = json.get(SLIM_KEY).getAsBoolean();
        } catch (NullPointerException e) {
            NpcMod.LOGGER.error("Failed to read from model_data.json: ", e);
        }
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

    public static final StreamCodec<RegistryFriendlyByteBuf, ModelData> PACKET_CODEC = StreamCodec.composite(
        
        ByteBufCodecs.STRING_UTF8.map(ResourceLocation::parse, ResourceLocation::toString), ModelData::texture,
        ByteBufCodecs.BOOL, ModelData::is_slim,
        ModelData::new
    );
}   