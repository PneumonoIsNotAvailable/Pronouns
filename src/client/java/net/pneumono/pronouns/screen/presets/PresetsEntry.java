package net.pneumono.pronouns.screen.presets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.screen.AbstractPronounsScreen;
import net.pneumono.pronouns.screen.edit.EditPronounsScreen;

import java.util.List;

public class PresetsEntry extends ElementListWidget.Entry<PresetsEntry> {
    protected MinecraftClient client;
    protected PlayerPronouns pronouns;
    protected Text previousTitle;
    protected boolean inGame;

    private final ButtonWidget selectWidget;

    public PresetsEntry(MinecraftClient client, PlayerPronouns pronouns, Text previousTitle, boolean inGame) {
        this.client = client;
        this.pronouns = pronouns;
        this.previousTitle = previousTitle;
        this.inGame = inGame;

        ButtonWidget.PressAction action = button -> this.client.setScreen(new EditPronounsScreen(this.previousTitle, this.inGame, this.pronouns));
        this.selectWidget = ButtonWidget.builder(Text.translatable("gui.pronouns.select"), action).dimensions(0, 0, 65, 20).build();
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(selectWidget);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(selectWidget);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int textX = x + 5;
        int textY = y + (entryHeight - this.client.textRenderer.fontHeight) / 2 + 1;

        context.fill(x, y, x + entryWidth, y + entryHeight, AbstractPronounsScreen.GRAY_COLOR);

        context.drawText(this.client.textRenderer, this.pronouns.getAbbreviation(), textX, textY, AbstractPronounsScreen.WHITE_COLOR, false);

        selectWidget.setX(x + 150);
        selectWidget.setY(y);
        selectWidget.render(context, mouseX, mouseY, tickDelta);
    }
}
