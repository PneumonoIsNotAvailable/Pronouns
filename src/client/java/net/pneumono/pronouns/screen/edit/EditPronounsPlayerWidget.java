package net.pneumono.pronouns.screen.edit;

import net.minecraft.client.MinecraftClient;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounSet;
import net.pneumono.pronouns.screen.AbstractPronounsPlayerWidget;
import net.pneumono.pronouns.screen.EntryType;

public class EditPronounsPlayerWidget extends AbstractPronounsPlayerWidget {
    public EditPronounsPlayerWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight, PlayerPronouns playerPronouns) {
        super(minecraftClient, width, height, top, bottom, itemHeight, playerPronouns);
    }

    @Override
    public void update(double scrollAmount) {
        boolean[] oldSelected = selected;
        selected = new boolean[playerPronouns.getPronounSets().length];
        for (int i = 0; i < selected.length; ++i) {
            selected[i] = i < oldSelected.length && oldSelected[i];
        }

        this.entries.clear();
        this.entries.add(new EditPronounsValueEntry(this.client, this, 0, EntryType.ABBREVIATION, playerPronouns.getAbbreviation(), false));
        boolean alone = playerPronouns.getPronounSets().length <= 1;

        for (int i = 0; i < playerPronouns.getPronounSets().length; ++i) {
            PronounSet set = playerPronouns.getPronounSets()[i];

            if (selected[i]) {
                this.entries.add(new EditPronounsSetEntry(this.client, this, i, true, alone));

                this.entries.add(new EditPronounsValueEntry(this.client, this, i, EntryType.SINGULAR, Boolean.toString(set.isSingular()), true));
                this.entries.add(new EditPronounsValueEntry(this.client, this, i, EntryType.SUBJECTIVE, set.getSubjective(), true));
                this.entries.add(new EditPronounsValueEntry(this.client, this, i, EntryType.OBJECTIVE, set.getObjective(), true));
                this.entries.add(new EditPronounsValueEntry(this.client, this, i, EntryType.POSSESSIVE_DETERMINER, set.getPossessiveDeterminer(), true));
                this.entries.add(new EditPronounsValueEntry(this.client, this, i, EntryType.POSSESSIVE_PRONOUN, set.getPossessivePronoun(), true));
                this.entries.add(new EditPronounsValueEntry(this.client, this, i, EntryType.REFLEXIVE, set.getReflexive(), true));

            } else {
                this.entries.add(new EditPronounsSetEntry(this.client, this, i, false, alone));
            }
        }

        this.entries.add(new EditPronounsAddSetEntry(this.client, this, false));

        this.replaceEntries(this.entries);
        this.setScrollAmount(scrollAmount);
    }
}
