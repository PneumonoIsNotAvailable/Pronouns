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

public class PronounsApi {
    /**
     * Sends a packet to all clients to communicate a change in the pronoun list.
     *
     * @param players The players the packet will be sent to. It is recommended that this is simply everyone.
     * @param pronouns The pronouns and their respective player UUIDs to be sent.
     */
    public static void sendDistributePronounsPacket(List<ServerPlayerEntity> players, Map<UUID, PlayerPronouns> pronouns) {
        PacketByteBuf buf = PacketByteBufs.create();

        JsonArray array = new JsonArray();
        for (Map.Entry<UUID, PlayerPronouns> entry : pronouns.entrySet()) {
            JsonObject object = new JsonObject();
            object.add("uuid", new JsonPrimitive(entry.getKey().toString()));
            object.add("pronouns", entry.getValue().toJson());
            array.add(object);
        }

        buf.writeString(array.toString());
        for (ServerPlayerEntity player : players) {
            ServerPlayNetworking.send(player, Pronouns.INFORM_PRONOUNS_ID, buf);
        }
    }

    /**
     * Returns the pronouns for the player in question, according to the pronoun list.
     *
     * @param player The player whose pronouns are needed.
     * @return The player's pronouns.
     */
    public static PlayerPronouns getPlayerPronouns(PlayerEntity player) {
        return getPlayerPronouns(player.getUuid());
    }

    /**
     * Returns the pronouns for the player UUID in question, according to the pronoun list.
     *
     * @param uuid The UUID of the player whose pronouns are needed.
     * @return The player's pronouns.
     */
    public static PlayerPronouns getPlayerPronouns(UUID uuid) {
        return Pronouns.uuidPronounsMap.get(uuid);
    }

    /**
     * Returns a {@link Text} instance with pronouns as additional arguments. Equivalent to {@link Text#translatable(String, Object[])}, but with relevant pronouns added to the start of the args array.<p>
     * When creating translations involving these pronouns, use:
     * <ul>
     * <li>'%1$s' for subjective pronouns (he, she, they)
     * <li>'%2$s' for objective pronouns (him, her, them)
     * <li>'%3$s' for possessive determiners (his, her, their)
     * <li>'%4$s' for possessive pronouns (his, hers, theirs)
     * <li>'%5$s' for reflexive pronouns (himself, herself, themselves)
     * </ul>
     * '%6$s' and onwards are for any other arguments entered through the {@code args} parameter, in order of entry.<p>
     * Translations must also have two variants, singular and plural, for different types of pronouns. Examples of singular pronouns are he/him or she/her, and the most common plural pronoun is they/them. These are defined with {@code your_translation_key_here.singular} and {@code your_translation_key_here.plural} in your lang file.<p>
     * An example of a singular translation could be "%1$s loses %3$s braincells when %1$s tries to understand this. %1$s is not okay!"<p>
     * An example of a plural translation could be "%1$s lose %3$s braincells when %1$s try to understand this. %1$s are not okay!"
     *
     * @param key The translation key of the text.
     * @param pronouns The pronouns used in the text.
     * @param args Additional arguments for the text.
     * @return A translatable text object with pronouns as additional arguments.
     */
    public static MutableText getTranslatableTextWithPronouns(String key, PlayerPronouns pronouns, Object... args) {
        PronounSet set = getRandomWeightedSet(pronouns);
        List<Object> newArgs = new ArrayList<>(List.of(set.subjective(), set.objective(), set.possessiveDeterminer(), set.possessivePronoun(), set.reflexive()));
        newArgs.addAll(Arrays.asList(args));

        return Text.translatable(key + (set.singular() ? ".singular" : ".plural"), newArgs.toArray());
    }

    /**
     * Returns a random pronoun set from a player's pronouns based on the weight of the set.
     *
     * @param pronouns The player pronouns one pronoun set will be picked from.
     * @return The set picked.
     */
    public static PronounSet getRandomWeightedSet(PlayerPronouns pronouns) {
        List<PronounSet> list = new ArrayList<>();
        for (PronounSet set : pronouns.pronounSets()) {
            for (int i = 0; i < set.weight(); ++i) {
                list.add(set);
            }
        }
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
