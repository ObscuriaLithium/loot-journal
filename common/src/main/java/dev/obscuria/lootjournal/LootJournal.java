package dev.obscuria.lootjournal;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public final class LootJournal
{
    public static final String MODID = "loot_journal";
    public static final ModConfig CONFIG = new ModConfig();

    public static ResourceLocation key(String path)
    {
        return new ResourceLocation(MODID, path);
    }

    public static boolean isAllowed(ItemStack stack)
    {
        return CONFIG.defaultItemPolicy.isAllowed(stack, CONFIG);
    }

    public static void init() {}
}