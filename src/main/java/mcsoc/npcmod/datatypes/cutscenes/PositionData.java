package mcsoc.npcmod.datatypes.cutscenes;

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
import net.minecraft.util.math.Vec3d;

public record PositionData(double x, double y, double z, float pitch, float yaw) {

    public Vec3d getPos() {
        return new Vec3d(x, y, z);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("x", this.x());
        json.addProperty("y", this.y());
        json.addProperty("z", this.z());
        json.addProperty("pitch", this.pitch());
        json.addProperty("yaw", this.yaw());
        return json;
    }

    public static PositionData fromJson(JsonObject json) {
        return new PositionData(
            json.get("x").getAsFloat(),
            json.get("y").getAsFloat(),
            json.get("z").getAsFloat(),
            json.get("pitch").getAsFloat(),
            json.get("yaw").getAsFloat()
        );
    }

    public static class JsonSerialiser implements JsonSerializer<PositionData>, JsonDeserializer<PositionData>{
        @Override
        public JsonElement serialize(PositionData src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        }

        @Override
        public PositionData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return PositionData.fromJson(json.getAsJsonObject());
        }
    }

    public static final PacketCodec<ByteBuf, PositionData> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.DOUBLE, PositionData::x,
        PacketCodecs.DOUBLE, PositionData::y,
        PacketCodecs.DOUBLE, PositionData::z,
        PacketCodecs.FLOAT, PositionData::pitch,
        PacketCodecs.FLOAT, PositionData::yaw,
        PositionData::new
    );
}
