package net.pneumono.pronouns.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.pneumono.pneumonocore.PneumonoCore;
import net.pneumono.pronouns.pronouns.PronounsApi;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ServerPronounsPlayerListEntry extends ElementListWidget.Entry<ServerPronounsPlayerListEntry> {
    private final MinecraftClient client;
    private final UUID uuid;
    private final String name;
    private final Supplier<Identifier> skinTexture;
    private final ViewPronounsIconWidget widget;

    public static final int WHITE_COLOR = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    public static final int GRAY_COLOR = ColorHelper.Argb.getArgb(255, 74, 74, 74);

    public ServerPronounsPlayerListEntry(MinecraftClient client, UUID uuid, String name, Supplier<Identifier> skinTexture) {
        this.client = client;
        this.uuid = uuid;
        this.name = name;
        this.skinTexture = skinTexture;
        this.widget = new ViewPronounsIconWidget();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int i = x + 4;
        int j = y + (entryHeight - 24) / 2;
        int k = i + 24 + 4;
        int l = y + (entryHeight - this.client.textRenderer.fontHeight) / 2;

        context.fill(x, y, x + entryWidth, y + entryHeight, GRAY_COLOR);
        PlayerSkinDrawer.draw(context, this.skinTexture.get(), i, j, 24);

        this.widget.setX(x + 195);
        this.widget.setY(y + 5);
        this.widget.render(context, mouseX, mouseY, tickDelta);

        context.drawText(this.client.textRenderer, this.name + " (" + PronounsApi.getPlayerPronouns(this.uuid).abbreviation() + ")", k, l, WHITE_COLOR, false);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(widget);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(widget);
    }

    public String getName() {
        return name;
    }

    public class ViewPronounsIconWidget extends ButtonWidget {
        ViewPronounsIconWidget() {
            super(0, 0, 20, 20, Text.translatable("pneumonocore.configs_screen.information"), button -> ServerPronounsPlayerListEntry.this.client.setScreen(new ViewPronounsScreen(uuid, name)), DEFAULT_NARRATION_SUPPLIER);
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderButton(context, mouseX, mouseY, delta);
            int i = this.getX() + 3;
            int j = this.getY() + 3;
            this.drawTexture(context, new Identifier(PneumonoCore.MOD_ID, "textures/gui/sprites/icon/information.png"), i, j, 0, 0, 0, 15, 15, 15, 15);
        }

        @Override
        public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        }
    }
}