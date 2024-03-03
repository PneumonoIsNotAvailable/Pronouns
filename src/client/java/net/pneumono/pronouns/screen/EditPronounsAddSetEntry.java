package net.pneumono.pronouns.screen;

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
import net.pneumono.pronouns.pronouns.PronounsApi;

import java.util.List;

public class EditPronounsAddSetEntry extends AbstractPronounsEntry {
    protected final EditPronounsPlayerWidget parent;

    private final AddWidget addWidget;

    public EditPronounsAddSetEntry(MinecraftClient client, EditPronounsPlayerWidget parent, boolean indented) {
        super(client, indented);
        this.parent = parent;
        this.addWidget = new AddWidget();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int indentation = (this.indented ? 24 : 0);
        int textX = x + 28;
        int textY = y + (entryHeight - this.client.textRenderer.fontHeight) / 2 + 1;

        context.drawText(this.client.textRenderer, getText(), textX + indentation, textY, WHITE_COLOR, false);

        this.addWidget.setX(x + 5);
        this.addWidget.setY(y);
        this.addWidget.render(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public Text getText() {
        return Text.translatable("gui.pronouns.add_set");
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(addWidget);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(addWidget);
    }

    private PlayerPronouns getPronounsWithNew() {
        PronounSet[] oldSets = parent.playerPronouns.pronounSets();
        PronounSet[] newSets = new PronounSet[oldSets.length + 1];
        System.arraycopy(oldSets, 0, newSets, 0, oldSets.length);
        newSets[newSets.length - 1] = PronounsApi.DEFAULT_PRONOUN_SET;

        return new PlayerPronouns(newSets, parent.playerPronouns.abbreviation());
    }

    public class AddWidget extends ButtonWidget {
        AddWidget() {
            super(0, 0, 20, 20, Text.translatable("gui.pronouns.screen_title"), button -> parent.setPlayerPronouns(getPronounsWithNew()), DEFAULT_NARRATION_SUPPLIER);
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderButton(context, mouseX, mouseY, delta);
            int i = this.getX() + 3;
            int j = this.getY() + 3;
            this.drawTexture(context, new Identifier(Pronouns.MOD_ID, "textures/gui/add.png"), i, j, 0, 0, 0, 15, 15, 15, 15);
        }

        @Override
        public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        }
    }
}
