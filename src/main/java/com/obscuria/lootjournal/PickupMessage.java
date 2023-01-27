package com.obscuria.lootjournal;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PickupMessage {
    ItemStack stack;

    public PickupMessage(ItemStack stack) {
        this.stack = stack;
    }

    public PickupMessage(FriendlyByteBuf buffer) {
        this.stack = buffer.readItem();
    }

    public static void buffer(PickupMessage message, FriendlyByteBuf buffer) {
        buffer.writeItem(message.stack);
    }

    public static void handler(PickupMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> clientHandler(message.stack));
        context.setPacketHandled(true);
    }

    public static void clientHandler(ItemStack stack) {
        PickupHandler.addPickup(stack);
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        LootJournal.addNetworkMessage(PickupMessage.class, PickupMessage::buffer, PickupMessage::new, PickupMessage::handler);
    }
}
