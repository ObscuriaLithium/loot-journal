package dev.obscuria.lootjournal.config;

import dev.obscuria.fragmentum.config.ConfigBuilder;
import dev.obscuria.fragmentum.config.ConfigValue;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.DefaultFilterRule;
import dev.obscuria.lootjournal.client.renderer.GrowthDirection;
import dev.obscuria.lootjournal.client.renderer.MergeMode;
import dev.obscuria.lootjournal.client.renderer.ScreenAnchor;
import dev.obscuria.lootjournal.client.renderer.StackingMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class Config {

    public static final ConfigValue<Boolean> ENABLE_LOOT_JOURNAL;
    public static final ConfigValue<Boolean> SHOW_ITEM_PICKUPS;
    public static final ConfigValue<Boolean> SHOW_XP_PICKUPS;
    public static final ConfigValue<Boolean> SHOW_OVERFLOW_PICKUPS;
    public static final ConfigValue<Boolean> ABBREVIATE_NUMBERS;
    public static final ConfigValue<MergeMode> MERGE_MODE;
    public static final ConfigValue<StackingMode> STACKING_MODE;
    public static final ConfigValue<Integer> MAX_NAME_WIDTH;
    public static final ConfigValue<Double> DISPLAY_TIME;
    public static final ConfigValue<Integer> DISPLAY_CAPACITY;
    public static final ConfigValue<Integer> QUEUE_SIZE;

    public static final ConfigValue<String> THEME;
    public static final ConfigValue<ScreenAnchor> SCREEN_ANCHOR;
    public static final ConfigValue<GrowthDirection> GROWTH_DIRECTION;
    public static final ConfigValue<Integer> ANCHOR_X_OFFSET;
    public static final ConfigValue<Integer> ANCHOR_Y_OFFSET;
    public static final ConfigValue<Integer> SEPARATION;
    public static final ConfigValue<Double> SCALE;
    public static final ConfigValue<String> ELEMENT_ORDER;
    public static final ConfigValue<Integer> ELEMENT_PADDING_LEFT;
    public static final ConfigValue<Integer> ELEMENT_PADDING_RIGHT;
    public static final ConfigValue<Integer> ELEMENT_PADDING_TOP;
    public static final ConfigValue<Integer> ELEMENT_PADDING_BOTTOM;

    public static final ConfigValue<Double> FADE_IN_TIME;
    public static final ConfigValue<Double> FADE_OUT_TIME;
    public static final ConfigValue<Easing> FADE_IN_EASING;
    public static final ConfigValue<Easing> FADE_OUT_EASING;
    public static final ConfigValue<Double> PULSE_STRENGTH;
    public static final ConfigValue<Double> PULSE_TIME;
    public static final ConfigValue<Double> PULSE_PEAK;
    public static final ConfigValue<Easing> PULSE_EASE_IN;
    public static final ConfigValue<Easing> PULSE_EASE_OUT;
    public static final ConfigValue<Boolean> RAY_GLOW_ENABLED;

    public static final ConfigValue<Boolean> TRACK_ITEM_PICKUPS;
    public static final ConfigValue<Boolean> TRACK_XP_PICKUPS;
    public static final ConfigValue<Boolean> ENABLE_PLAYER_FILTERING;
    public static final ConfigValue<List<? extends String>> PLAYER_WHITELIST;

    public static final ConfigValue<Boolean> ENABLE_SOUNDS;
    public static final ConfigValue<String> SOUND_ID;
    public static final ConfigValue<Double> SOUND_VOLUME;
    public static final ConfigValue<Double> SOUND_PITCH;

    public static final ConfigValue<Boolean> ENABLE_FILTERING;
    public static final ConfigValue<Boolean> HIDE_YOUR_COMMON_ITEMS;
    public static final ConfigValue<Boolean> HIDE_OTHER_COMMON_ITEMS;
    public static final ConfigValue<DefaultFilterRule> DEFAULT_FILTER_RULE;
    public static final ConfigValue<List<? extends String>> ITEM_ID_BLACKLIST;
    public static final ConfigValue<List<? extends String>> ITEM_ID_WHITELIST;
    public static final ConfigValue<List<? extends String>> ITEM_TAG_BLACKLIST;
    public static final ConfigValue<List<? extends String>> ITEM_TAG_WHITELIST;
    public static final ConfigValue<List<? extends String>> MOD_ID_BLACKLIST;
    public static final ConfigValue<List<? extends String>> MOD_ID_WHITELIST;

    public static final HashSet<ConfigValue<?>> VALUES;

    public static void init() {}

    static {
        final var builder = new ConfigBuilder("obscuria/loot_journal-client.toml");

        builder.comment(
                "===========[ Loot Journal Client Config ]===========",
                " Please prefer editing the config through the in-game ",
                " configuration screen (available via the mods list),",
                " as it includes many hints and quality-of-life improvements.",
                "====================================================");

        ENABLE_LOOT_JOURNAL = builder.defineBoolean("enableLootJournal", true);
        SHOW_ITEM_PICKUPS = builder.defineBoolean("showItemPickups", true);
        SHOW_XP_PICKUPS = builder.defineBoolean("showXpPickups", true);
        SHOW_OVERFLOW_PICKUPS = builder.defineBoolean("showOverflowPickups", true);
        ABBREVIATE_NUMBERS = builder.defineBoolean("abbreviateNumbers", true);
        MERGE_MODE = builder.DefineEnum("mergeMode", MergeMode.TYPE_NAMED);
        STACKING_MODE = builder.DefineEnum("stackingMode", StackingMode.SMOOTH_FLOW);
        MAX_NAME_WIDTH = builder.defineInt("maxNameWidth", 100, 0, 500);
        DISPLAY_TIME = builder.defineDouble("displayTime", 4.0, 1.0, 60.0);
        DISPLAY_CAPACITY = builder.defineInt("displayCapacity", 9, 1, 64);
        QUEUE_SIZE = builder.defineInt("queueSize", 9, 0, 256);

        THEME = builder.defineString("activeTheme", "Classic");

        SCREEN_ANCHOR = builder.DefineEnum("screenAnchor", ScreenAnchor.BOTTOM_RIGHT);
        GROWTH_DIRECTION = builder.DefineEnum("growthDirection", GrowthDirection.NATURAL);
        ANCHOR_X_OFFSET = builder.defineInt("anchorXOffset", 0, -256, 256);
        ANCHOR_Y_OFFSET = builder.defineInt("anchorYOffset", 32, -256, 256);
        SEPARATION = builder.defineInt("separation", 2, 0, 16);
        SCALE = builder.defineDouble("scale", 1.0, 0.1, 3.0);
        ELEMENT_ORDER = builder.defineString("elementOrder", "COUNT 4px NAME 4px ICON 4px TOTAL");
        ELEMENT_PADDING_LEFT = builder.defineInt("elementPaddingLeft", 0, 0, 32);
        ELEMENT_PADDING_RIGHT = builder.defineInt("elementPaddingRight", 0, 0, 32);
        ELEMENT_PADDING_TOP = builder.defineInt("elementPaddingTop", 0, 0, 16);
        ELEMENT_PADDING_BOTTOM = builder.defineInt("elementPaddingBottom", 0, 0, 16);

        FADE_IN_TIME = builder.defineDouble("fadeInTime", 0.8, 0.0, 5.0);
        FADE_OUT_TIME = builder.defineDouble("fadeOutTime", 1.0, 0.0, 5.0);
        FADE_IN_EASING = builder.DefineEnum("fadeInEasing", Easing.EASE_OUT_BACK);
        FADE_OUT_EASING = builder.DefineEnum("fadeOutEasing", Easing.EASE_IN_CUBIC);
        PULSE_STRENGTH = builder.defineDouble("pulseStrength", 1.0, 0.0, 10.0);
        PULSE_TIME = builder.defineDouble("pulseTime", 0.5, 0.0, 5.0);
        PULSE_PEAK = builder.defineDouble("pulsePeak", 0.1, 0.0, 1.0);
        PULSE_EASE_IN = builder.DefineEnum("pulseEaseIn", Easing.EASE_OUT_CUBIC);
        PULSE_EASE_OUT = builder.DefineEnum("pulseEaseOut", Easing.EASE_OUT_ELASTIC);
        RAY_GLOW_ENABLED = builder.defineBoolean("enableRayGlow", true);

        TRACK_ITEM_PICKUPS = builder.defineBoolean("trackItemPickups", false);
        TRACK_XP_PICKUPS = builder.defineBoolean("trackXpPickups", false);
        ENABLE_PLAYER_FILTERING = builder.defineBoolean("enablePlayerFiltering", false);
        PLAYER_WHITELIST = builder.defineList("playerWhitelist", new ArrayList<>(), () -> "");

        ENABLE_SOUNDS = builder.defineBoolean("enableSounds", true);
        SOUND_ID = builder.defineString("soundId", "minecraft:entity.item.pickup");
        SOUND_VOLUME = builder.defineDouble("soundVolume", 1.0, 0.0, 1.0);
        SOUND_PITCH = builder.defineDouble("soundPitch", 1.0, 0.0, 2.0);

        ENABLE_FILTERING = builder.defineBoolean("enableFiltering", false);
        HIDE_YOUR_COMMON_ITEMS = builder.defineBoolean("hideYourCommonItems", false);
        HIDE_OTHER_COMMON_ITEMS = builder.defineBoolean("hideOtherCommonItems", false);
        DEFAULT_FILTER_RULE = builder.DefineEnum("defaultFilterRule", DefaultFilterRule.ALLOW_ALL);
        ITEM_ID_BLACKLIST = builder.defineList("itemIdBlacklist", new ArrayList<>(), () -> "");
        ITEM_ID_WHITELIST = builder.defineList("itemIdWhitelist", new ArrayList<>(), () -> "");
        ITEM_TAG_BLACKLIST = builder.defineList("itemTagBlacklist", new ArrayList<>(), () -> "");
        ITEM_TAG_WHITELIST = builder.defineList("itemTagWhitelist", new ArrayList<>(), () -> "");
        MOD_ID_BLACKLIST = builder.defineList("modIdBlacklist", new ArrayList<>(), () -> "");
        MOD_ID_WHITELIST = builder.defineList("modIdWhitelist", new ArrayList<>(), () -> "");

        builder.buildClient(LootJournal.MOD_ID);
        VALUES = builder.collectValues();
    }
}
