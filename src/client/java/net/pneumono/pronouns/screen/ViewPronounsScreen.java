package net.pneumono.pronouns.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.pneumono.pronouns.Pronouns;
import net.pneumono.pronouns.PronounsClient;
import net.pneumono.pronouns.pronouns.PronounsApi;

import java.util.Objects;
import java.util.UUID;

public class ViewPronounsScreen extends Screen {
    private static final Identifier VIEW_PRONOUNS_TEXTURE = new Identifier(Pronouns.MOD_ID, "textures/gui/pronouns.png");
    public static final int WHITE_COLOR = ColorHelper.Argb.getArgb(255, 255, 255, 255);

    private final UUID player;
    private final String name;
    private ViewPronounsPlayerWidget playerWidget;
    private ButtonWidget backButton;
    private boolean initialized;

    public ViewPronounsScreen(UUID player, String name) {
        super(Text.translatable("gui.pronouns.screen_title"));
        this.player = player;
        this.name = name;
    }

    private int getScreenHeight() {
        return Math.max(52, this.height - 128 - 16);
    }

    private int getPlayerListBottom() {
        return 80 + this.getScreenHeight() - 8;
    }

    private int getSearchBoxX() {
        return (this.width - 238) / 2;
    }

    @Override
    protected void init() {
        if (this.initialized) {
            this.playerWidget.updateSize(this.width, this.height, 88, this.getPlayerListBottom());
        } else {
            this.playerWidget = new ViewPronounsPlayerWidget(this.client, this.width, this.height, 88, this.getPlayerListBottom(), 24, PronounsApi.getPlayerPronouns(player));
        }
        this.playerWidget.update(this.playerWidget.getScrollAmount());

        ButtonWidget.PressAction action = button -> Objects.requireNonNull(client).setScreen(new ServerPronounsScreen());
        this.backButton = this.addSelectableChild(ButtonWidget.builder(Text.translatable("gui.pronouns.back"), action).dimensions(this.width / 2 + 61, 73, 50, 14).build());

        this.addSelectableChild(this.playerWidget);
        this.initialized = true;
    }

    @Override
    public void renderBackground(DrawContext context) {
        int i = this.getSearchBoxX() + 3;
        super.renderBackground(context);
        context.drawNineSlicedTexture(VIEW_PRONOUNS_TEXTURE, i, 64, 236, this.getScreenHeight() + 16, 8, 236, 34, 1, 1);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        this.playerWidget.render(context, mouseX, mouseY, delta);
        this.backButton.render(context, mouseX, mouseY, delta);
        context.drawText(Objects.requireNonNull(this.client).textRenderer, Text.translatable("gui.pronouns.name", name), this.width / 2 - 106, 76, WHITE_COLOR, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (PronounsClient.pronounScreenKeybind.matchesKey(keyCode, scanCode)) {
            Objects.requireNonNull(this.client).setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
