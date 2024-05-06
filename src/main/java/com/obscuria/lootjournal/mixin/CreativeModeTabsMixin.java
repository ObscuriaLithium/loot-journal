package com.obscuria.lootjournal.mixin;

import com.obscuria.lootjournal.client.TabsAccessor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(CreativeModeTabs.class)
public abstract class CreativeModeTabsMixin implements TabsAccessor {

    private static @Shadow @Nullable CreativeModeTab.ItemDisplayParameters CACHED_PARAMETERS;

    @Override
    public boolean lootJournal$ShouldRebuild() {
        return CACHED_PARAMETERS == null;
    }
}
