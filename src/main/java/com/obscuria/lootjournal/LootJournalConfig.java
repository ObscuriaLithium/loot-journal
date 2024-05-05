package com.obscuria.lootjournal;

import com.obscuria.lootjournal.client.renderer.Anchor;
import com.obscuria.lootjournal.client.renderer.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class LootJournalConfig {

    public static final ForgeConfigSpec clientSpec;

    public static final ForgeConfigSpec.EnumValue<Style> style;
    public static final ForgeConfigSpec.EnumValue<Anchor> anchor;
    public static final ForgeConfigSpec.IntValue offset;
    public static final ForgeConfigSpec.IntValue separation;
    public static final ForgeConfigSpec.DoubleValue duration;
    public static final ForgeConfigSpec.IntValue displayCapacity;
    public static final ForgeConfigSpec.IntValue queueCapacity;
    public static final ForgeConfigSpec.BooleanValue total;

    public static final ForgeConfigSpec.BooleanValue blacklistedByDefault;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> tabsBlacklist;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> itemsBlacklist;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> tabsWhitelist;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> itemsWhitelist;

    static {
        final var builder = new ForgeConfigSpec.Builder();
        builder.push("Display");
        style = builder.worldRestart()
                .comment("Notification display style")
                .defineEnum("Style", Style.DEFAULT);
        anchor = builder.worldRestart()
                .comment("Notification display position")
                .defineEnum("Anchor", Anchor.BOTTOM_RIGHT);
        offset = builder.worldRestart()
                .comment("Vertical offset (in pixels) relative to the anchor")
                .defineInRange("Offset", 3, 0, 10000);
        separation = builder.worldRestart()
                .comment("Distance (in pixels) between notifications")
                .defineInRange("Separation", 3, 0, 10);
        duration = builder.worldRestart()
                .comment("Duration (in seconds) of notifications display")
                .defineInRange("Duration", 6.0, 1.0, 20.0);
        displayCapacity = builder.worldRestart()
                .comment("Maximum amount of notifications displayed simultaneously")
                .defineInRange("DisplayCapacity", 12, 1, 64);
        queueCapacity = builder.worldRestart()
                .comment("Maximum amount of queued notifications")
                .defineInRange("QueueCapacity", 24, 0, 256);
        total = builder.worldRestart()
                .comment("Display the total amount of the same items in your inventory")
                .define("CountTotal", true);
        builder.pop();
        builder.push("Filtering");
        blacklistedByDefault = builder.worldRestart()
                .comment("Make all items blacklisted by default")
                .define("BlacklistedByDefault", false);
        tabsBlacklist = builder.worldRestart()
                .comment("1.19.3+ ONLY / Creative tabs whose items will be ignored")
                .defineList("TabsBlacklist", List.of("mod_id:some_tab_id", "mod_id:other_tab_id"), it -> it instanceof String);
        itemsBlacklist = builder.worldRestart()
                .comment("Items that will be ignored")
                .defineList("ItemsBlacklist", List.of("mod_id:some_item_id", "mod_id:other_item_id"), it -> it instanceof String);
        tabsWhitelist = builder.worldRestart()
                .comment("1.19.3+ ONLY / Creative tabs whose items will be shown (even if they are blacklisted)")
                .defineList("TabsWhitelist",  List.of(), it -> it instanceof String);
        itemsWhitelist = builder.worldRestart()
                .comment("Items that will be shown (even if they are blacklisted)")
                .defineList("ItemsWhitelist", List.of(), it -> it instanceof String);
        builder.pop();
        clientSpec = builder.build();
    }

    public static void setup() {
        var obscuria = Paths.get(FMLPaths.CONFIGDIR.get().toAbsolutePath().toString(), "Obscuria");
        try {
            Files.createDirectory(obscuria);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) {
            LootJournal.LOGGER.error("Failed to create Obscuria config directory", e);
        }
        ModLoadingContext.get().registerConfig(
                ModConfig.Type.CLIENT, clientSpec,
                "Obscuria/loot-journal-client.toml");
    }
}

