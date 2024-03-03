package net.pneumono.pronouns.pronouns;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.math.MathHelper;

public record PronounSet(int weight, String subjective, String objective, String possessiveDeterminer, String possessivePronoun, String reflexive, boolean singular) {
    public static PronounSet fromJson(JsonObject json) {
        JsonPrimitive primitive;
        int weight = -1;
        String subjective = "";
        String objective = "";
        String possessiveDeterminer = "";
        String possessivePronoun = "";
        String reflexive = "";
        boolean singular = true;

        primitive = json.getAsJsonPrimitive("weight");
        if (primitive != null) {
            weight = MathHelper.clamp(primitive.getAsInt(), 1, 10);
        }

        primitive = json.getAsJsonPrimitive("subjective");
        if (primitive != null) {
            subjective = primitive.getAsString();
        }

        primitive = json.getAsJsonPrimitive("objective");
        if (primitive != null) {
            objective = primitive.getAsString();
        }

        primitive = json.getAsJsonPrimitive("possessive_determiner");
        if (primitive != null) {
            possessiveDeterminer = primitive.getAsString();
        }

        primitive = json.getAsJsonPrimitive("possessive_pronoun");
        if (primitive != null) {
            possessivePronoun = primitive.getAsString();
        }

        primitive = json.getAsJsonPrimitive("reflexive");
        if (primitive != null) {
            reflexive = primitive.getAsString();
        }

        primitive = json.getAsJsonPrimitive("singular");
        if (primitive != null) {
            singular = primitive.getAsBoolean();
        }

        return new PronounSet(weight, subjective, objective, possessiveDeterminer, possessivePronoun, reflexive, singular);
    }

    public JsonObject toJson() {
        JsonObject setObject = new JsonObject();

        setObject.add("weight", new JsonPrimitive(MathHelper.clamp(weight, 1, 10)));
        setObject.add("subjective", new JsonPrimitive(subjective()));
        setObject.add("objective", new JsonPrimitive(objective()));
        setObject.add("possessive_determiner", new JsonPrimitive(possessiveDeterminer()));
        setObject.add("possessive_pronoun", new JsonPrimitive(possessivePronoun()));
        setObject.add("reflexive", new JsonPrimitive(reflexive()));
        setObject.add("singular", new JsonPrimitive(singular()));

        return setObject;
    }

    public static JsonArray toJson(PronounSet[] pronouns) {
        JsonArray array = new JsonArray();

        for (PronounSet set : pronouns) {
            array.add(set.toJson());
        }

        return array;
    }
}
