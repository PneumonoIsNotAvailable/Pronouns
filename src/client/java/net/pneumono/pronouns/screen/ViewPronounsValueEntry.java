package net.pneumono.pronouns.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.text.Text;

import java.util.List;

public class ViewPronounsValueEntry extends ViewPronounsEntry {
    private final EntryType type;
    private final String value;

    public ViewPronounsValueEntry(MinecraftClient client, EntryType type, String value, boolean indented) {
        super(client, indented);
        this.type = type;
        this.value = value;
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of();
    }

    @Override
    public List<? extends Element> children() {
        return List.of();
    }

    @Override
    public Text getText() {
        return Text.translatable("gui.pronouns.pronoun_type." + type.name().toLowerCase(), value);
    }
}
