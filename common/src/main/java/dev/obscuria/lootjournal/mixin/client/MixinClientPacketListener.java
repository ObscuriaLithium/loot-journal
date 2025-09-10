package dev.obscuria.lootjournal.mixin.client;

import dev.obscuria.lootjournal.client.render.PickupComponent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPacketListener.class)
public abstract class MixinClientPacketListener
{
    @Inject(method = "handleTakeItemEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
                    shift = At.Shift.AFTER))
    public void handleTakeItemEntity_Listener(ClientboundTakeItemEntityPacket packet, CallbackInfo info)
    {
        PickupComponent.appendItem(packet.getItemId(), packet.getPlayerId(), packet.getAmount());
    }
}