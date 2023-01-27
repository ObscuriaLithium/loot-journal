package com.obscuria.lootjournal;

import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(LootJournalMod.MODID)
public class LootJournalMod {
    public static final Logger LOGGER = LogManager.getLogger(LootJournalMod.class);
    public static final String MODID = "loot_journal";

    public LootJournalMod() {
        LootJournalConfig.load();
    }
}
