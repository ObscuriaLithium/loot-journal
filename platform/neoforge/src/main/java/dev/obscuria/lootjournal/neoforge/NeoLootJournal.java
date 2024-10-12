package dev.obscuria.lootjournal.neoforge;

import dev.obscuria.lootjournal.LootJournal;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = LootJournal.MODID, dist = Dist.CLIENT)
public class NeoLootJournal
{
    public NeoLootJournal(IEventBus eventBus,
                          ModContainer container)
    {
        NeoConfig.init(eventBus, container);
        LootJournal.init();
    }
}