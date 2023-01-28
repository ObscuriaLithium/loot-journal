package com.obscuria.lootjournal;

import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@OnlyIn(Dist.CLIENT)
public class LootJournalConfig {

    public static class Client {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec CLIENT_SPEC;
        public static final ForgeConfigSpec.EnumValue<PickupStyle> style;
        public static final ForgeConfigSpec.IntValue lifetime;
        public static final ForgeConfigSpec.IntValue offset;
        public static final ForgeConfigSpec.BooleanValue useCustomColor;
        public static final ForgeConfigSpec.EnumValue<ChatFormatting> customColor;

        static {
            BUILDER.push("PickupNotifier");
            style = BUILDER.worldRestart().defineEnum("Style", PickupStyle.COMMON);
            lifetime = BUILDER.worldRestart().defineInRange("Lifetime", 5, 0, 20);
            offset = BUILDER.worldRestart().defineInRange("Offset", 0, 0, 1080);
            useCustomColor = BUILDER.worldRestart().comment("If false, the item's rarity color will be used").define("UseCustomColor", false);
            customColor = BUILDER.worldRestart().defineEnum("CustomColor", ChatFormatting.WHITE);
            BUILDER.pop();
            CLIENT_SPEC = BUILDER.build();
        }
    }

    public static void load() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "Obscuria");
        try { Files.createDirectory(modConfigPath); }
        catch (FileAlreadyExistsException ignored) {}
        catch (IOException e) { LootJournalMod.LOGGER.error("Failed to create Obscuria config directory", e); }
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Client.CLIENT_SPEC, "Obscuria/loot-journal-client.toml");
    }

    public enum PickupStyle {
        COMMON, WITHOUT_BACKGROUND;
    }
}

