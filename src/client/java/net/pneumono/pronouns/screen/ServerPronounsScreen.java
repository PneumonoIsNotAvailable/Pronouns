package net.pneumono.pronouns.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.pneumono.pronouns.Pronouns;
import net.pneumono.pronouns.PronounsClient;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class ServerPronounsScreen extends Screen {
    private static final Identifier VIEW_PRONOUNS_TEXTURE = new Identifier(Pronouns.MOD_ID, "textures/gui/pronouns.png");
    private static final Text SEARCH_TEXT = Text.translatable("gui.server_pronouns.search_hint").formatted(Formatting.ITALIC).formatted(Formatting.GRAY);
    private static final Text EMPTY_SEARCH_TEXT = Text.translatable("gui.server_pronouns.search_empty").formatted(Formatting.GRAY);

    private ServerPronounsPlayerListWidget playerList;
    private TextFieldWidget searchBox;
    private String currentSearch = "";
    private boolean initialized;

    public ServerPronounsScreen() {
        super(Text.translatable("gui.pronouns.screen_title"));
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
    public void tick() {
        super.tick();
        this.searchBox.tick();
    }

    @Override
    protected void init() {
        if (this.initialized) {
            this.playerList.updateSize(this.width, this.height, 88, this.getPlayerListBottom());
        } else {
            this.playerList = new ServerPronounsPlayerListWidget(this.client, this.width, this.height, 88, this.getPlayerListBottom(), 36);
        }
        Collection<UUID> uuids = Objects.requireNonNull(Objects.requireNonNull(this.client).player).networkHandler.getPlayerUuids();
        this.playerList.update(uuids, this.playerList.getScrollAmount());

        String string = this.searchBox != null ? this.searchBox.getText() : "";
        this.searchBox = new TextFieldWidget(this.textRenderer, this.getSearchBoxX() + 29, 75, 198, 13, SEARCH_TEXT){
            @Override
            protected MutableText getNarrationMessage() {
                if (!searchBox.getText().isEmpty() && playerList.isEmpty()) {
                    return super.getNarrationMessage().append(", ").append(EMPTY_SEARCH_TEXT);
                }
                return super.getNarrationMessage();
            }
        };
        this.searchBox.setMaxLength(16);
        this.searchBox.setVisible(true);
        this.searchBox.setEditableColor(0xFFFFFF);
        this.searchBox.setText(string);
        this.searchBox.setPlaceholder(SEARCH_TEXT);
        this.searchBox.setChangedListener(this::onSearchChange);
        this.addSelectableChild(this.searchBox);
        this.addSelectableChild(this.playerList);
        this.initialized = true;
    }

    @Override
    public void renderBackground(DrawContext context) {
        int i = this.getSearchBoxX() + 3;
        super.renderBackground(context);
        context.drawNineSlicedTexture(VIEW_PRONOUNS_TEXTURE, i, 64, 236, this.getScreenHeight() + 16, 8, 236, 34, 1, 1);
        context.drawTexture(VIEW_PRONOUNS_TEXTURE, i + 10, 76, 243, 1, 12, 12);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        if (!this.playerList.isEmpty()) {
            this.playerList.render(context, mouseX, mouseY, delta);
        } else if (!this.searchBox.getText().isEmpty()) {
            context.drawCenteredTextWithShadow(Objects.requireNonNull(this.client).textRenderer, EMPTY_SEARCH_TEXT, this.width / 2, (72 + this.getPlayerListBottom()) / 2, -1);
        }
        this.searchBox.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.searchBox.isFocused() && PronounsClient.pronounScreenKeybind.matchesKey(keyCode, scanCode)) {
            Objects.requireNonNull(this.client).setScreen(null);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onSearchChange(String currentSearch) {
        if (!(currentSearch = currentSearch.toLowerCase(Locale.ROOT)).equals(this.currentSearch)) {
            this.playerList.setCurrentSearch(currentSearch);
            this.currentSearch = currentSearch;
        }
        Collection<UUID> uuids = Objects.requireNonNull(Objects.requireNonNull(this.client).player).networkHandler.getPlayerUuids();
        this.playerList.update(uuids, this.playerList.getScrollAmount());
    }
}
