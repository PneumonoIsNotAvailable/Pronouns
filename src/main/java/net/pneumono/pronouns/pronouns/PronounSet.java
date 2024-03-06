package net.pneumono.pronouns.pronouns;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.math.MathHelper;

public class PronounSet {
    private int weight;
    private boolean singular;
    private String subjective;
    private String objective;
    private String possessiveDeterminer;
    private String possessivePronoun;
    private String reflexive;

    public PronounSet(int weight, boolean singular, String subjective, String objective, String possessiveDeterminer, String possessivePronoun, String reflexive) {
        this.weight = MathHelper.clamp(weight, 1, 10);
        this.singular = singular;
        this.subjective = subjective;
        this.objective = objective;
        this.possessiveDeterminer = possessiveDeterminer;
        this.possessivePronoun = possessivePronoun;
        this.reflexive = reflexive;
    }

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
            weight = primitive.getAsInt();
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

        return new PronounSet(weight, singular, subjective, objective, possessiveDeterminer, possessivePronoun, reflexive);
    }

    public JsonObject toJson() {
        JsonObject setObject = new JsonObject();

        setObject.add("weight", new JsonPrimitive(weight));
        setObject.add("subjective", new JsonPrimitive(subjective));
        setObject.add("objective", new JsonPrimitive(objective));
        setObject.add("possessive_determiner", new JsonPrimitive(possessiveDeterminer));
        setObject.add("possessive_pronoun", new JsonPrimitive(possessivePronoun));
        setObject.add("reflexive", new JsonPrimitive(reflexive));
        setObject.add("singular", new JsonPrimitive(singular));

        return setObject;
    }

    public static JsonArray toJson(PronounSet[] pronouns) {
        JsonArray array = new JsonArray();

        for (PronounSet set : pronouns) {
            array.add(set.toJson());
        }

        return array;
    }

    public int getWeight() {
        return this.weight;
    }

    public boolean isSingular() {
        return this.singular;
    }

    public String getSubjective() {
        return this.subjective;
    }

    public String getObjective() {
        return this.objective;
    }

    public String getPossessiveDeterminer() {
        return this.possessiveDeterminer;
    }

    public String getPossessivePronoun() {
        return this.possessivePronoun;
    }

    public String getReflexive() {
        return this.reflexive;
    }

    public PronounSet setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public PronounSet setSingular(boolean singular) {
        this.singular = singular;
        return this;
    }

    public PronounSet setSubjective(String subjective) {
        this.subjective = subjective;
        return this;
    }

    public PronounSet setObjective(String objective) {
        this.objective = objective;
        return this;
    }

    public PronounSet setPossessiveDeterminer(String possessiveDeterminer) {
        this.possessiveDeterminer = possessiveDeterminer;
        return this;
    }

    public PronounSet setPossessivePronoun(String possessivePronoun) {
        this.possessivePronoun = possessivePronoun;
        return this;
    }

    public PronounSet setReflexive(String reflexive) {
        this.reflexive = reflexive;
        return this;
    }
}
