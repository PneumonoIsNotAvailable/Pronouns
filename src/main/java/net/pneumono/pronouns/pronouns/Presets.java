package net.pneumono.pronouns.pronouns;

@SuppressWarnings("unused")
public class Presets {
    public static PronounSet getHeHimSet() {
        return new PronounSet(1, "he", "him", "his", "his", "himself", true);
    }

    public static PronounSet getSheHerSet() {
        return new PronounSet(1, "she", "her", "her", "hers", "herself", true);
    }

    public static PronounSet getTheyThemSet() {
        return new PronounSet(1, "they", "them", "their", "theirs", "themselves", false);
    }

    public static PronounSet getItItsSet() {
        return new PronounSet(1, "it", "it", "its", "its", "itself", true);
    }

    public static PronounSet[] getAllSets() {
        return new PronounSet[]{
                getHeHimSet(),
                getSheHerSet(),
                getTheyThemSet(),
                getItItsSet()
        };
    }

    public static PlayerPronouns getHeHimPronouns() {
        return new PlayerPronouns(new PronounSet[]{getHeHimSet()}, "he/him");
    }

    public static PlayerPronouns getHeTheyPronouns() {
        return new PlayerPronouns(new PronounSet[]{getHeHimSet(), getTheyThemSet()}, "he/they");
    }

    public static PlayerPronouns getHeItPronouns() {
        return new PlayerPronouns(new PronounSet[]{getHeHimSet(), getItItsSet()}, "he/it");
    }

    public static PlayerPronouns getSheHerPronouns() {
        return new PlayerPronouns(new PronounSet[]{getSheHerSet()}, "she/her");
    }

    public static PlayerPronouns getSheTheyPronouns() {
        return new PlayerPronouns(new PronounSet[]{getSheHerSet(), getTheyThemSet()}, "she/they");
    }

    public static PlayerPronouns getSheItPronouns() {
        return new PlayerPronouns(new PronounSet[]{getSheHerSet(), getItItsSet()}, "she/it");
    }

    public static PlayerPronouns getTheyThemPronouns() {
        return new PlayerPronouns(new PronounSet[]{getTheyThemSet()}, "they/them");
    }

    public static PlayerPronouns getTheyItPronouns() {
        return new PlayerPronouns(new PronounSet[]{getTheyThemSet(), getItItsSet()}, "they/it");
    }

    public static PlayerPronouns getItItsPronouns() {
        return new PlayerPronouns(new PronounSet[]{getItItsSet()}, "it/its");
    }

    public static PlayerPronouns[] getAllPronouns() {
        return new PlayerPronouns[]{
                getHeHimPronouns(),
                getHeTheyPronouns(),
                getHeItPronouns(),
                getSheHerPronouns(),
                getSheTheyPronouns(),
                getSheItPronouns(),
                getTheyThemPronouns(),
                getTheyItPronouns(),
                getItItsPronouns()
        };
    }
}