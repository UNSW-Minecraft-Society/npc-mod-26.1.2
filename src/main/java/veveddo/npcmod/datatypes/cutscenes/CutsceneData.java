package veveddo.npcmod.datatypes.cutscenes;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public record CutsceneData(List<CutsceneInstruction> actions) {
    private static final String ACTIONS_KEY = "action_list";

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        JsonArray action_json = new JsonArray();
        actions.forEach(action -> action_json.add(action.toJson()));
        json.add(ACTIONS_KEY, action_json);
        return json;
    }

    public static CutsceneData fromJson(JsonObject json) {
        List<CutsceneInstruction> actions = json.get(ACTIONS_KEY).getAsJsonArray().asList().stream()
                .map(JsonElement::getAsJsonObject)
                .map(CutsceneInstruction::fromJson)
                .toList();
        return new CutsceneData(actions);
    }


    public static class JsonSerialiser implements JsonSerializer<CutsceneData>, JsonDeserializer<CutsceneData>{
        @Override
        public JsonElement serialize(CutsceneData src, Type typeOfSrc, JsonSerializationContext context) {
            return src.toJson();
        }

        @Override
        public CutsceneData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return CutsceneData.fromJson(json.getAsJsonObject());
        }
    }
}
