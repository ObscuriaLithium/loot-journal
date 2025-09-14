package dev.obscuria.lootjournal.forge;

import dev.obscuria.lootjournal.ModConfig;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.ModConfigDefaults;
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
    private static final ForgeConfigSpec.BooleanValue ITEM_ENTRY_DISPLAY;
    private static final ForgeConfigSpec.BooleanValue ITEM_ENTRY_DISPLAY_TOTAL_AMOUNT;
    private static final ForgeConfigSpec.BooleanValue ITEM_ENTRY_USE_ITEM_FORMATTING;
    private static final ForgeConfigSpec.ConfigValue<String> ITEM_ENTRY_COLOR;
    private static final ForgeConfigSpec.BooleanValue ITEM_ENTRY_ITALIC;
    private static final ForgeConfigSpec.BooleanValue AGGREGATED_ENTRY_DISPLAY;
    private static final ForgeConfigSpec.ConfigValue<String> AGGREGATED_ENTRY_COLOR;
    private static final ForgeConfigSpec.BooleanValue AGGREGATED_ENTRY_ITALIC;
    private static final ForgeConfigSpec.BooleanValue EXPERIENCE_ENTRY_DISPLAY;
    private static final ForgeConfigSpec.ConfigValue<String> EXPERIENCE_ENTRY_COLOR;
    private static final ForgeConfigSpec.BooleanValue EXPERIENCE_ENTRY_ITALIC;
    private static final ForgeConfigSpec.EnumValue<Anchor> ANCHOR;
    private static final ForgeConfigSpec.IntValue ANCHOR_PIXEL_OFFSET;
    private static final ForgeConfigSpec.IntValue ANCHOR_PERCENT_OFFSET;
    private static final ForgeConfigSpec.IntValue SCALE;
    private static final ForgeConfigSpec.IntValue SEPARATION;
    private static final ForgeConfigSpec.IntValue LIFETIME;
    private static final ForgeConfigSpec.IntValue DISPLAY_CAPACITY;
    private static final ForgeConfigSpec.IntValue QUEUE_CAPACITY;
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

        LootJournal.CONFIG.itemEntryDisplay = ITEM_ENTRY_DISPLAY.get();
        LootJournal.CONFIG.itemEntryDisplayTotalAmount = ITEM_ENTRY_DISPLAY_TOTAL_AMOUNT.get();
        LootJournal.CONFIG.itemEntryUseItemFormatting = ITEM_ENTRY_USE_ITEM_FORMATTING.get();
        LootJournal.CONFIG.itemEntryStyle = ModConfig.createStyle(
                ITEM_ENTRY_COLOR.get(),
                ITEM_ENTRY_ITALIC.get());

        LootJournal.CONFIG.aggregatedEntryDisplay = AGGREGATED_ENTRY_DISPLAY.get();
        LootJournal.CONFIG.aggregatedEntryStyle = ModConfig.createStyle(
                AGGREGATED_ENTRY_COLOR.get(),
                AGGREGATED_ENTRY_ITALIC.get());

        LootJournal.CONFIG.experienceEntryDisplay = EXPERIENCE_ENTRY_DISPLAY.get();
        LootJournal.CONFIG.experienceEntryStyle = ModConfig.createStyle(
                EXPERIENCE_ENTRY_COLOR.get(),
                EXPERIENCE_ENTRY_ITALIC.get());

        LootJournal.CONFIG.anchor = ANCHOR.get();
        LootJournal.CONFIG.anchorPixelOffset = ANCHOR_PIXEL_OFFSET.get();
        LootJournal.CONFIG.anchorPercentOffset = ANCHOR_PERCENT_OFFSET.get();
        LootJournal.CONFIG.scale = SCALE.get();
        LootJournal.CONFIG.separation = SEPARATION.get();
        LootJournal.CONFIG.lifetime = LIFETIME.get();
        LootJournal.CONFIG.displayCapacity = DISPLAY_CAPACITY.get();
        LootJournal.CONFIG.queueCapacity = QUEUE_CAPACITY.get();

        LootJournal.CONFIG.defaultItemPolicy = DEFAULT_ITEM_POLICY.get();
        LootJournal.CONFIG.itemIdBlacklist = ModConfig.mapItems(ITEM_ID_BLACKLIST.get());
        LootJournal.CONFIG.itemIdWhitelist = ModConfig.mapItems(ITEM_ID_WHITELIST.get());
        LootJournal.CONFIG.modIdBlacklist = new ArrayList<>(MOD_ID_BLACKLIST.get());
        LootJournal.CONFIG.modIdWhitelist = new ArrayList<>(MOD_ID_WHITELIST.get());
    }

    static
    {
        final var builder = new ForgeConfigSpec.Builder();

        builder.push("Display Options");
        STYLE = builder.defineEnum("Message Style", ModConfigDefaults.STYLE);

        builder.push("Item Pickup");
        ITEM_ENTRY_DISPLAY = builder.define("Show Item Pickups", ModConfigDefaults.ITEM_ENTRY_DISPLAY);
        ITEM_ENTRY_DISPLAY_TOTAL_AMOUNT = builder.define("Show Total Item Count", ModConfigDefaults.ITEM_ENTRY_DISPLAY_TOTAL_AMOUNT);
        ITEM_ENTRY_USE_ITEM_FORMATTING = builder.define("Use Minecraft Formatting", ModConfigDefaults.ITEM_ENTRY_USE_ITEM_FORMATTING);
        ITEM_ENTRY_COLOR = builder.define("Text Color", ModConfig.intToHex(ModConfigDefaults.ITEM_ENTRY_COLOR));
        ITEM_ENTRY_ITALIC = builder.define("Italic Text", ModConfigDefaults.ITEM_ENTRY_ITALIC);
        builder.pop();

        builder.push("Aggregated Pickup");
        AGGREGATED_ENTRY_DISPLAY = builder.define("Show Aggregated Pickups", ModConfigDefaults.AGGREGATED_ENTRY_DISPLAY);
        AGGREGATED_ENTRY_COLOR = builder.define("Text Color", ModConfig.intToHex(ModConfigDefaults.AGGREGATED_ENTRY_COLOR));
        AGGREGATED_ENTRY_ITALIC = builder.define("Italic Text", ModConfigDefaults.AGGREGATED_ENTRY_ITALIC);
        builder.pop();

        builder.push("Experience Pickup");
        EXPERIENCE_ENTRY_DISPLAY = builder.define("Show Experience Pickups", ModConfigDefaults.EXPERIENCE_ENTRY_DISPLAY);
        EXPERIENCE_ENTRY_COLOR = builder.define("Text Color", ModConfig.intToHex(ModConfigDefaults.EXPERIENCE_ENTRY_COLOR));
        EXPERIENCE_ENTRY_ITALIC = builder.define("Italic Text", ModConfigDefaults.EXPERIENCE_ENTRY_ITALIC);
        builder.pop();

        builder.pop();

        builder.push("Layout Options");
        ANCHOR = builder.defineEnum("Screen Position", ModConfigDefaults.ANCHOR);
        ANCHOR_PIXEL_OFFSET = builder.defineInRange("Offset", ModConfigDefaults.ANCHOR_PIXEL_OFFSET, 0, 200);
        ANCHOR_PERCENT_OFFSET = builder.defineInRange("Relative Offset", ModConfigDefaults.ANCHOR_PERCENT_OFFSET, 0, 50);
        SCALE = builder.defineInRange("Message Scale", ModConfigDefaults.SCALE, 50, 100);
        SEPARATION = builder.defineInRange("Line Spacing", ModConfigDefaults.SEPARATION, 0, 16);
        LIFETIME = builder.defineInRange("Message Duration", ModConfigDefaults.LIFETIME, 0, 32);
        DISPLAY_CAPACITY = builder.defineInRange("Max Visible Messages", ModConfigDefaults.DISPLAY_CAPACITY, 1, 64);
        QUEUE_CAPACITY = builder.defineInRange("Message Queue Size", ModConfigDefaults.QUEUE_CAPACITY, 0, 256);
        builder.pop();

        builder.push("Filter Options");
        DEFAULT_ITEM_POLICY = builder.defineEnum("Default Filter Rule", ModConfigDefaults.DEFAULT_ITEM_POLICY);
        ITEM_ID_BLACKLIST = builder.defineList("Item ID Blacklist", Lists.newArrayList(), it -> it instanceof String);
        ITEM_ID_WHITELIST = builder.defineList("Item ID Whitelist", Lists.newArrayList(), it -> it instanceof String);
        MOD_ID_BLACKLIST = builder.defineList("Mod ID Blacklist", Lists.newArrayList(), it -> it instanceof String);
        MOD_ID_WHITELIST = builder.defineList("Mod ID Whitelist", Lists.newArrayList(), it -> it instanceof String);
        builder.pop();

        SPEC = builder.build();
    }
}
