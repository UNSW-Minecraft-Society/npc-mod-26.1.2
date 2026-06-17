package veveddo.npcmod.datatypes;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record PositionData(double x, double y, double z, float yaw, float pitch) {

    public static PositionData fromPosAndAngles(Vec3 pos, float yaw, float pitch) {
        return new PositionData(pos.x, pos.y, pos.z, yaw, pitch);
    }

    public PositionData add(PositionData other) {
        return new PositionData(x + other.x, y + other.y, z + other.z, yaw + other.yaw, pitch + other.pitch);
    }
    public PositionData subtract(PositionData other) {
        return new PositionData(x - other.x, y - other.y, z - other.z, yaw - other.yaw, pitch - other.pitch);
    }

    public PositionData multiply(float f) {
        return new PositionData(x * f, y * f, z * f, yaw * f, pitch * f);
    }
    public PositionData divide(float f) {
        return new PositionData(x / f, y / f, z / f, yaw / f, pitch / f);
    }

    public Vec3 getPos() {
        return new Vec3(x, y, z);
    }
    public BlockPos getBlockPos() {
        return BlockPos.containing(getPos());
    }

    public PositionData addPos(Vec3 offset) {
        return new PositionData(x + offset.x, y + offset.y, z + offset.z, yaw, pitch);
    }
    public PositionData addAngles(float off_yaw, float off_pitch) {
        return new PositionData(x, y, z, yaw + off_yaw, pitch + off_pitch);
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("x", this.x());
        json.addProperty("y", this.y());
        json.addProperty("z", this.z());
        json.addProperty("yaw", this.yaw());
        json.addProperty("pitch", this.pitch());
        return json;
    }


    public static PositionData fromEntity(Entity e) {
        return new PositionData(e.getX(), e.getY(), e.getZ(), e.getYRot(), e.getXRot());
    }

    public static PositionData fromJson(JsonObject json) {
        return new PositionData(
            json.get("x").getAsFloat(),
            json.get("y").getAsFloat(),
            json.get("z").getAsFloat(),
            json.get("yaw").getAsFloat(),
            json.get("pitch").getAsFloat()
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

    public static final StreamCodec<ByteBuf, PositionData> PACKET_CODEC = StreamCodec.composite(
        ByteBufCodecs.DOUBLE, PositionData::x,
        ByteBufCodecs.DOUBLE, PositionData::y,
        ByteBufCodecs.DOUBLE, PositionData::z,
        ByteBufCodecs.FLOAT, PositionData::yaw,
        ByteBufCodecs.FLOAT, PositionData::pitch,
        PositionData::new
    );
}
