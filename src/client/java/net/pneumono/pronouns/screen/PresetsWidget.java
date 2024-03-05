package net.pneumono.pronouns.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.Presets;

import java.util.ArrayList;
import java.util.List;

public class PresetsWidget extends ElementListWidget<PresetsEntry> {
    public final List<PresetsEntry> entries = new ArrayList<>();
    public final Text previousTitle;
    public final boolean inGame;

    public PresetsWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight, Text previousTitle, boolean inGame) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);

        this.previousTitle = previousTitle;
        this.inGame = inGame;
    }

    public void update(double scrollAmount) {
        this.entries.clear();

        for (PlayerPronouns preset : Presets.getAllPronouns()) {
            this.entries.add(new PresetsEntry(this.client, preset, previousTitle, inGame));
        }

        this.replaceEntries(this.entries);
        this.setScrollAmount(scrollAmount);
    }
}
