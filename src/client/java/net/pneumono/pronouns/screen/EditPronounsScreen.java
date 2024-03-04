package net.pneumono.pronouns.screen;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounsClientApi;

import java.util.Objects;

public class EditPronounsScreen extends AbstractPronounsScreen {
    private final boolean inGame;

    public EditPronounsScreen(String name, boolean inGame) {
        super(name);
        this.inGame = inGame;
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
        return new EditPronounsPlayerWidget(this.client, this.width, this.height, 88, this.getPlayerListBottom(), 24, PronounsClientApi.getLoadedPronouns());
    }
}
