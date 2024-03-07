package net.pneumono.pronouns.screen.edit;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.pneumono.pronouns.Pronouns;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounSet;
import net.pneumono.pronouns.screen.AbstractPronounsSetEntry;

import java.util.List;

public class EditPronounsSetEntry extends AbstractPronounsSetEntry {
    private final boolean alone;
    private final DeleteWidget deleteWidget;

    public EditPronounsSetEntry(MinecraftClient client, EditPronounsPlayerWidget parent, int index, boolean dropped, boolean alone) {
        super(client, parent, index, dropped);
        this.alone = alone;
        this.deleteWidget = new DeleteWidget();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        if (!alone) {
            this.deleteWidget.setX(x + 173);
            this.deleteWidget.setY(y);
            this.deleteWidget.render(context, mouseX, mouseY, tickDelta);
        }
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return alone ? List.of(dropDownWidget) : List.of(dropDownWidget, deleteWidget);
    }

    @Override
    public List<? extends Element> children() {
        return alone ? List.of(dropDownWidget) : List.of(dropDownWidget, deleteWidget);
    }

    private PlayerPronouns getPronounsWithout() {
        PronounSet[] oldSets = parent.playerPronouns.getPronounSets();
        PronounSet[] newSets = new PronounSet[oldSets.length - 1];
        System.arraycopy(oldSets, 0, newSets, 0, newSets.length);

        return new PlayerPronouns(newSets, parent.playerPronouns.getAbbreviation());
    }

    public class DeleteWidget extends ButtonWidget {
        DeleteWidget() {
            super(0, 0, 20, 20, Text.translatable("gui.pronouns.screen_title"), button -> parent.setPlayerPronouns(getPronounsWithout()), DEFAULT_NARRATION_SUPPLIER);
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderButton(context, mouseX, mouseY, delta);
            int i = this.getX() + 3;
            int j = this.getY() + 3;
            this.drawTexture(context, new Identifier(Pronouns.MOD_ID, "textures/gui/delete.png"), i, j, 0, 0, 0, 15, 15, 15, 15);
        }

        @Override
        public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        }
    }
}
