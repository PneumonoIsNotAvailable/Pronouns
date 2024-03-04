package net.pneumono.pronouns.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Objects;

public class SetupPronounsScreen extends EditPronounsScreen {
    public ButtonWidget skipButton;

    public SetupPronounsScreen() {
        super("Setup", false);
    }

    @Override
    protected void init() {
        super.init();
        skipButton = this.addSelectableChild(ButtonWidget.builder(
                Text.translatable("gui.pronouns.skip"), button -> Objects.requireNonNull(client).setScreen(new TitleScreen(true))
        ).dimensions(this.width / 2 - 50, getScreenHeight() + 85, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        skipButton.render(context, mouseX, mouseY, delta);
    }
}
