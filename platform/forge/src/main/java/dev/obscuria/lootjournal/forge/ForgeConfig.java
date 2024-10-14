package dev.obscuria.lootjournal.forge;

import dev.obscuria.lootjournal.ModConfig;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.DefaultBehavior;
import dev.obscuria.lootjournal.client.render.Anchor;
import dev.obscuria.lootjournal.client.render.Style;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public final class ForgeConfig
{
    private static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.EnumValue<Style> STYLE;
    private static final ForgeConfigSpec.BooleanValue USE_RARITY_COLOR;
    private static final ForgeConfigSpec.ConfigValue<String> ITEMS_COLOR;
    private static final ForgeConfigSpec.ConfigValue<String> GROUPED_ITEMS_COLOR;
    private static final ForgeConfigSpec.ConfigValue<String> EXPERIENCE_COLOR;
    private static final ForgeConfigSpec.BooleanValue DISPLAY_EXPERIENCE;
    private static final ForgeConfigSpec.BooleanValue DISPLAY_TOTAL;

    private static final ForgeConfigSpec.EnumValue<Anchor> ANCHOR;
    private static final ForgeConfigSpec.IntValue ANCHOR_OFFSET;
    private static final ForgeConfigSpec.IntValue NOTIFICATION_SEPARATION;
    private static final ForgeConfigSpec.IntValue NOTIFICATION_LIFETIME;
    private static final ForgeConfigSpec.IntValue MAX_VISIBLE_NOTIFICATIONS;
    private static final ForgeConfigSpec.IntValue MAX_QUEUED_NOTIFICATIONS;

    private static final ForgeConfigSpec.EnumValue<DefaultBehavior> DEFAULT_BEHAVIOR;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TABS_BLACKLIST;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEMS_BLACKLIST;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> TABS_WHITELIST;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEMS_WHITELIST;

    public static void init(IEventBus eventBus)
    {
        eventBus.addListener(ForgeConfig::onUpdate);
        ModLoadingContext.get().registerConfig(
                net.minecraftforge.fml.config.ModConfig.Type.CLIENT, SPEC,
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
        final var builder = new ForgeConfigSpec.Builder();

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
