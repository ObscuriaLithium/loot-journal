package dev.obscuria.lootjournal;

import dev.obscuria.lootjournal.client.render.Anchor;
import dev.obscuria.lootjournal.client.DefaultBehavior;
import dev.obscuria.lootjournal.client.render.Style;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;

public final class ModConfig
{
    public static final Style STYLE = Style.CLASSIC;
    public static final boolean USE_RARITY_COLOR = true;
    public static final int ITEMS_COLOR = 0xFFFFFF;
    public static final int GROUPED_ITEMS_COLOR = 0xFFFFFF;
    public static final int EXPERIENCE_COLOR = 0x55FF55;
    public static final boolean DISPLAY_EXPERIENCE = true;
    public static final boolean DISPLAY_TOTAL = true;
    public static final Anchor ANCHOR = Anchor.BOTTOM_RIGHT;
    public static final int ANCHOR_OFFSET = 3;
    public static final int NOTIFICATION_SEPARATION = 3;
    public static final int NOTIFICATION_LIFETIME = 6;
    public static final int MAX_VISIBLE_NOTIFICATIONS = 12;
    public static final int MAX_QUEUED_NOTIFICATIONS = 23;
    public static final DefaultBehavior DEFAULT_BEHAVIOR = DefaultBehavior.ALL_WHITELISTED;

    public Style style = STYLE;
    public boolean useRarityColor = USE_RARITY_COLOR;
    public int itemsColor = ITEMS_COLOR;
    public int groupedItemsColor = GROUPED_ITEMS_COLOR;
    public int experienceColor = EXPERIENCE_COLOR;
    public boolean displayExperience = DISPLAY_EXPERIENCE;
    public boolean displayTotal = DISPLAY_TOTAL;

    public Anchor anchor = ANCHOR;
    public int anchorOffset = ANCHOR_OFFSET;
    public int notificationSeparation = NOTIFICATION_SEPARATION;
    public double notificationLifetime = NOTIFICATION_LIFETIME;
    public int maxVisibleNotifications = MAX_VISIBLE_NOTIFICATIONS;
    public int maxQueuedNotifications = MAX_QUEUED_NOTIFICATIONS;

    public DefaultBehavior defaultBehavior = DEFAULT_BEHAVIOR;
    public List<CreativeModeTab> tabsBlacklist = Lists.newArrayList();
    public List<Item> itemsBlacklist = Lists.newArrayList();
    public List<CreativeModeTab> tabsWhitelist = Lists.newArrayList();
    public List<Item> itemsWhitelist = Lists.newArrayList();

    public static String intToHex(int color)
    {
        return String.format("%06X", (0xFFFFFF & color));
    }

    public static int hexToInt(String color)
    {
        try {return Integer.parseInt(color, 16);}
        catch (Exception ignored) {return 0xFFFFFF;}
    }

    public static List<CreativeModeTab> mapTabs(List<? extends String> values)
    {
        return values.stream()
                .map(value -> Optional.ofNullable(ResourceLocation.tryParse(value)))
                .map(value -> value.map(BuiltInRegistries.CREATIVE_MODE_TAB::get))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public static List<Item> mapItems(List<? extends String> values)
    {
        return values.stream()
                .map(value -> Optional.ofNullable(ResourceLocation.tryParse(value)))
                .map(value -> value.map(BuiltInRegistries.ITEM::get))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
