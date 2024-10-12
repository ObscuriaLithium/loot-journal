package dev.obscuria.lootjournal.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.obscuria.lootjournal.client.render.PickupComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends Player
{
    private MixinLocalPlayer(Level level, BlockPos pos,
                             float yaw, GameProfile profile)
    {
        super(level, pos, yaw, profile);
    }

    @Inject(method = "setExperienceValues", at = @At("HEAD"))
    private void setExperienceValues_Listener(float progress, int total,
                                              int level, CallbackInfo info)
    {
        if (tickCount < 20) return;
        final var difference = total - this.totalExperience;
        if (difference <= 0) return;
        PickupComponent.appendExperience(difference);
    }
}
