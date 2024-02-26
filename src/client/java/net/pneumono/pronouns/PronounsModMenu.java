package net.pneumono.pronouns;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.pneumono.pneumonocore.PneumonoCoreModMenu;

public class PronounsModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return PneumonoCoreModMenu.getModConfigScreenFactory(Pronouns.MOD_ID);
    }
}
