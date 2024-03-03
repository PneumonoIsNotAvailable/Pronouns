package net.pneumono.pronouns.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.pneumono.pronouns.Pronouns;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounsApi;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ServerPronounsPlayerListEntry extends ElementListWidget.Entry<ServerPronounsPlayerListEntry> {
    private final MinecraftClient client;
    private final UUID uuid;
    private final String name;
    private final Supplier<Identifier> skinTexture;
    private final ViewPronounsIconWidget viewWidget;
    private final NoPronounsIconWidget noWidget;

    public static final int WHITE_COLOR = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    public static final int RED_COLOR = ColorHelper.Argb.getArgb(255, 255, 85, 85);
    public static final int GRAY_COLOR = ColorHelper.Argb.getArgb(255, 74, 74, 74);

    public ServerPronounsPlayerListEntry(MinecraftClient client, UUID uuid, String name, Supplier<Identifier> skinTexture) {
        this.client = client;
        this.uuid = uuid;
        this.name = name;
        this.skinTexture = skinTexture;
        this.viewWidget = new ViewPronounsIconWidget();
        this.noWidget = new NoPronounsIconWidget();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int i = x + 4;
        int j = y + (entryHeight - 24) / 2;
        int k = i + 24 + 4;
        int l = y + (entryHeight - this.client.textRenderer.fontHeight) / 2;

        context.fill(x, y, x + entryWidth, y + entryHeight, GRAY_COLOR);
        PlayerSkinDrawer.draw(context, this.skinTexture.get(), i, j, 24);

        PlayerPronouns pronouns = PronounsApi.getPlayerPronounsNullable(this.uuid);
        boolean hasPronouns = pronouns != null;

        if (hasPronouns) {
            this.viewWidget.setX(x + 195);
            this.viewWidget.setY(y + 5);
            this.viewWidget.render(context, mouseX, mouseY, tickDelta);
        } else {
            this.noWidget.setX(x + 195);
            this.noWidget.setY(y + 5);
            this.noWidget.render(context, mouseX, mouseY, tickDelta);
        }

        String appended = hasPronouns ? " (" + pronouns.abbreviation() + ")" : "";
        context.drawText(this.client.textRenderer, this.name + appended, k, l, hasPronouns ? WHITE_COLOR : RED_COLOR, false);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        PlayerPronouns pronouns = PronounsApi.getPlayerPronounsNullable(this.uuid);
        return pronouns != null ? List.of(viewWidget) : List.of();
    }

    @Override
    public List<? extends Element> children() {
        PlayerPronouns pronouns = PronounsApi.getPlayerPronounsNullable(this.uuid);
        return pronouns != null ? List.of(viewWidget) : List.of();
    }

    public String getName() {
        return name;
    }

    public class ViewPronounsIconWidget extends ButtonWidget {
        ViewPronounsIconWidget() {
            super(0, 0, 20, 20, Text.translatable("gui.pronouns.title"), button -> ServerPronounsPlayerListEntry.this.client.setScreen(new ViewPronounsScreen(uuid, name)), DEFAULT_NARRATION_SUPPLIER);
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

    public static class NoPronounsIconWidget extends ButtonWidget {
        NoPronounsIconWidget() {
            super(0, 0, 20, 20, Text.translatable("gui.pronouns.title"), button -> {}, DEFAULT_NARRATION_SUPPLIER);
            this.setTooltip(Tooltip.of(Text.translatable("gui.pronouns.no_pronouns").formatted(Formatting.RED)));
        }

        @Override
        public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
            super.renderButton(context, mouseX, mouseY, delta);
            int i = this.getX() + 3;
            int j = this.getY() + 3;
            this.drawTexture(context, new Identifier(Pronouns.MOD_ID, "textures/gui/no_pronouns.png"), i, j, 0, 0, 0, 15, 15, 15, 15);
        }

        @Override
        public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        }
    }
}
