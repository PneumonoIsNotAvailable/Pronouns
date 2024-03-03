package net.pneumono.pronouns.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewPronounsPlayerWidget extends ElementListWidget<ViewPronounsEntry> {
    private final PlayerPronouns playerPronouns;

    private final List<ViewPronounsEntry> entries = new ArrayList<>();
    private final boolean[] selected;

    public ViewPronounsPlayerWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight, PlayerPronouns playerPronouns) {
        super(minecraftClient, width, height, top, bottom, itemHeight);
        this.setRenderBackground(false);
        this.setRenderHorizontalShadows(false);
        this.playerPronouns = playerPronouns;
        selected = new boolean[playerPronouns.pronounSets().length];
        Arrays.fill(selected, false);
    }

    public void update(double scrollAmount) {
        this.entries.clear();
        this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.ABBREVIATION, playerPronouns.abbreviation(), false));

        for (int i = 0; i < playerPronouns.pronounSets().length; ++i) {
            PronounSet set = playerPronouns.pronounSets()[i];

            if (selected[i]) {
                this.entries.add(new ViewPronounsSetEntry(this.client, this, i, true));

                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.WEIGHT, Integer.toString(set.weight()), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.SINGULAR, Boolean.toString(set.singular()), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.SUBJECTIVE, set.subjective(), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.OBJECTIVE, set.objective(), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.POSSESSIVE_DETERMINER, set.possessiveDeterminer(), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.POSSESSIVE_PRONOUN, set.possessivePronoun(), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.REFLEXIVE, set.reflexive(), true));

            } else {
                this.entries.add(new ViewPronounsSetEntry(this.client, this, i, false));
            }
        }

        this.replaceEntries(this.entries);
        this.setScrollAmount(scrollAmount);
    }

    public void setSelected(int index, boolean value) {
        selected[index] = value;
        update(getScrollAmount());
    }

    public boolean getSelected(int index) {
        return selected[index];
    }
}
