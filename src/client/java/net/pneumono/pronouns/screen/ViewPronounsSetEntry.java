package net.pneumono.pronouns.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.pneumono.pronouns.Pronouns;
import net.pneumono.pronouns.pronouns.PronounsApi;

import java.util.List;

public class ViewPronounsSetEntry extends AbstractPronounsSetEntry {
    private final ExampleTextWidget exampleTextWidget;

    public ViewPronounsSetEntry(MinecraftClient client, ViewPronounsPlayerWidget parent, int index, boolean dropped) {
        super(client, parent, index, dropped);
        this.exampleTextWidget = new ExampleTextWidget();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        this.exampleTextWidget.setX(x + 173);
        this.exampleTextWidget.setY(y);
        this.exampleTextWidget.render(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(dropDownWidget, exampleTextWidget);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(dropDownWidget, exampleTextWidget);
    }

    public class ExampleTextWidget extends ButtonWidget {
        ExampleTextWidget() {
            super(0, 0, 20, 20, Text.translatable("gui.pronouns.screen_title"), button -> {}, DEFAULT_NARRATION_SUPPLIER);
            this.setTooltip(Tooltip.of(PronounsApi.getTranslatableTextWithPronouns("gui.pronouns.example_message", parent.getPlayerPronouns().getPronounSets()[index], parent.getPlayerPronouns().getAbbreviation())));
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderButton(context, mouseX, mouseY, delta);
            int i = this.getX() + 3;
            int j = this.getY() + 3;
            this.drawTexture(context, new Identifier(Pronouns.MOD_ID, "textures/gui/information.png"), i, j, 0, 0, 0, 15, 15, 15, 15);
        }

        @Override
        public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        }
    }
}
