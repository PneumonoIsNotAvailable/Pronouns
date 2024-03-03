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

import java.util.List;

public class ViewPronounsSetEntry extends ViewPronounsEntry {
    private final ViewPronounsPlayerWidget parent;
    private final int index;
    private final boolean dropped;
    private final DropDownWidget widget;

    public ViewPronounsSetEntry(MinecraftClient client, ViewPronounsPlayerWidget parent, int index, boolean dropped) {
        super(client, false);
        this.parent = parent;
        this.index = index;
        this.dropped = dropped;
        this.widget = new DropDownWidget();
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(widget);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(widget);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        this.widget.setX(x + 195);
        this.widget.setY(y);
        this.widget.render(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public Text getText() {
        return Text.translatable("gui.pronouns.set", this.index + 1);
    }

    public class DropDownWidget extends ButtonWidget {
        DropDownWidget() {
            super(0, 0, 20, 20, Text.translatable("gui.pronouns.screen_title"), button -> parent.setSelected(index, !parent.getSelected(index)), DEFAULT_NARRATION_SUPPLIER);
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderButton(context, mouseX, mouseY, delta);
            int i = this.getX() + 3;
            int j = this.getY() + 3;
            this.drawTexture(context, new Identifier(Pronouns.MOD_ID, "textures/gui/drop_down.png"), i, j, (dropped ? 15 : 0), 0, 0, 15, 15, 30, 15);
        }

        @Override
        public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        }
    }
}
