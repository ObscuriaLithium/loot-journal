package dev.obscuria.lootjournal;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LootJournal
{
    public static final String MODID = "loot_journal";
    public static final String DISPLAY_NAME = "Loot Journal";
    public static final ModConfig CONFIG = new ModConfig();
    public static final Logger LOGGER = LoggerFactory.getLogger(DISPLAY_NAME);

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