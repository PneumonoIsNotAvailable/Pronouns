package net.pneumono.pronouns.pronouns;

/**
 * This class mostly just provides a few common pronoun sets so nobody has to bother making them manually.
 */
@SuppressWarnings("unused")
public class Presets {
    public static PronounSet getHeHimSet() {
        return new PronounSet(true, "he", "him", "his", "his", "himself");
    }

    public static PronounSet getSheHerSet() {
        return new PronounSet(true, "she", "her", "her", "hers", "herself");
    }

    public static PronounSet getTheyThemSet() {
        return new PronounSet(false, "they", "them", "their", "theirs", "themselves");
    }

    public static PronounSet getItItsSet() {
        return new PronounSet(true, "it", "it", "its", "its", "itself");
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

    /**
     * Only includes he/him, she/her, and they/them.
     */
    public static PlayerPronouns getAnyPronouns() {
        return new PlayerPronouns(new PronounSet[]{getHeHimSet(), getSheHerSet(), getTheyThemSet()}, "any");
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
                getItItsPronouns(),
                getAnyPronouns()
        };
    }
}
