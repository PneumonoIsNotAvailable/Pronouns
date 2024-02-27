package net.pneumono.pronouns.pronouns;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Language;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslatablePronounsTextContent implements TextContent {
    private static final StringVisitable LITERAL_PERCENT_SIGN = StringVisitable.plain("%");
    private static final StringVisitable NULL_ARGUMENT = StringVisitable.plain("null");
    private final String key;
    @Nullable
    private final String fallback;
    private final Object[] args;
    private final PronounSet pronouns;
    @Nullable
    private Language languageCache;
    private List<StringVisitable> translations = ImmutableList.of();
    private static final Pattern ARG_FORMAT = Pattern.compile("%(?:(\\d+|[sokprSOKPR])\\$)?([A-Za-z%]|$)");

    public TranslatablePronounsTextContent(String key, @Nullable String fallback, PronounSet pronouns, Object[] args) {
        this.key = key;
        this.fallback = fallback;
        this.args = args;
        this.pronouns = pronouns;
    }

    private void updateTranslations() {
        Language language = Language.getInstance();
        if (language == this.languageCache) {
            return;
        }
        this.languageCache = language;
        String string = this.fallback != null ? language.get(this.key, this.fallback) : language.get(this.key);
        try {
            ImmutableList.Builder<StringVisitable> builder = ImmutableList.builder();
            this.forEachPart(string, builder::add);
            this.translations = builder.build();
        } catch (TranslationException translationException) {
            this.translations = ImmutableList.of(StringVisitable.plain(string));
        }
    }

    private void forEachPart(String translation, Consumer<StringVisitable> partsConsumer) {
        Matcher matcher = ARG_FORMAT.matcher(translation);
        try {
            int argCounter = 0;
            int previousEnd = 0;
            while (matcher.find(previousEnd)) {
                String string;
                int start = matcher.start();
                int end = matcher.end();
                if (start > previousEnd) {
                    string = translation.substring(previousEnd, start);
                    if (string.indexOf(37) != -1) {
                        throw new IllegalArgumentException();
                    }
                    partsConsumer.accept(StringVisitable.plain(string));
                }
                String index = matcher.group(1);
                String type = matcher.group(2);
                String match = translation.substring(start, end);
                if ("%".equals(type) && "%%".equals(match)) {
                    partsConsumer.accept(LITERAL_PERCENT_SIGN);
                } else if ("s".equals(type)) {
                    int argIndex = index != null ? Integer.parseInt(index) - 1 : argCounter++;
                    partsConsumer.accept(this.getArg(argIndex));
                } else if ("p".equals(type)) {
                    partsConsumer.accept(getPronoun(index, false));
                } else if ("P".equals(type)) {
                    partsConsumer.accept(getPronoun(index, true));
                } else {
                    throw new PronounTranslationException(this, "Unsupported format: '" + match + "'");
                }
                previousEnd = end;
            }
            if (previousEnd < translation.length()) {
                String end = translation.substring(previousEnd);
                if (end.indexOf(37) != -1) {
                    throw new IllegalArgumentException();
                }
                partsConsumer.accept(StringVisitable.plain(end));
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new PronounTranslationException(this, illegalArgumentException);
        }
    }

    public final StringVisitable getPronoun(String pronounType, boolean uppercase) {
        String string = switch (pronounType) {
            case "s" -> pronouns.subjective();
            case "o" -> pronouns.objective();
            case "k" -> pronouns.possessiveDeterminer();
            case "p" -> pronouns.possessivePronoun();
            case "r" -> pronouns.reflexive();
            case "S" -> capitalizeFirstCharacter(pronouns.subjective());
            case "O" -> capitalizeFirstCharacter(pronouns.objective());
            case "K" -> capitalizeFirstCharacter(pronouns.possessiveDeterminer());
            case "P" -> capitalizeFirstCharacter(pronouns.possessivePronoun());
            case "R" -> capitalizeFirstCharacter(pronouns.reflexive());
            default -> throw new PronounTranslationException(this, "Unsupported pronoun type: '" + pronounType + "'");
        };
        if (uppercase) {
            string = string.toUpperCase();
        }
        return StringVisitable.plain(string);
    }

    private static String capitalizeFirstCharacter(String string) {
        char[] charArray = string.toCharArray();
        if (charArray.length > 0) {
            charArray[0] = Character.toUpperCase(charArray[0]);
        }
        return String.valueOf(charArray);
    }

    public final StringVisitable getArg(int index) {
        if (index < 0 || index >= this.args.length) {
            throw new PronounTranslationException(this, index);
        }
        Object object = this.args[index];
        if (object instanceof Text) {
            return (Text)object;
        }
        return object == null ? NULL_ARGUMENT : StringVisitable.plain(object.toString());
    }

    @Override
    public <T> Optional<T> visit(StringVisitable.StyledVisitor<T> visitor, Style style) {
        this.updateTranslations();
        for (StringVisitable stringVisitable : this.translations) {
            Optional<T> optional = stringVisitable.visit(visitor, style);
            if (optional.isEmpty()) continue;
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> visit(StringVisitable.Visitor<T> visitor) {
        this.updateTranslations();
        for (StringVisitable stringVisitable : this.translations) {
            Optional<T> optional = stringVisitable.visit(visitor);
            if (optional.isEmpty()) continue;
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public MutableText parse(@Nullable ServerCommandSource source, @Nullable Entity sender, int depth) throws CommandSyntaxException {
        Object[] objects = new Object[this.args.length];
        for (int i = 0; i < objects.length; ++i) {
            Object object = this.args[i];
            objects[i] = object instanceof Text ? Texts.parse(source, (Text)object, sender, depth) : object;
        }
        return MutableText.of(new TranslatableTextContent(this.key, this.fallback, objects));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof TranslatablePronounsTextContent textContent && Objects.equals(this.key, textContent.key) && Objects.equals(this.fallback, textContent.fallback) && Arrays.equals(this.args, textContent.args);
    }

    public int hashCode() {
        int i = Objects.hashCode(this.key);
        i = 31 * i + Objects.hashCode(this.fallback);
        i = 31 * i + Arrays.hashCode(this.args);
        return i;
    }

    public String toString() {
        return "translation{key='" + this.key + "'" + (this.fallback != null ? ", fallback='" + this.fallback + "'" : "") + ", args=" + Arrays.toString(this.args) + "}";
    }

    @SuppressWarnings("unused")
    public String getKey() {
        return this.key;
    }

    @Nullable
    @SuppressWarnings("unused")
    public String getFallback() {
        return this.fallback;
    }

    @SuppressWarnings("unused")
    public Object[] getArgs() {
        return this.args;
    }
}
