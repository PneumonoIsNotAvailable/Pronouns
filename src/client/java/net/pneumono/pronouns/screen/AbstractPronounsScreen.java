package net.pneumono.pronouns.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.pneumono.pronouns.Pronouns;

import java.util.Objects;

public abstract class AbstractPronounsScreen extends Screen {
    public static final Identifier VIEW_PRONOUNS_TEXTURE = new Identifier(Pronouns.MOD_ID, "textures/gui/pronouns.png");
    public static final int WHITE_COLOR = ColorHelper.Argb.getArgb(255, 255, 255, 255);
    public static final int GRAY_COLOR = ColorHelper.Argb.getArgb(255, 74, 74, 74);
    public static final int RED_COLOR = ColorHelper.Argb.getArgb(255, 255, 85, 85);

    protected AbstractPronounsPlayerWidget playerWidget;
    protected ButtonWidget backButton;
    protected boolean initialized;

    protected AbstractPronounsScreen(Text title) {
        super(title);
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
            this.playerWidget.updateSize(this.width, this.height, 88, this.getPlayerListBottom());
        } else {
            this.playerWidget = createWidget();
        }
        this.playerWidget.update(this.playerWidget.getScrollAmount());

        this.addSelectableChild(this.playerWidget);

        this.backButton = this.addSelectableChild(ButtonWidget.builder(getReturnText(), getExitAction()).dimensions(this.width / 2 + 60, 74, 50, 15).build());
        this.initialized = true;
    }

    public abstract ButtonWidget.PressAction getExitAction();

    public abstract Text getReturnText();

    public abstract AbstractPronounsPlayerWidget createWidget();

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

        context.drawText(Objects.requireNonNull(this.client).textRenderer, this.title, this.width / 2 - 103, 77, WHITE_COLOR, false);
        super.render(context, mouseX, mouseY, delta);
    }
}
