package fr.neutronstars.room.royale.core.history;

import com.google.gson.*;
import fr.neutronstars.room.royale.core.game.entity.journal.LiteralParameter;
import fr.neutronstars.room.royale.core.game.entity.journal.Parameter;

import java.lang.reflect.Type;

public class ParameterAdapter implements JsonSerializer<Parameter>, JsonDeserializer<Parameter> {
    @Override
    public JsonElement serialize(Parameter src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        if (src instanceof LiteralParameter) {
            jsonObject.addProperty("paramType", "literal");
        }

        jsonObject.add("data", context.serialize(src));
        return jsonObject;
    }

    @Override
    public Parameter deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("paramType").getAsString();
        JsonElement data = jsonObject.get("data");

        if ("literal".equals(type)) {
            return context.deserialize(data, LiteralParameter.class);
        }

        throw new JsonParseException("unknown parameter type : " + type);
    }
}
