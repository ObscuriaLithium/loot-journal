package com.obscuria.lootjournal.client;

import com.obscuria.lootjournal.LootJournalConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
public final class Filtering {

    public static boolean isAllowed(ItemStack stack) {
        final var id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemsWhitelist().contains(id)) return true;
        if (tabsWhitelist().contains(id)) return true;
        if (LootJournalConfig.blacklistedByDefault.get()) return false;
        if (itemsBlacklist().contains(id)) return false;
        return !tabsBlacklist().contains(id);
    }

    private static List<ResourceLocation> itemsWhitelist() {
        return unwrapIds(LootJournalConfig.itemsWhitelist.get()).toList();
    }

    private static List<ResourceLocation> itemsBlacklist() {
        return unwrapIds(LootJournalConfig.itemsBlacklist.get()).toList();
    }

    private static List<ResourceLocation> tabsWhitelist() {
        return unwrapIds(LootJournalConfig.tabsWhitelist.get())
                .flatMap(Filtering::mapToTab)
                .flatMap(Filtering::mapToContent)
                .toList();
    }

    private static List<ResourceLocation> tabsBlacklist() {
        return unwrapIds(LootJournalConfig.tabsBlacklist.get())
                .flatMap(Filtering::mapToTab)
                .flatMap(Filtering::mapToContent)
                .toList();
    }

    private static Stream<ResourceLocation> unwrapIds(List<? extends String> list) {
        return list.stream().map(ResourceLocation::new);
    }

    private static Stream<CreativeModeTab> mapToTab(ResourceLocation id) {
        final var result = CreativeModeTabRegistry.getTab(id);
        return result == null ? Stream.empty() : Stream.of(result);
    }

    private static Stream<ResourceLocation> mapToContent(CreativeModeTab tab) {
        tryRebuildTabContents();
        return tab.getDisplayItems().stream().map(stack -> ForgeRegistries.ITEMS.getKey(stack.getItem()));
    }

    @SuppressWarnings("all")
    private static void tryRebuildTabContents() {
        final var player = Minecraft.getInstance().player;
        final var tabs = new CreativeModeTabs();
        if (player != null
                && tabs instanceof TabsAccessor accessor
                && accessor.lootJournal$ShouldRebuild())
            CreativeModeTabs.tryRebuildTabContents(FeatureFlags.DEFAULT_FLAGS, false, player.level().registryAccess());
    }
}
