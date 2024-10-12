package dev.obscuria.lootjournal.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.obscuria.lootjournal.fabric.FabricConfig;
import me.shedaniel.autoconfig.AutoConfig;

public final class ModMenuCompat implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> AutoConfig.getConfigScreen(FabricConfig.class, parent).get();
    }
}
