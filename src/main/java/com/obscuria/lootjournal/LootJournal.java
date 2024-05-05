package com.obscuria.lootjournal;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LootJournal.MODID)
public final class LootJournal {
    public static final Logger LOGGER = LogManager.getLogger(LootJournal.class);
    public static final String MODID = "loot_journal";

    public LootJournal() {
        if (FMLEnvironment.dist.isClient())
            LootJournalConfig.setup();
    }
}
