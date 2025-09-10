package dev.obscuria.lootjournal.fabric;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.ModConfig;
import dev.obscuria.lootjournal.ModConfigDefaults;
import dev.obscuria.lootjournal.client.ItemPolicy;
import dev.obscuria.lootjournal.client.render.Anchor;
import dev.obscuria.lootjournal.client.render.PickupDrawStyle;
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
    @ConfigEntry.Gui.CollapsibleObject
    public Display display = new Display();
    @ConfigEntry.Gui.CollapsibleObject
    public Layout layout = new Layout();
    @ConfigEntry.Gui.CollapsibleObject
    public Filter filter = new Filter();

    public static void init()
    {
        final var configHolder = AutoConfig.register(FabricConfig.class, Toml4jConfigSerializer::new);
        configHolder.registerLoadListener(FabricConfig::onUpdate);
        configHolder.registerSaveListener(FabricConfig::onUpdate);
        configHolder.load();
    }

    public static InteractionResult onUpdate(ConfigHolder<FabricConfig> holder, FabricConfig config)
    {
        LootJournal.CONFIG.style = config.display.style;

        LootJournal.CONFIG.itemEntryDisplay = config.display.itemEntry.display;
        LootJournal.CONFIG.itemEntryDisplayTotalAmount = config.display.itemEntry.displayTotalAmount;
        LootJournal.CONFIG.itemEntryUseItemFormatting = config.display.itemEntry.useItemFormatting;
        LootJournal.CONFIG.itemEntryStyle = ModConfig.createStyle(
                config.display.itemEntry.color,
                config.display.itemEntry.italic);

        LootJournal.CONFIG.aggregatedEntryDisplay = config.display.aggregatedEntry.display;
        LootJournal.CONFIG.aggregatedEntryStyle = ModConfig.createStyle(
                config.display.aggregatedEntry.color,
                config.display.aggregatedEntry.italic);

        LootJournal.CONFIG.experienceEntryDisplay = config.display.experienceEntry.display;
        LootJournal.CONFIG.experienceEntryStyle = ModConfig.createStyle(
                config.display.experienceEntry.color,
                config.display.experienceEntry.italic);

        LootJournal.CONFIG.anchor = config.layout.anchor;
        LootJournal.CONFIG.anchorPixelOffset = config.layout.anchorPixelOffset;
        LootJournal.CONFIG.anchorPercentOffset = config.layout.anchorPercentOffset;
        LootJournal.CONFIG.scale = config.layout.scale;
        LootJournal.CONFIG.separation = config.layout.separation;
        LootJournal.CONFIG.lifetime = config.layout.lifetime;
        LootJournal.CONFIG.displayCapacity = config.layout.displayCapacity;
        LootJournal.CONFIG.queueCapacity = config.layout.queueCapacity;

        LootJournal.CONFIG.defaultItemPolicy = config.filter.defaultItemPolicy;
        LootJournal.CONFIG.itemIdBlacklist = ModConfig.mapItems(config.filter.itemIdBlacklist);
        LootJournal.CONFIG.itemIdWhitelist = ModConfig.mapItems(config.filter.itemIdWhitelist);
        LootJournal.CONFIG.modIdBlacklist = config.filter.modIdBlacklist;
        LootJournal.CONFIG.modIdWhitelist = config.filter.modIdWhitelist;

        return InteractionResult.SUCCESS;
    }

    public static final class Display
    {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public PickupDrawStyle style = ModConfigDefaults.STYLE;
        @ConfigEntry.Gui.CollapsibleObject
        public ItemEntry itemEntry = new ItemEntry();
        @ConfigEntry.Gui.CollapsibleObject
        public AggregatedEntry aggregatedEntry = new AggregatedEntry();
        @ConfigEntry.Gui.CollapsibleObject
        public ExperienceEntry experienceEntry = new ExperienceEntry();

        public static final class ItemEntry
        {
            public boolean display = ModConfigDefaults.ITEM_ENTRY_DISPLAY;
            public boolean displayTotalAmount = ModConfigDefaults.ITEM_ENTRY_DISPLAY_TOTAL_AMOUNT;
            public boolean useItemFormatting = ModConfigDefaults.ITEM_ENTRY_USE_ITEM_FORMATTING;
            @ConfigEntry.ColorPicker
            public int color = ModConfigDefaults.ITEM_ENTRY_COLOR;
            public boolean italic = ModConfigDefaults.ITEM_ENTRY_ITALIC;
        }

        public static final class AggregatedEntry
        {
            public boolean display = ModConfigDefaults.AGGREGATED_ENTRY_DISPLAY;
            @ConfigEntry.ColorPicker
            public int color = ModConfigDefaults.AGGREGATED_ENTRY_COLOR;
            public boolean italic = ModConfigDefaults.AGGREGATED_ENTRY_ITALIC;
        }

        public static final class ExperienceEntry
        {
            public boolean display = ModConfigDefaults.EXPERIENCE_ENTRY_DISPLAY;
            @ConfigEntry.ColorPicker
            public int color = ModConfigDefaults.EXPERIENCE_ENTRY_COLOR;
            public boolean italic = ModConfigDefaults.EXPERIENCE_ENTRY_ITALIC;
        }
    }

    public static final class Layout
    {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public Anchor anchor = Anchor.BOTTOM_RIGHT;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 200)
        public int anchorPixelOffset = ModConfigDefaults.ANCHOR_PIXEL_OFFSET;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 50)
        public int anchorPercentOffset = ModConfigDefaults.ANCHOR_PERCENT_OFFSET;
        @ConfigEntry.BoundedDiscrete(min = 50, max = 100)
        public int scale = ModConfigDefaults.SCALE;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 16)
        public int separation = ModConfigDefaults.SEPARATION;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 32)
        public int lifetime = ModConfigDefaults.LIFETIME;
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64)
        public int displayCapacity = ModConfigDefaults.DISPLAY_CAPACITY;
        @ConfigEntry.BoundedDiscrete(min = 0, max = 256)
        public int queueCapacity = ModConfigDefaults.QUEUE_CAPACITY;
    }

    public static final class Filter
    {
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public ItemPolicy defaultItemPolicy = ModConfigDefaults.DEFAULT_ITEM_POLICY;
        public List<String> itemIdBlacklist = Lists.newArrayList();
        public List<String> itemIdWhitelist = Lists.newArrayList();
        public List<String> modIdBlacklist = Lists.newArrayList();
        public List<String> modIdWhitelist = Lists.newArrayList();
    }
}
