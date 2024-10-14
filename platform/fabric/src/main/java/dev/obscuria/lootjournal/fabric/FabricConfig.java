package dev.obscuria.lootjournal.fabric;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.ModConfig;
import dev.obscuria.lootjournal.client.DefaultBehavior;
import dev.obscuria.lootjournal.client.render.Anchor;
import dev.obscuria.lootjournal.client.render.Style;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.world.InteractionResult;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Config(name = "obscuria/loot_journal-client")
public final class FabricConfig implements ConfigData
{
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Display display = new Display();
    @ConfigEntry.Gui.CollapsibleObject
    public Positioning positioning = new Positioning();
    @ConfigEntry.Gui.CollapsibleObject
    public Filtering filtering = new Filtering();

    public static void init()
    {
        final var configHolder = AutoConfig.register(FabricConfig.class, Toml4jConfigSerializer::new);
        configHolder.registerLoadListener(FabricConfig::onUpdate);
        configHolder.registerSaveListener(FabricConfig::onUpdate);
        configHolder.load();
    }

    public static InteractionResult onUpdate(ConfigHolder<FabricConfig> holder,
                                             FabricConfig config)
    {
        LootJournal.CONFIG.style = config.display.style;
        LootJournal.CONFIG.useRarityColor = config.display.useRarityColor;
        LootJournal.CONFIG.itemsColor = config.display.itemsColor;
        LootJournal.CONFIG.groupedItemsColor = config.display.groupedItemsColor;
        LootJournal.CONFIG.experienceColor = config.display.experienceColor;
        LootJournal.CONFIG.displayExperience = config.display.displayExperience;
        LootJournal.CONFIG.displayTotal = config.display.displayTotal;

        LootJournal.CONFIG.anchor = config.positioning.anchor;
        LootJournal.CONFIG.anchorOffset = config.positioning.anchorOffset;
        LootJournal.CONFIG.notificationSeparation = config.positioning.notificationSeparation;
        LootJournal.CONFIG.notificationLifetime = config.positioning.notificationLifetime;
        LootJournal.CONFIG.maxVisibleNotifications = config.positioning.maxVisibleNotifications;
        LootJournal.CONFIG.maxQueuedNotifications = config.positioning.maxQueuedNotifications;

        LootJournal.CONFIG.defaultBehavior = config.filtering.defaultBehavior;
        LootJournal.CONFIG.tabsBlacklist = ModConfig.mapTabs(config.filtering.tabsBlacklist);
        LootJournal.CONFIG.itemsBlacklist = ModConfig.mapItems(config.filtering.itemsBlacklist);
        LootJournal.CONFIG.tabsWhitelist = ModConfig.mapTabs(config.filtering.tabsWhitelist);
        LootJournal.CONFIG.itemsWhitelist = ModConfig.mapItems(config.filtering.itemsWhitelist);

        return InteractionResult.SUCCESS;
    }

    public static final class Display
    {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Style style = ModConfig.STYLE;
        public boolean useRarityColor = ModConfig.USE_RARITY_COLOR;
        @ConfigEntry.ColorPicker
        public int itemsColor = ModConfig.ITEMS_COLOR;
        @ConfigEntry.ColorPicker
        public int groupedItemsColor = ModConfig.GROUPED_ITEMS_COLOR;
        @ConfigEntry.ColorPicker
        public int experienceColor = ModConfig.EXPERIENCE_COLOR;
        public boolean displayExperience = ModConfig.DISPLAY_EXPERIENCE;
        public boolean displayTotal = ModConfig.DISPLAY_TOTAL;
    }

    public static final class Positioning
    {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Anchor anchor = Anchor.BOTTOM_RIGHT;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 256)
        public int anchorOffset = ModConfig.ANCHOR_OFFSET;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 16)
        public int notificationSeparation = ModConfig.NOTIFICATION_SEPARATION;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 32)
        public int notificationLifetime = ModConfig.NOTIFICATION_LIFETIME;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int maxVisibleNotifications = ModConfig.MAX_VISIBLE_NOTIFICATIONS;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 256)
        public int maxQueuedNotifications = ModConfig.MAX_QUEUED_NOTIFICATIONS;
    }

    public static final class Filtering
    {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public DefaultBehavior defaultBehavior = ModConfig.DEFAULT_BEHAVIOR;
        public List<String> tabsBlacklist = Lists.newArrayList();
        public List<String> itemsBlacklist = Lists.newArrayList();
        public List<String> tabsWhitelist = Lists.newArrayList();
        public List<String> itemsWhitelist = Lists.newArrayList();
    }
}
