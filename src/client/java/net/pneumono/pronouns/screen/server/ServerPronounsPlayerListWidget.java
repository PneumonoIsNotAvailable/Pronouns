package net.pneumono.pronouns.screen.server;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ServerPronounsPlayerListWidget extends ElementListWidget<ServerPronounsPlayerListEntry> {
    private final List<ServerPronounsPlayerListEntry> players = Lists.newArrayList();
    @Nullable
    private String currentSearch;

    public ServerPronounsPlayerListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
    }

    @Override
    protected void enableScissor(DrawContext context) {
        context.enableScissor(this.left, this.top + 4, this.right, this.bottom);
    }

    public void update(Collection<UUID> uuids, double scrollAmount) {
        HashMap<UUID, ServerPronounsPlayerListEntry> map = new HashMap<>();
        this.setPlayers(uuids, map);
        this.refresh(map.values(), scrollAmount);
    }

    private void setPlayers(Collection<UUID> playerUuids, Map<UUID, ServerPronounsPlayerListEntry> entriesByUuids) {
        for (UUID uUID : playerUuids) {
            ClientPlayNetworkHandler clientPlayNetworkHandler = Objects.requireNonNull(this.client.player).networkHandler;
            PlayerListEntry playerListEntry = clientPlayNetworkHandler.getPlayerListEntry(uUID);
            if (playerListEntry == null) continue;
            entriesByUuids.put(uUID, new ServerPronounsPlayerListEntry(this.client, uUID, playerListEntry.getProfile().getName(), playerListEntry::getSkinTexture));
        }
    }

    private void refresh(Collection<ServerPronounsPlayerListEntry> players, double scrollAmount) {
        this.players.clear();
        this.players.addAll(players);
        this.filterPlayers();
        this.replaceEntries(this.players);
        this.setScrollAmount(scrollAmount);
    }

    private void filterPlayers() {
        if (this.currentSearch != null) {
            this.players.removeIf(player -> !player.getName().toLowerCase(Locale.ROOT).contains(this.currentSearch));
            this.replaceEntries(this.players);
        }
    }

    public void setCurrentSearch(@Nullable String currentSearch) {
        this.currentSearch = currentSearch;
    }

    public boolean isEmpty() {
        return this.players.isEmpty();
    }
}
