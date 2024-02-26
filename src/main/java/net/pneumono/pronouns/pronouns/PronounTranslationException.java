package net.pneumono.pronouns.pronouns;

import java.util.Locale;

public class PronounTranslationException extends IllegalArgumentException {
    public PronounTranslationException(TranslatablePronounsTextContent text, String message) {
        super(String.format(Locale.ROOT, "Error parsing: %s: %s", text, message));
    }

    public PronounTranslationException(TranslatablePronounsTextContent text, int index) {
        super(String.format(Locale.ROOT, "Invalid index %d requested for %s", index, text));
    }

    public PronounTranslationException(TranslatablePronounsTextContent text, Throwable cause) {
        super(String.format(Locale.ROOT, "Error while parsing: %s", text), cause);
    }
}
