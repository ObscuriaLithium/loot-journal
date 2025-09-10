package dev.obscuria.lootjournal.forge;

import dev.obscuria.lootjournal.ModConfig;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.ItemPolicy;
import dev.obscuria.lootjournal.client.render.Anchor;
import dev.obscuria.lootjournal.client.render.PickupDrawStyle;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.config.ModConfig.Type;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

public final class ForgeConfig
{
    private static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.EnumValue<PickupDrawStyle> STYLE;
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

    private static final ForgeConfigSpec.EnumValue<ItemPolicy> DEFAULT_ITEM_POLICY;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_ID_BLACKLIST;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_ID_WHITELIST;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOD_ID_BLACKLIST;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOD_ID_WHITELIST;

    public static void init(IEventBus eventBus)
    {
        eventBus.addListener(ForgeConfig::onUpdate);
        ModLoadingContext.get().registerConfig(Type.CLIENT, SPEC, "obscuria/loot_journal-client.toml");
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
        LootJournal.CONFIG.anchorPixelOffset = ANCHOR_OFFSET.get();
        LootJournal.CONFIG.separation = NOTIFICATION_SEPARATION.get();
        LootJournal.CONFIG.lifetime = NOTIFICATION_LIFETIME.get();
        LootJournal.CONFIG.displayCapacity = MAX_VISIBLE_NOTIFICATIONS.get();
        LootJournal.CONFIG.queueCapacity = MAX_QUEUED_NOTIFICATIONS.get();

        LootJournal.CONFIG.defaultItemPolicy = DEFAULT_ITEM_POLICY.get();
        LootJournal.CONFIG.itemIdBlacklist = ModConfig.mapItems(ITEM_ID_BLACKLIST.get());
        LootJournal.CONFIG.itemIdWhitelist = ModConfig.mapItems(ITEM_ID_WHITELIST.get());
        LootJournal.CONFIG.modIdBlacklist = new ArrayList<>(MOD_ID_BLACKLIST.get());
        LootJournal.CONFIG.modIdWhitelist = new ArrayList<>(MOD_ID_WHITELIST.get());
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

        builder.push("Layout");
        ANCHOR = builder.defineEnum("anchor", ModConfig.ANCHOR);
        ANCHOR_OFFSET = builder.defineInRange("anchorOffset", ModConfig.ANCHOR_OFFSET, 0, 256);
        NOTIFICATION_SEPARATION = builder.defineInRange("notificationSeparation", ModConfig.NOTIFICATION_SEPARATION, 0, 16);
        NOTIFICATION_LIFETIME = builder.defineInRange("notificationLifetime", ModConfig.NOTIFICATION_LIFETIME, 0, 32);
        MAX_VISIBLE_NOTIFICATIONS = builder.defineInRange("maxVisibleNotifications", ModConfig.MAX_VISIBLE_NOTIFICATIONS, 1, 64);
        MAX_QUEUED_NOTIFICATIONS = builder.defineInRange("maxQueuedNotifications", ModConfig.MAX_QUEUED_NOTIFICATIONS, 0, 256);
        builder.pop();

        builder.push("Filter");
        DEFAULT_ITEM_POLICY = builder.defineEnum("defaultItemPolicy", ModConfig.DEFAULT_ITEM_POLICY);
        ITEM_ID_BLACKLIST = builder.defineList("itemIdBlacklist", Lists.newArrayList(), it -> it instanceof String);
        ITEM_ID_WHITELIST = builder.defineList("itemIdWhitelist", Lists.newArrayList(), it -> it instanceof String);
        MOD_ID_BLACKLIST = builder.defineList("modIdBlacklist", Lists.newArrayList(), it -> it instanceof String);
        MOD_ID_WHITELIST = builder.defineList("modIdWhitelist", Lists.newArrayList(), it -> it instanceof String);
        builder.pop();

        SPEC = builder.build();
    }
}
