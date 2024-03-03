package net.pneumono.pronouns.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.pneumono.pronouns.pronouns.PlayerPronouns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractPronounsPlayerWidget extends ElementListWidget<AbstractPronounsEntry> {
    public PlayerPronouns playerPronouns;

    public final List<AbstractPronounsEntry> entries = new ArrayList<>();
    public boolean[] selected;

    public AbstractPronounsPlayerWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight, PlayerPronouns playerPronouns) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
        this.playerPronouns = playerPronouns;
        selected = new boolean[playerPronouns.pronounSets().length];
        Arrays.fill(selected, selected.length <= 1);
    }

    public abstract void update(double scrollAmount);

    public void setSelected(int index, boolean value) {
        selected[index] = value;
        update(getScrollAmount());
    }

    public boolean getSelected(int index) {
        return selected[index];
    }

    public void setPlayerPronouns(PlayerPronouns playerPronouns) {
        this.playerPronouns = playerPronouns;
        update(getScrollAmount());
    }

    public void setPlayerPronounsNoUpdate(PlayerPronouns playerPronouns) {
        this.playerPronouns = playerPronouns;
    }

    public PlayerPronouns getPlayerPronouns() {
        return playerPronouns;
    }
}
