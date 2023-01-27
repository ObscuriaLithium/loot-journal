package com.obscuria.lootjournal;

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
        public static final ForgeConfigSpec.BooleanValue pickupDisplay;
        public static final ForgeConfigSpec.BooleanValue pickupStyle;
        public static final ForgeConfigSpec.IntValue pickupLifetime;
        public static final ForgeConfigSpec.IntValue pickupOffset;

        static {
            BUILDER.push("LootNotifier");
            pickupDisplay = BUILDER.worldRestart().define("Display", true);
            pickupStyle = BUILDER.worldRestart().define("Style", true);
            pickupLifetime = BUILDER.worldRestart().defineInRange("Lifetime", 5, 0, 20);
            pickupOffset = BUILDER.worldRestart().defineInRange("Offset", 0, 0, 1080);
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
}

