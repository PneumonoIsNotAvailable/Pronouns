package net.pneumono.pronouns.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.pneumono.pronouns.PronounsClient;
import net.pneumono.pronouns.pronouns.PronounsClientApi;

import java.util.Objects;

public class EditPronounsScreen extends AbstractPronounsScreen {
    protected EditPronounsScreen(String name) {
        super(name);
    }

    @Override
    public ButtonWidget.PressAction getExitAction() {
        return button -> {
            if (this.playerWidget instanceof EditPronounsPlayerWidget widget) {
                PronounsClientApi.sendInformPronounsPacket(PronounsClientApi.writePronouns(widget.getPlayerPronouns()));
            }
            Objects.requireNonNull(client).setScreen(new ServerPronounsScreen());
        };
    }

    @Override
    public Text getReturnText() {
        return Text.translatable("gui.pronouns.save");
    }

    @Override
    public AbstractPronounsPlayerWidget createWidget() {
        return new EditPronounsPlayerWidget(this.client, this.width, this.height, 88, this.getPlayerListBottom(), 24, PronounsClient.loadedPronouns);
    }
}
