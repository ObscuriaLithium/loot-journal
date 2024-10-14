package dev.obscuria.lootjournal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class LootJournal
{
    public static final String MODID = "loot_journal";
    public static final ModConfig CONFIG = new ModConfig();
    public static boolean shouldRebuildTabs = true;

    public static ResourceLocation key(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static boolean isAllowed(ItemStack stack)
    {
        tryRebuildTabContents();
        final var item = stack.getItem();
        if (LootJournal.CONFIG.itemsWhitelist.contains(item)) return true;
        if (LootJournal.CONFIG.tabsWhitelist.stream().anyMatch(tab -> containsAny(tab, stack))) return true;
        if (LootJournal.CONFIG.defaultBehavior.isBlacklisted()) return false;
        if (LootJournal.CONFIG.itemsBlacklist.contains(item)) return false;
        return LootJournal.CONFIG.tabsBlacklist.stream().noneMatch(tab -> containsAny(tab, stack));
    }

    private static void tryRebuildTabContents()
    {
        if (!shouldRebuildTabs) return;
        final var player = Minecraft.getInstance().player;
        if (player == null) return;
        shouldRebuildTabs = false;
        new CreativeModeInventoryScreen(player,
                player.connection.enabledFeatures(),
                Minecraft.getInstance().options.operatorItemsTab().get());
    }

    private static boolean containsAny(CreativeModeTab tab, ItemStack stack)
    {
        for (var inner : tab.getDisplayItems())
            if (inner.is(stack.getItem()))
                return true;
        return false;
    }

    public static void init() {}
}