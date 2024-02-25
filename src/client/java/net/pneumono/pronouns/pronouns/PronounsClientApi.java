package net.pneumono.pronouns.pronouns;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.pneumono.pneumonocore.config.Configs;
import net.pneumono.pronouns.Pronouns;
import net.pneumono.pronouns.PronounsClient;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

public class PronounsClientApi {
    public static final File PRONOUNS_FILE = new File(FabricLoader.getInstance().getGameDir().toFile(), "pronouns.json");

    /**
     * Sends a packet to the server to communicate the client's pronouns.
     *
     * @param pronouns The pronouns to be sent.
     */
    public static void sendInformPronounsPacket(PlayerPronouns pronouns) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(pronouns.toJson().toString());
        ClientPlayNetworking.send(Pronouns.INFORM_PRONOUNS_ID, buf);
    }

    /**
     * Saves a set of player pronouns to pronouns.json.
     *
     * @param pronouns The pronouns to be saved.
     */
    @SuppressWarnings("unused")
    public static void writePronouns(PlayerPronouns pronouns) {
        try {
            Writer writer = Files.newBufferedWriter(PRONOUNS_FILE.toPath());
            (new GsonBuilder().setPrettyPrinting().create()).toJson(pronouns.toJson(), writer);
            writer.close();
        } catch (IOException e) {
            Configs.LOGGER.error("Could not write pronouns.json!", e);
        }
    }

    /**
     * Returns pronouns by reading pronouns.json from the game directory. Calling this method also updates saved pronouns client-side.
     *
     * @return The player pronouns read from pronouns.json.
     */
    @SuppressWarnings("UnusedReturnValue")
    public static PlayerPronouns readPronouns() {
        JsonObject jsonObject = null;
        try {
            Reader reader = Files.newBufferedReader(PRONOUNS_FILE.toPath());
            jsonObject = new GsonBuilder().setPrettyPrinting().create().fromJson(reader, JsonObject.class);
            reader.close();
        } catch (IOException e) {
            Configs.LOGGER.warn("Could not read pronouns.json!", e);
        }

        PlayerPronouns pronouns = PlayerPronouns.fromJson(jsonObject);
        PronounsClient.loadedPronouns = pronouns;
        return pronouns;
    }
}
