package net.pneumono.pronouns.screen;

import net.minecraft.client.MinecraftClient;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounSet;

public class ViewPronounsPlayerWidget extends AbstractPronounsPlayerWidget {
    public ViewPronounsPlayerWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight, PlayerPronouns playerPronouns) {
        super(minecraftClient, width, height, top, bottom, itemHeight, playerPronouns);
    }

    public void update(double scrollAmount) {
        this.entries.clear();
        this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.ABBREVIATION, playerPronouns.getAbbreviation(), false));

        for (int i = 0; i < playerPronouns.getPronounSets().length; ++i) {
            PronounSet set = playerPronouns.getPronounSets()[i];

            if (selected[i]) {
                this.entries.add(new ViewPronounsSetEntry(this.client, this, i, true));

                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.WEIGHT, Integer.toString(set.getWeight()), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.SINGULAR, Boolean.toString(set.isSingular()), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.SUBJECTIVE, set.getSubjective(), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.OBJECTIVE, set.getObjective(), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.POSSESSIVE_DETERMINER, set.getPossessiveDeterminer(), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.POSSESSIVE_PRONOUN, set.getPossessivePronoun(), true));
                this.entries.add(new ViewPronounsValueEntry(this.client, EntryType.REFLEXIVE, set.getReflexive(), true));

            } else {
                this.entries.add(new ViewPronounsSetEntry(this.client, this, i, false));
            }
        }

        this.replaceEntries(this.entries);
        this.setScrollAmount(scrollAmount);
    }
}
