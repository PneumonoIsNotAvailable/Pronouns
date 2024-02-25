package net.pneumono.pronouns.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.pneumono.pronouns.PronounsClient;
import net.pneumono.pronouns.pronouns.PronounsClientApi;

public class JoinServerEvent implements ClientPlayConnectionEvents.Join {
    @Override
    public void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client) {
        PronounsClientApi.sendInformPronounsPacket(PronounsClient.loadedPronouns);
    }
}
