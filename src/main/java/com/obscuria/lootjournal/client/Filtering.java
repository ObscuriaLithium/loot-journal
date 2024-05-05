package com.obscuria.lootjournal.client;

import com.obscuria.lootjournal.LootJournalConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public final class Filtering {

    public static boolean isAllowed(ItemStack stack) {
        final var itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemsWhitelist().contains(itemId)) return true;
        if (LootJournalConfig.blacklistedByDefault.get()) return false;
        return !itemsBlacklist().contains(itemId);
    }

    private static List<ResourceLocation> tabsBlacklist() {
        return remap(LootJournalConfig.tabsBlacklist.get());
    }

    private static List<ResourceLocation> itemsBlacklist() {
        return remap(LootJournalConfig.itemsBlacklist.get());
    }

    private static List<ResourceLocation> tabsWhitelist() {
        return remap(LootJournalConfig.tabsWhitelist.get());
    }

    private static List<ResourceLocation> itemsWhitelist() {
        return remap(LootJournalConfig.itemsWhitelist.get());
    }

    private static List<ResourceLocation> remap(List<? extends String> list) {
        return list.stream().map(ResourceLocation::new).toList();
    }
}
