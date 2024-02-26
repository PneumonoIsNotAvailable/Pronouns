package net.pneumono.pronouns.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.pneumono.pronouns.Pronouns;

public class LeaveServerEvent implements ServerPlayConnectionEvents.Disconnect {
    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        Pronouns.uuidPronounsMap.remove(handler.getPlayer().getUuid());
    }
}
