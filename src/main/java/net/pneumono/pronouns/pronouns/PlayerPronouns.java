package net.pneumono.pronouns.pronouns;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

public record PlayerPronouns(PronounSet[] pronounSets, String abbreviation) {
    public static PlayerPronouns fromJson(JsonObject json) {
        if (json != null) {
            List<PronounSet> setsList = new ArrayList<>();

            JsonArray setsJson = json.getAsJsonArray("sets");
            if (setsJson != null) {
                for (JsonElement setJson : setsJson) {
                    setsList.add(PronounSet.fromJson(setJson.getAsJsonObject()));
                }
            }

            String abbreviation = null;
            JsonPrimitive primitive = json.getAsJsonPrimitive("abbreviation");
            if (primitive != null) {
                abbreviation = primitive.getAsString();
            }

            if (setsList.size() > 0 && abbreviation != null) {
                return new PlayerPronouns(setsList.toArray(new PronounSet[0]), abbreviation);
            }
        }
        return null;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.add("sets", PronounSet.toJson(pronounSets()));
        json.addProperty("abbreviation", abbreviation());
        return json;
    }
}
