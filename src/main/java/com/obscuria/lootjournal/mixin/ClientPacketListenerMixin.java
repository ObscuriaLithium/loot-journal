package com.obscuria.lootjournal.mixin;

import com.obscuria.lootjournal.client.PickupHandler;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPacketListener.class, priority = 1243)
public abstract class ClientPacketListenerMixin implements ClientGamePacketListener {

    @Inject(method = "handleTakeItemEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V", shift = At.Shift.AFTER))
    public void handleTakeItemEntity$invokeEnsureRunningOnSameThread(ClientboundTakeItemEntityPacket packet, CallbackInfo ci) {
        PickupHandler.addFromPacket(packet.getItemId(), packet.getPlayerId(), packet.getAmount());
    }
}
