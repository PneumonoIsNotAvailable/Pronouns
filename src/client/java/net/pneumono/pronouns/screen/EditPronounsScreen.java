package net.pneumono.pronouns.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.pneumono.pronouns.PronounsClient;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounsClientApi;

import java.util.Objects;

public class EditPronounsScreen extends AbstractPronounsScreen {
    private final boolean inGame;
    protected ButtonWidget presetsButton;
    protected PlayerPronouns pronouns;

    public EditPronounsScreen(Text title, boolean inGame, PlayerPronouns pronouns) {
        super(title);
        this.inGame = inGame;
        this.pronouns = pronouns;
    }

    @Override
    protected void init() {
        super.init();
        this.presetsButton = this.addSelectableChild(ButtonWidget.builder(
                Text.translatable("gui.pronouns.presets"),
                button -> Objects.requireNonNull(this.client).setScreen(new PresetsScreen(this.title, this.inGame))
        ).dimensions(this.width / 2 + 5, 74, 50, 15).build());
    }

    @Override
    public ButtonWidget.PressAction getExitAction() {
        return button -> {
            if (this.playerWidget instanceof EditPronounsPlayerWidget widget) {
                PlayerPronouns pronouns = widget.getPlayerPronouns();
                PronounsClientApi.writePronouns(pronouns);
                if (inGame) {
                    PronounsClientApi.sendInformPronounsPacket(pronouns);
                }
            }

            if (inGame) {
                Objects.requireNonNull(client).setScreen(new ServerPronounsScreen());
            } else {
                Objects.requireNonNull(client).setScreen(new TitleScreen(true));
            }
        };
    }

    @Override
    public Text getReturnText() {
        return Text.translatable("gui.pronouns.save");
    }

    @Override
    public AbstractPronounsPlayerWidget createWidget() {
        return new EditPronounsPlayerWidget(this.client, this.width, this.height, 88, this.getPlayerListBottom(), 24, pronouns);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (PronounsClient.pronounScreenKeybind.matchesKey(keyCode, scanCode) && inGame) {
            Objects.requireNonNull(this.client).setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.presetsButton.render(context, mouseX, mouseY, delta);
    }
}
