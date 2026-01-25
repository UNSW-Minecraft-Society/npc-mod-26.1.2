package mcsoc.npcmod.datatypes.npcs;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import mcsoc.npcmod.datatypes.PositionData;
import mcsoc.npcmod.util.Instruction;


public sealed interface MovementInstruction extends Instruction {
    
    static final String TYPE_KEY = "type";
    static final String ERROR_TYPE_NAME = "parse_error";
    static final String WALK_TYPE_NAME = "walk";
    static final String SWIM_TYPE_NAME = "swim";
    static final String TURN_TYPE_NAME = "turn";
    static final String STOP_TYPE_NAME = "stop";
    static final String JUMP_TYPE_NAME = "jump";

    static final String TICKS_PROPERTY_KEY = "ticks";
    static final String SPEED_PROPERTY_KEY = "speed";
    static final String POSITION_PROPERTY_KEY = "position";
    static final String PROPERTIES_KEY = "properties";

    record ParseError() implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, ERROR_TYPE_NAME);
            JsonObject properties_json = new JsonObject();
            json.add(PROPERTIES_KEY, properties_json);

            return json;
        }
    }
    record Walk(PositionData pos, double speed) implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, WALK_TYPE_NAME);

            JsonObject properties = new JsonObject();
            properties.add(POSITION_PROPERTY_KEY, this.pos.toJson());
            properties.addProperty(SPEED_PROPERTY_KEY, this.speed);
            json.add(PROPERTIES_KEY, properties);

            return json;
        }
    }
    record Swim(PositionData pos, double speed) implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, SWIM_TYPE_NAME);

            JsonObject properties = new JsonObject();
            properties.add(POSITION_PROPERTY_KEY, this.pos.toJson());
            properties.addProperty(SPEED_PROPERTY_KEY, this.speed);
            json.add(PROPERTIES_KEY, properties);
            
            return json;
        }
    }
    record Turn(int ticks, float target_degrees) implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, TURN_TYPE_NAME);

            JsonObject properties = new JsonObject();
            properties.addProperty(TICKS_PROPERTY_KEY, this.ticks);
            properties.addProperty("degrees", this.target_degrees);
            json.add(PROPERTIES_KEY, properties);
            
            return json;
        }
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
    }
    record Jump() implements MovementInstruction {
        @Override
        public JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty(TYPE_KEY, JUMP_TYPE_NAME);
            json.add(PROPERTIES_KEY, new JsonObject());
            return json;
        }
    }

    public abstract JsonObject toJson();

    public static MovementInstruction fromJson(JsonObject json) throws JsonParseException {
        String type_name = json.get(TYPE_KEY).getAsString();
        JsonObject properties = json.get(PROPERTIES_KEY).getAsJsonObject();
        try {
            return (
                switch (type_name) {
                    case WALK_TYPE_NAME -> new MovementInstruction.Walk(
                        PositionData.fromJson(properties.get(POSITION_PROPERTY_KEY).getAsJsonObject()), 
                        properties.get(SPEED_PROPERTY_KEY).getAsDouble()
                    );
                    case SWIM_TYPE_NAME -> new MovementInstruction.Swim(
                        PositionData.fromJson(properties.get(POSITION_PROPERTY_KEY).getAsJsonObject()), 
                        properties.get(SPEED_PROPERTY_KEY).getAsDouble()
                    );
                    case TURN_TYPE_NAME -> new MovementInstruction.Turn(
                        properties.get(TICKS_PROPERTY_KEY).getAsInt(), 
                        properties.get("degrees").getAsFloat()
                    );
                    case STOP_TYPE_NAME -> new MovementInstruction.Stop(
                        properties.get(TICKS_PROPERTY_KEY).getAsInt()
                    );
                    case JUMP_TYPE_NAME -> new MovementInstruction.Jump();
                    case ERROR_TYPE_NAME -> new MovementInstruction.ParseError();
                    default -> throw new JsonParseException(String.format("\"%s\" is not valid for MovementInstruction type name", type_name ));
                }
            );
        } catch (Exception e) {
            return new MovementInstruction.ParseError();
        }
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
}