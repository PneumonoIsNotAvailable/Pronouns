package net.pneumono.pronouns.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.pneumono.pronouns.PronounsClient;
import net.pneumono.pronouns.pronouns.PronounsApi;

import java.util.Objects;
import java.util.UUID;

public class ViewPronounsScreen extends AbstractPronounsScreen {
    private final UUID player;

    public ViewPronounsScreen(UUID player, String name) {
        super(name);
        this.player = player;
    }

    @Override
    public ButtonWidget.PressAction getExitAction() {
        return button -> Objects.requireNonNull(client).setScreen(new ServerPronounsScreen());
    }

    @Override
    public Text getReturnText() {
        return Text.translatable("gui.pronouns.back");
    }

    @Override
    public AbstractPronounsPlayerWidget createWidget() {
        return new ViewPronounsPlayerWidget(this.client, this.width, this.height, 88, this.getPlayerListBottom(), 24, PronounsApi.getPlayerPronouns(player));
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
