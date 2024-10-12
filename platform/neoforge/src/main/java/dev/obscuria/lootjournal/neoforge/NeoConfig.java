package dev.obscuria.lootjournal.neoforge;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.ModConfig;
import dev.obscuria.lootjournal.client.DefaultBehavior;
import dev.obscuria.lootjournal.client.render.Anchor;
import dev.obscuria.lootjournal.client.render.Style;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.config.ModConfig.Type;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public final class NeoConfig
{
    private static final ModConfigSpec SPEC;

    private static final ModConfigSpec.EnumValue<Style> STYLE;
    private static final ModConfigSpec.BooleanValue USE_RARITY_COLOR;
    private static final ModConfigSpec.ConfigValue<String> ITEMS_COLOR;
    private static final ModConfigSpec.ConfigValue<String> GROUPED_ITEMS_COLOR;
    private static final ModConfigSpec.ConfigValue<String> EXPERIENCE_COLOR;
    private static final ModConfigSpec.BooleanValue DISPLAY_EXPERIENCE;
    private static final ModConfigSpec.BooleanValue DISPLAY_TOTAL;

    private static final ModConfigSpec.EnumValue<Anchor> ANCHOR;
    private static final ModConfigSpec.IntValue ANCHOR_OFFSET;
    private static final ModConfigSpec.IntValue NOTIFICATION_SEPARATION;
    private static final ModConfigSpec.IntValue NOTIFICATION_LIFETIME;
    private static final ModConfigSpec.IntValue MAX_VISIBLE_NOTIFICATIONS;
    private static final ModConfigSpec.IntValue MAX_QUEUED_NOTIFICATIONS;

    private static final ModConfigSpec.EnumValue<DefaultBehavior> DEFAULT_BEHAVIOR;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> TABS_BLACKLIST;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEMS_BLACKLIST;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> TABS_WHITELIST;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> ITEMS_WHITELIST;

    public static void init(IEventBus eventBus,
                            ModContainer container)
    {
        eventBus.addListener(NeoConfig::onUpdate);
        container.registerConfig(
                Type.CLIENT, SPEC,
                "obscuria/loot_journal-client.toml");
    }

    public static void onUpdate(final ModConfigEvent event)
    {
        LootJournal.CONFIG.style = STYLE.get();
        LootJournal.CONFIG.useRarityColor = USE_RARITY_COLOR.get();
        LootJournal.CONFIG.itemsColor = ModConfig.hexToInt(ITEMS_COLOR.get());
        LootJournal.CONFIG.groupedItemsColor = ModConfig.hexToInt(GROUPED_ITEMS_COLOR.get());
        LootJournal.CONFIG.experienceColor = ModConfig.hexToInt(EXPERIENCE_COLOR.get());
        LootJournal.CONFIG.displayExperience = DISPLAY_EXPERIENCE.get();
        LootJournal.CONFIG.displayTotal = DISPLAY_TOTAL.get();

        LootJournal.CONFIG.anchor = ANCHOR.get();
        LootJournal.CONFIG.anchorOffset = ANCHOR_OFFSET.get();
        LootJournal.CONFIG.notificationSeparation = NOTIFICATION_SEPARATION.get();
        LootJournal.CONFIG.notificationLifetime = NOTIFICATION_LIFETIME.get();
        LootJournal.CONFIG.maxVisibleNotifications = MAX_VISIBLE_NOTIFICATIONS.get();
        LootJournal.CONFIG.maxQueuedNotifications = MAX_QUEUED_NOTIFICATIONS.get();

        LootJournal.CONFIG.defaultBehavior = DEFAULT_BEHAVIOR.get();
        LootJournal.CONFIG.tabsBlacklist = ModConfig.mapTabs(TABS_BLACKLIST.get());
        LootJournal.CONFIG.itemsBlacklist = ModConfig.mapItems(ITEMS_BLACKLIST.get());
        LootJournal.CONFIG.tabsWhitelist = ModConfig.mapTabs(TABS_WHITELIST.get());
        LootJournal.CONFIG.itemsWhitelist = ModConfig.mapItems(ITEMS_WHITELIST.get());
    }

    static
    {
        final var builder = new ModConfigSpec.Builder();

        builder.push("Display");
        STYLE = builder.defineEnum("style", ModConfig.STYLE);
        USE_RARITY_COLOR = builder.define("useRarityColor", ModConfig.USE_RARITY_COLOR);
        ITEMS_COLOR = builder.define("itemsColor", ModConfig.intToHex(ModConfig.ITEMS_COLOR));
        GROUPED_ITEMS_COLOR = builder.define("groupedItemsColor", ModConfig.intToHex(ModConfig.GROUPED_ITEMS_COLOR));
        EXPERIENCE_COLOR = builder.define("experienceColor", ModConfig.intToHex(ModConfig.EXPERIENCE_COLOR));
        DISPLAY_EXPERIENCE = builder.define("displayExperience", ModConfig.DISPLAY_EXPERIENCE);
        DISPLAY_TOTAL = builder.define("displayTotal", ModConfig.DISPLAY_TOTAL);
        builder.pop();

        builder.push("Positioning");
        ANCHOR = builder.defineEnum("anchor", ModConfig.ANCHOR);
        ANCHOR_OFFSET = builder.defineInRange("anchorOffset", ModConfig.ANCHOR_OFFSET, 0, 256);
        NOTIFICATION_SEPARATION = builder.defineInRange("notificationSeparation", ModConfig.NOTIFICATION_SEPARATION, 0, 16);
        NOTIFICATION_LIFETIME = builder.defineInRange("notificationLifetime", ModConfig.NOTIFICATION_LIFETIME, 0, 32);
        MAX_VISIBLE_NOTIFICATIONS = builder.defineInRange("maxVisibleNotifications", ModConfig.MAX_VISIBLE_NOTIFICATIONS, 1, 64);
        MAX_QUEUED_NOTIFICATIONS = builder.defineInRange("maxQueuedNotifications", ModConfig.MAX_QUEUED_NOTIFICATIONS, 0, 256);
        builder.pop();

        builder.push("Filtering");
        DEFAULT_BEHAVIOR = builder.defineEnum("defaultBehavior", ModConfig.DEFAULT_BEHAVIOR);
        TABS_BLACKLIST = builder.defineList("tabsBlacklist", Lists.newArrayList(), it -> it instanceof String);
        ITEMS_BLACKLIST = builder.defineList("itemsBlacklist", Lists.newArrayList(), it -> it instanceof String);
        TABS_WHITELIST = builder.defineList("tabsWhitelist", Lists.newArrayList(), it -> it instanceof String);
        ITEMS_WHITELIST = builder.defineList("itemsWhitelist", Lists.newArrayList(), it -> it instanceof String);
        builder.pop();

        SPEC = builder.build();
    }
}
