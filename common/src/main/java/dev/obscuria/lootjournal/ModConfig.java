package dev.obscuria.lootjournal;

import dev.obscuria.lootjournal.client.render.Anchor;
import dev.obscuria.lootjournal.client.ItemPolicy;
import dev.obscuria.lootjournal.client.render.PickupDrawStyle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

public final class ModConfig
{
    public PickupDrawStyle style = ModConfigDefaults.STYLE;
    //
    public boolean itemEntryDisplay = ModConfigDefaults.ITEM_ENTRY_DISPLAY;
    public boolean itemEntryDisplayTotalAmount = ModConfigDefaults.ITEM_ENTRY_DISPLAY_TOTAL_AMOUNT;
    public boolean itemEntryUseItemFormatting = ModConfigDefaults.ITEM_ENTRY_USE_ITEM_FORMATTING;
    public Style itemEntryStyle = createStyle(
            ModConfigDefaults.ITEM_ENTRY_COLOR,
            ModConfigDefaults.ITEM_ENTRY_ITALIC);
    //
    public boolean aggregatedEntryDisplay = ModConfigDefaults.AGGREGATED_ENTRY_DISPLAY;
    public Style aggregatedEntryStyle = createStyle(
            ModConfigDefaults.AGGREGATED_ENTRY_COLOR,
            ModConfigDefaults.AGGREGATED_ENTRY_ITALIC);
    //
    public boolean experienceEntryDisplay = ModConfigDefaults.EXPERIENCE_ENTRY_DISPLAY;
    public Style experienceEntryStyle = createStyle(
            ModConfigDefaults.EXPERIENCE_ENTRY_COLOR,
            ModConfigDefaults.EXPERIENCE_ENTRY_ITALIC);
    //
    public Anchor anchor = ModConfigDefaults.ANCHOR;
    public int anchorPixelOffset = ModConfigDefaults.ANCHOR_PIXEL_OFFSET;
    public int anchorPercentOffset = ModConfigDefaults.ANCHOR_PERCENT_OFFSET;
    public int scale = ModConfigDefaults.SCALE;
    public int separation = ModConfigDefaults.SEPARATION;
    public int lifetime = ModConfigDefaults.LIFETIME;
    public int displayCapacity = ModConfigDefaults.DISPLAY_CAPACITY;
    public int queueCapacity = ModConfigDefaults.QUEUE_CAPACITY;
    //
    public ItemPolicy defaultItemPolicy = ModConfigDefaults.DEFAULT_ITEM_POLICY;
    public List<Item> itemIdBlacklist = Lists.newArrayList();
    public List<Item> itemIdWhitelist = Lists.newArrayList();
    public List<String> modIdBlacklist = Lists.newArrayList();
    public List<String> modIdWhitelist = Lists.newArrayList();

    public static String intToHex(int color)
    {
        return String.format("%06X", (0xFFFFFF & color));
    }

    public static int hexToInt(String color)
    {
        try
        {
            return Integer.parseInt(color, 16);
        }
        catch (Exception ignored)
        {
            return 0xFFFFFF;
        }
    }

    public static Style createStyle(String color, boolean italic)
    {
        return createStyle(hexToInt(color), italic);
    }

    public static Style createStyle(int color, boolean italic)
    {
        return Style.EMPTY.withColor(color).withItalic(italic);
    }

    public static List<Item> mapItems(List<? extends String> ids)
    {
        return ids.stream()
                .map(it -> Optional.ofNullable(ResourceLocation.tryParse(it)))
                .map(it -> it.map(BuiltInRegistries.ITEM::get))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
