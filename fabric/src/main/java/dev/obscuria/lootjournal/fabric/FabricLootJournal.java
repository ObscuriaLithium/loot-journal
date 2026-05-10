package dev.obscuria.lootjournal.fabric;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.registry.PickupResourceManager;
import dev.obscuria.lootjournal.client.renderer.PickupComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;

public final class FabricLootJournal implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LootJournal.clientInit();
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(
                LootJournal.identifier("pickup_resources"),
                PickupResourceManager.SHARED);
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.SUBTITLES,
                LootJournal.identifier("pickup_component"),
                PickupComponent::render);
    }
}