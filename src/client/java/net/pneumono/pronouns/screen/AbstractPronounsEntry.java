package net.pneumono.pronouns.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

public abstract class AbstractPronounsEntry extends ElementListWidget.Entry<AbstractPronounsEntry> {

    public final MinecraftClient client;
    public final boolean indented;

    public AbstractPronounsEntry(MinecraftClient client, boolean indented) {
        this.client = client;
        this.indented = indented;
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int indentation = (this.indented ? 24 : 0);
        int textX = x + 5;
        int textY = y + (entryHeight - this.client.textRenderer.fontHeight) / 2 + 1;

        context.fill(x + indentation, y, x + entryWidth, y + entryHeight, AbstractPronounsScreen.GRAY_COLOR);

        context.drawText(this.client.textRenderer, getText(), textX + indentation, textY, AbstractPronounsScreen.WHITE_COLOR, false);
    }

    public abstract Text getText();
}
