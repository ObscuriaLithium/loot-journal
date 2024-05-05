package com.obscuria.lootjournal.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.obscuria.lootjournal.client.renderer.PickupComponent;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
                    shift = At.Shift.AFTER))
    private void renderPickups(float partialTicks, long nanos, boolean flag, CallbackInfo ci) {
        PickupComponent.render(new PoseStack());
    }
}
