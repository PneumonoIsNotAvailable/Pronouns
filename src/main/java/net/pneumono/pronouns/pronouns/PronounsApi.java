package net.pneumono.pronouns.pronouns;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.pneumono.pronouns.Pronouns;

import java.util.*;

@SuppressWarnings("unused")
public class PronounsApi {
    /**
     * The default pronoun set. Literally just they/them.
     */
    public static PronounSet getDefaultPronounSet() {
        return Presets.getTheyThemSet();
    }

    /**
     * The default player pronouns. Literally just they/them.
     */
    public static PlayerPronouns getDefaultPlayerPronouns() {
        return Presets.getTheyThemPronouns();
    }

    /**
     * Sends a packet to all players in the list to communicate a change in the server-side pronoun map. It is recommended that the packet is simply sent to all players.
     *
     * @param players The players the packet will be sent to.
     * @param pronouns The pronouns and their respective player UUIDs to be sent.
     */
    public static void sendDistributePronounsPacket(List<ServerPlayerEntity> players, Map<UUID, PlayerPronouns> pronouns) {
        PacketByteBuf buf = PacketByteBufs.create();

        JsonArray array = new JsonArray();
        for (Map.Entry<UUID, PlayerPronouns> entry : pronouns.entrySet()) {
            JsonObject object = new JsonObject();
            object.add("uuid", new JsonPrimitive(entry.getKey().toString()));

            PlayerPronouns playerPronouns = entry.getValue();
            object.add("pronouns", playerPronouns != null ? playerPronouns.toJson() : new JsonObject());
            array.add(object);
        }

        buf.writeString(array.toString());
        for (ServerPlayerEntity player : players) {
            ServerPlayNetworking.send(player, Pronouns.DISTRIBUTE_PRONOUNS_ID, buf);
        }
    }

    /**
     * Returns the pronouns for the player in question, according to the pronoun map. If the player has not set their pronouns, returns {@link PronounsApi#getDefaultPlayerPronouns()} (they/them) instead.
     *
     * @param player The player whose pronouns are needed.
     * @return The player's pronouns.
     */
    public static PlayerPronouns getPlayerPronouns(PlayerEntity player) {
        return getPlayerPronouns(player.getUuid());
    }

    /**
     * Returns the pronouns for the player UUID in question, according to the pronoun map. If the player has not set their pronouns, returns {@link PronounsApi#getDefaultPlayerPronouns()} (they/them) instead.
     *
     * @param uuid The UUID of the player whose pronouns are needed.
     * @return The player's pronouns.
     */
    public static PlayerPronouns getPlayerPronouns(UUID uuid) {
        PlayerPronouns pronouns = Pronouns.uuidPronounsMap.get(uuid);
        return pronouns != null ? pronouns : getDefaultPlayerPronouns();
    }

    /**
     * Returns the pronouns for the player UUID in question, according to the pronoun map. If the player has not set their pronouns, will return null instead of {@link PronounsApi#getDefaultPlayerPronouns()} (they/them).<p>
     * This should be used in situations where it is important to know whether the player has actually set their pronouns, in which {@code null} will represent this.
     *
     * @param uuid The UUID of the player whose pronouns are needed.
     * @return The player's pronouns. May be null.
     */
    public static PlayerPronouns getPlayerPronounsNullable(UUID uuid) {
        return Pronouns.uuidPronounsMap.get(uuid);
    }

    /**
     * Returns a {@link Text} instance with pronouns as additional arguments. Similar to {@link Text#translatable(String, Object[])}, but can also include pronouns.<p>
     * This method selects a random pronoun set from the provided player pronouns and then returns text from {@link PronounsApi#getTranslatableTextWithPronouns(String, PronounSet, Object...)} using that set.<p>
     * When creating translations involving these pronouns, use:
     * <ul>
     * <li>{@code %s$p} for subjective pronouns (he, she, they)
     * <li>{@code %o$p} for objective pronouns (him, her, them)
     * <li>{@code %k$p} for possessive determiners (his, her, their)
     * <li>{@code %p$p} for possessive pronouns (his, hers, theirs)
     * <li>{@code %r$p} for reflexive pronouns (himself, herself, themselves)
     * </ul>
     * For situations where the first character must be capitalized (e.g. the start of a sentence), the second character can be capitalized. For example, using {@code %S$p} would turn {@code he} into {@code He}.
     * For situations where the entire pronoun must be capitalized (e.g. to indicate shouting), the fourth character can be capitalized. For example, using {@code %s$P} would turn {@code she} into {@code SHE}.
     * Using both simultaneously (e.g. {@code %S$P}) will not cause errors, but is redundant and equivalent to {@code %s$P}.<p>
     * Other arguments are used the same as normal; {@code %s} and {@code %x$s} (where x is any integer) are for any other arguments entered through the {@code args} parameter, in order of entry.<p>
     * Translations must also have two variants, singular and plural, for different types of pronouns. Examples of singular pronouns are he/him or she/her, and the most common plural pronoun is they/them.
     * These are defined with {@code your_translation_key_here.singular} and {@code your_translation_key_here.plural} in your lang file.<p>
     * An example of a singular translation could be "%s$p loses %k$p braincells when %s$p tries to understand this. %s$p is not okay!"<p>
     * An example of a plural translation could be "%s$p lose %k$p braincells when %s$p try to understand this. %s$p are not okay!"<p>
     * If the provided pronouns are null, {@link PronounsApi#getDefaultPlayerPronouns()} will be used (they/them).
     *
     * @param key The translation key of the text.
     * @param pronouns The pronouns used in the text.
     * @param args Additional arguments for the text.
     * @return A translatable text object with pronouns as additional arguments.
     */
    public static MutableText getTranslatableTextWithPronouns(String key, PlayerPronouns pronouns, Object... args) {
        PronounSet set = getRandomSet(pronouns);
        return getTranslatableTextWithPronouns(key, set, args);
    }

    /**
     * Returns a {@link Text} instance with pronouns as additional arguments. Similar to {@link Text#translatable(String, Object[])}, but can also include pronouns.<p>
     * When creating translations involving these pronouns, use:
     * <ul>
     * <li>{@code %s$p} for subjective pronouns (he, she, they)
     * <li>{@code %o$p} for objective pronouns (him, her, them)
     * <li>{@code %k$p} for possessive determiners (his, her, their)
     * <li>{@code %p$p} for possessive pronouns (his, hers, theirs)
     * <li>{@code %r$p} for reflexive pronouns (himself, herself, themselves)
     * </ul>
     * For situations where the first character must be capitalized (e.g. the start of a sentence), the second character can be capitalized. For example, using {@code %S$p} would turn {@code he} into {@code He}.
     * For situations where the entire pronoun must be capitalized (e.g. to indicate shouting), the fourth character can be capitalized. For example, using {@code %s$P} would turn {@code she} into {@code SHE}.
     * Using both simultaneously (e.g. {@code %S$P}) will not cause errors, but is redundant and equivalent to {@code %s$P}.<p>
     * Other arguments are used the same as normal; {@code %s} and {@code %x$s} (where x is any integer) are for any other arguments entered through the {@code args} parameter, in order of entry.<p>
     * Translations must also have two variants, singular and plural, for different types of pronouns. Examples of singular pronouns are he/him or she/her, and the most common plural pronoun is they/them.
     * These are defined with {@code your_translation_key_here.singular} and {@code your_translation_key_here.plural} in your lang file.<p>
     * An example of a singular translation could be "%s$p loses %k$p braincells when %s$p tries to understand this. %s$p is not okay!"<p>
     * An example of a plural translation could be "%s$p lose %k$p braincells when %s$p try to understand this. %s$p are not okay!"<p>
     * If the provided pronouns are null, {@link PronounsApi#getDefaultPlayerPronouns()} will be used (they/them).
     *
     * @param key The translation key of the text.
     * @param pronouns The pronouns used in the text.
     * @param args Additional arguments for the text.
     * @return A translatable text object with pronouns as additional arguments.
     */
    public static MutableText getTranslatableTextWithPronouns(String key, PronounSet pronouns, Object... args) {
        String translationKey = key + (pronouns.isSingular() ? ".singular" : ".plural");
        return MutableText.of(new TranslatablePronounsTextContent(translationKey, null, pronouns, args));
    }

    /**
     * Returns a random pronoun set from a player's pronouns.<p>
     * If the provided pronouns are null, {@link PronounsApi#getDefaultPlayerPronouns()} will be used (they/them).
     *
     * @param pronouns The player pronouns one pronoun set will be picked from.
     * @return The set picked.
     */
    public static PronounSet getRandomSet(PlayerPronouns pronouns) {
        if (pronouns == null) {
            pronouns = getDefaultPlayerPronouns();
        }
        List<PronounSet> list = new ArrayList<>(Arrays.asList(pronouns.getPronounSets()));
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
