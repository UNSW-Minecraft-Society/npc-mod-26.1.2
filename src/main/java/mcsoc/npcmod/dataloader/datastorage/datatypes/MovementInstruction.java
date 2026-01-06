package mcsoc.npcmod.dataloader.datastorage.datatypes;

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

public sealed interface MovementInstruction {
    static final String TYPE_KEY = "type";
    static final String WALK_TYPE_NAME = "walk";
    static final String SWIM_TYPE_NAME = "swim";
    static final String TURN_TYPE_NAME = "turn";
    static final String STOP_TYPE_NAME = "stop";
    static final String JUMP_TYPE_NAME = "jump";
    static final String TICKS_PROPERTY_KEY = "ticks";
    static final String SPEED_PROPERTY_KEY = "speed";
    static final String PROPERTIES_KEY = "properties";

    record Walk(int ticks, double speed) implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, WALK_TYPE_NAME);

            JsonObject properties = new JsonObject();
            properties.addProperty(TICKS_PROPERTY_KEY, this.ticks);
            properties.addProperty(SPEED_PROPERTY_KEY, this.speed);
            json.add(PROPERTIES_KEY, properties);

            return json;
        }

        public static PacketCodec<ByteBuf, MovementInstruction> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, Walk::ticks,
            PacketCodecs.DOUBLE, Walk::speed,
            Walk::new
        ).<MovementInstruction>xmap(w -> w, i -> (Walk) i);
    }
    record Swim(int ticks, double speed) implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, SWIM_TYPE_NAME);

            JsonObject properties = new JsonObject();
            properties.addProperty(TICKS_PROPERTY_KEY, this.ticks);
            properties.addProperty(SPEED_PROPERTY_KEY, this.speed);
            json.add(PROPERTIES_KEY, properties);
            
            return json;
        }

        public static PacketCodec<ByteBuf, MovementInstruction> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, Swim::ticks,
            PacketCodecs.DOUBLE, Swim::speed,
            Swim::new
        ).<MovementInstruction>xmap(s -> s, i -> (Swim)i);
    }
    record Turn(int ticks, double degrees) implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, TURN_TYPE_NAME);

            JsonObject properties = new JsonObject();
            properties.addProperty(TICKS_PROPERTY_KEY, this.ticks);
            properties.addProperty("degrees", this.degrees);
            json.add(PROPERTIES_KEY, properties);
            
            return json;
        }

        public static PacketCodec<ByteBuf, MovementInstruction> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, Turn::ticks,
            PacketCodecs.DOUBLE, Turn::degrees,
            Turn::new
        ).<MovementInstruction>xmap(t -> t, i -> (Turn) i);
    }
    record Stop(int ticks) implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, STOP_TYPE_NAME);

            JsonObject properties = new JsonObject();
            properties.addProperty(TICKS_PROPERTY_KEY, this.ticks);
            json.add(PROPERTIES_KEY, properties);
            
            return json;
        }

        public static PacketCodec<ByteBuf, MovementInstruction> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, Stop::ticks,
            Stop::new
        ).<MovementInstruction>xmap(s -> s, i -> (Stop) i);
    }
    record Jump() implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, JUMP_TYPE_NAME);
            json.add(PROPERTIES_KEY, new JsonObject());
            return json;
        }

        public static PacketCodec<ByteBuf, MovementInstruction> PACKET_CODEC = PacketCodec.unit(new Jump());
    }

    public abstract JsonObject toJson();

    public static MovementInstruction fromJson(JsonObject json) throws JsonParseException {
        String type_name = json.get(TYPE_KEY).getAsString();
        JsonObject properties = json.get(PROPERTIES_KEY).getAsJsonObject();
        return (
            switch (type_name) {
                case WALK_TYPE_NAME -> new MovementInstruction.Walk(
                    properties.get(TICKS_PROPERTY_KEY).getAsInt(), 
                    properties.get(SPEED_PROPERTY_KEY).getAsDouble()
                );
                case SWIM_TYPE_NAME -> new MovementInstruction.Swim(
                    properties.get(TICKS_PROPERTY_KEY).getAsInt(), 
                    properties.get(SPEED_PROPERTY_KEY).getAsDouble()
                );
                case TURN_TYPE_NAME -> new MovementInstruction.Turn(
                    properties.get(TICKS_PROPERTY_KEY).getAsInt(), 
                    properties.get("degrees").getAsDouble()
                );
                case STOP_TYPE_NAME -> new MovementInstruction.Stop(
                    properties.get(TICKS_PROPERTY_KEY).getAsInt()
                );
                case JUMP_TYPE_NAME -> new MovementInstruction.Jump();

                default -> throw new JsonParseException(String.format("\"%s\" is not valid for MovementInstruction type name", type_name ));
            }
        );
    }

    public static class JsonSerialiser implements JsonSerializer<MovementInstruction>, JsonDeserializer<MovementInstruction> {
        @Override
        public JsonElement serialize(MovementInstruction src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        }
        @Override
        public MovementInstruction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return MovementInstruction.fromJson(json.getAsJsonObject());
        }
    }     

    public static final PacketCodec<ByteBuf, MovementInstruction> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public MovementInstruction decode(ByteBuf buf) {
            int id = buf.readByte();
            return switch (id) {
                case 0 -> Walk.PACKET_CODEC.decode(buf);
                case 1 -> Swim.PACKET_CODEC.decode(buf);
                case 2 -> Turn.PACKET_CODEC.decode(buf);
                case 3 -> Stop.PACKET_CODEC.decode(buf);
                case 4 -> Jump.PACKET_CODEC.decode(buf);
                default -> throw new IllegalArgumentException("Unknown movement type ID: " + id);
            };
        }

        @Override
        public void encode(ByteBuf buf, MovementInstruction value) {
            if (value instanceof Walk) {
                buf.writeByte(0);
                Walk.PACKET_CODEC.encode(buf, value);
            } else if (value instanceof Swim) {
                buf.writeByte(1);
                Swim.PACKET_CODEC.encode(buf, value);
            } else if (value instanceof Turn) {
                buf.writeByte(2);
                Turn.PACKET_CODEC.encode(buf, value);
            } else if (value instanceof Stop) {
                buf.writeByte(3);
                Stop.PACKET_CODEC.encode(buf, value);
            } else if (value instanceof Jump) {
                buf.writeByte(4);
                Jump.PACKET_CODEC.encode(buf, value);
            }
        }
    };
}