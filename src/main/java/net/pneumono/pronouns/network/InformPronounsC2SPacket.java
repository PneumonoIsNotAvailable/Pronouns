package net.pneumono.pronouns.network;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.pneumono.pronouns.Pronouns;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounsApi;

public class InformPronounsC2SPacket {
    @SuppressWarnings("unused")
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        PlayerPronouns pronouns = null;
        try {
            String string = buf.readString();
            pronouns = string != null && !string.equals("null") ? PlayerPronouns.fromJson(JsonParser.parseString(string).getAsJsonObject()) : null;
        } catch (JsonSyntaxException | IllegalStateException | IndexOutOfBoundsException e) {
            Pronouns.LOGGER.warn("Recieved invalid pronoun packet from player " + player.getUuid() + " (" + player.getName().getString() + ")");
        }

        Pronouns.uuidPronounsMap.put(player.getUuid(), pronouns);
        PronounsApi.sendDistributePronounsPacket(server.getPlayerManager().getPlayerList(), Pronouns.uuidPronounsMap);
    }
}
