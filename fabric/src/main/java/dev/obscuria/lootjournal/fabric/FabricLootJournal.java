package dev.obscuria.lootjournal.fabric;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.registry.PickupResourceManager;
import dev.obscuria.lootjournal.client.renderer.PickupComponent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class FabricLootJournal implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LootJournal.clientInit();
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(ReloadListener.SHARED);
        HudRenderCallback.EVENT.register(PickupComponent::render);
    }

    private static final class ReloadListener implements IdentifiableResourceReloadListener {
        private static final ResourceLocation ID = LootJournal.id("pickup_resources");
        private static final ReloadListener SHARED = new ReloadListener();

        @Override public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
            return PickupResourceManager.SHARED.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
        }

        @Override public ResourceLocation getFabricId() {
            return ID;
        }
    }
}