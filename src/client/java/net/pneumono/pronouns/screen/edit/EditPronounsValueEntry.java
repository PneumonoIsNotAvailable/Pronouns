package net.pneumono.pronouns.screen.edit;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.pneumono.pronouns.pronouns.PlayerPronouns;
import net.pneumono.pronouns.pronouns.PronounSet;
import net.pneumono.pronouns.screen.EntryType;
import net.pneumono.pronouns.screen.view.ViewPronounsValueEntry;

import java.util.List;

public class EditPronounsValueEntry extends ViewPronounsValueEntry {
    private final EditPronounsPlayerWidget parent;
    private final int index;
    private final ButtonWidget toggleWidget;
    private boolean toggleWidgetValue;
    private final TextFieldWidget textWidget;

    public EditPronounsValueEntry(MinecraftClient client, EditPronounsPlayerWidget parent, int index, EntryType type, String value, boolean indented) {
        super(client, type, value, indented);
        this.parent = parent;
        this.index = index;

        this.toggleWidgetValue = Boolean.parseBoolean(value);
        this.toggleWidget = ButtonWidget.builder(Text.literal(this.value), (button) -> {
            this.toggleWidgetValue = !this.toggleWidgetValue;
            this.parent.setPlayerPronouns(getPronounsWithValue(Boolean.toString(toggleWidgetValue)));
        }).dimensions(0, 0, 65, 20).build();

        this.textWidget = new TextFieldWidget(this.client.textRenderer, 0, 0, 65, 20, null, getText());
        this.textWidget.setText(value);
        this.textWidget.setChangedListener(text -> this.parent.setPlayerPronounsNoUpdate(getPronounsWithValue(text)));
    }

    private PlayerPronouns getPronounsWithValue(String value) {
        if (type == EntryType.ABBREVIATION) {
            return new PlayerPronouns(parent.playerPronouns.getPronounSets(), value);
        }

        PronounSet[] sets = parent.playerPronouns.getPronounSets();
        PronounSet oldSet = sets[index];

        sets[index] = switch (type) {
            case SINGULAR -> oldSet.setSingular(Boolean.parseBoolean(value));
            case SUBJECTIVE -> oldSet.setSubjective(value);
            case OBJECTIVE -> oldSet.setObjective(value);
            case POSSESSIVE_DETERMINER -> oldSet.setPossessiveDeterminer(value);
            case POSSESSIVE_PRONOUN -> oldSet.setPossessivePronoun(value);
            case REFLEXIVE -> oldSet.setReflexive(value);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };

        return new PlayerPronouns(sets, parent.playerPronouns.getAbbreviation());
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(type == EntryType.SINGULAR ? toggleWidget : textWidget);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(type == EntryType.SINGULAR ? toggleWidget : textWidget);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        if (type == EntryType.SINGULAR) {
            toggleWidget.setX(x + 150);
            toggleWidget.setY(y);
            toggleWidget.render(context, mouseX, mouseY, tickDelta);
        } else {
            textWidget.setX(x + 150);
            textWidget.setY(y);
            textWidget.render(context, mouseX, mouseY, tickDelta);
        }
    }

    @Override
    public Text getText() {
        return Text.translatable("gui.pronouns.pronoun_type." + type.name().toLowerCase());
    }
}
