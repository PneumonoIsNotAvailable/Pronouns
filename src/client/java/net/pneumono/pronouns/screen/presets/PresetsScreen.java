package net.pneumono.pronouns.screen.presets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.pneumono.pronouns.Pronouns;
import net.pneumono.pronouns.PronounsClient;
import net.pneumono.pronouns.pronouns.PronounsClientApi;
import net.pneumono.pronouns.screen.AbstractPronounsScreen;
import net.pneumono.pronouns.screen.edit.EditPronounsScreen;

import java.util.Objects;

public class PresetsScreen extends Screen {
    public static final Identifier VIEW_PRONOUNS_TEXTURE = new Identifier(Pronouns.MOD_ID, "textures/gui/pronouns.png");

    protected final Text previousTitle;
    protected final boolean inGame;
    protected PresetsWidget presetsWidget;
    protected ButtonWidget backButton;
    protected boolean initialized;

    public PresetsScreen(Text previousTitle, boolean inGame) {
        super(Text.translatable("gui.pronouns.presets"));
        this.previousTitle = previousTitle;
        this.inGame = inGame;
    }

    public int getScreenHeight() {
        return Math.max(52, this.height - 128 - 16);
    }

    public int getSearchBoxX() {
        return (this.width - 238) / 2;
    }

    public int getPlayerListBottom() {
        return 80 + this.getScreenHeight() - 8;
    }

    @Override
    protected void init() {
        super.init();

        if (this.initialized) {
            this.presetsWidget.updateSize(this.width, this.height, 88, this.getPlayerListBottom());
        } else {
            this.presetsWidget = new PresetsWidget(this.client, this.width, this.height, 88, this.getPlayerListBottom(), 24, previousTitle, inGame);
        }
        this.presetsWidget.update(this.presetsWidget.getScrollAmount());

        this.addSelectableChild(this.presetsWidget);

        Text returnText = Text.translatable("gui.pronouns.back");
        ButtonWidget.PressAction action = button -> Objects.requireNonNull(client).setScreen(
                new EditPronounsScreen(Text.literal(Objects.requireNonNull(client.player).getName().getString()), inGame, PronounsClientApi.getLoadedPronouns())
        );
        this.backButton = this.addSelectableChild(ButtonWidget.builder(returnText, action).dimensions(this.width / 2 + 60, 74, 50, 15).build());
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

        this.presetsWidget.render(context, mouseX, mouseY, delta);
        this.backButton.render(context, mouseX, mouseY, delta);

        context.drawText(Objects.requireNonNull(this.client).textRenderer, this.title, this.width / 2 - 103, 77, AbstractPronounsScreen.WHITE_COLOR, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (PronounsClient.pronounScreenKeybind.matchesKey(keyCode, scanCode) && inGame) {
            Objects.requireNonNull(this.client).setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
