package dev.obscuria.lootjournal.client;

import dev.obscuria.lootjournal.ModConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public enum ItemPolicy
{
    ALLOW_ALL
            {
                @Override
                public boolean isAllowed(ItemStack stack, ModConfig config)
                {
                    final var item = stack.getItem();
                    if (config.itemIdBlacklist.contains(item)) return false;
                    var itemId = BuiltInRegistries.ITEM.getKey(item);
                    return !config.modIdBlacklist.contains(itemId.getNamespace());
                }
            },
    DENY_ALL
            {
                @Override
                public boolean isAllowed(ItemStack stack, ModConfig config)
                {
                    final var item = stack.getItem();
                    if (config.itemIdWhitelist.contains(item)) return true;
                    var itemId = BuiltInRegistries.ITEM.getKey(item);
                    return config.modIdWhitelist.contains(itemId.getNamespace());
                }
            };

    public abstract boolean isAllowed(ItemStack stack, ModConfig config);
}
