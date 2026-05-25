package dev.obscuria.lootjournal.neoforge;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.registry.PickupResourceManager;
import dev.obscuria.lootjournal.client.renderer.PickupComponent;
import dev.obscuria.lootjournal.config.ConfigBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@Mod(value = LootJournal.MOD_ID, dist = Dist.CLIENT)
public final class NeoForgeLootJournal {

    public NeoForgeLootJournal(IEventBus modBus, ModContainer container) {
        LootJournal.clientInit();
        modBus.addListener(NeoForgeLootJournal::addClientReloadListeners);
        modBus.addListener(NeoForgeLootJournal::registerGuiLayers);
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                NeoForgeLootJournal::createConfigScreen);
    }

    private static void addClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(PickupResourceManager.SHARED);
    }

    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
                VanillaGuiLayers.SELECTED_ITEM_NAME,
                LootJournal.id("pickup_component"),
                PickupComponent::render);
    }

    private static Screen createConfigScreen(ModContainer container, Screen parent) {
        return ConfigBuilder.createConfigScreen(parent);
    }
}