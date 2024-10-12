package dev.obscuria.lootjournal.forge;

import dev.obscuria.lootjournal.LootJournal;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(LootJournal.MODID)
public class ForgeLootJournal
{
    public ForgeLootJournal()
    {
        final var eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist.isClient())
        {
            ForgeConfig.init(eventBus);
            LootJournal.init();
        }
    }
}