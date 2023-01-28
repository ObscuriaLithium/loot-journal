package com.obscuria.lootjournal.client;

import com.obscuria.lootjournal.client.pickup.ItemStackPickup;
import com.obscuria.lootjournal.client.pickup.Pickup;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber({Dist.CLIENT})
public final class PickupHandler {
    private static final List<Pickup> PICKUP_ENTRIES = new ArrayList<>();

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        List.copyOf(PICKUP_ENTRIES).forEach(pickupEntry -> pickupEntry.tick(PICKUP_ENTRIES));
    }

    public static void addFromPacket(int itemID, int playerID, int amount) {
        final Player player = Minecraft.getInstance().player;
        if (player != null && player.getId() == playerID && player.level.getEntity(itemID) instanceof ItemEntity itemEntity) {
            final ItemStack stack = itemEntity.getItem().copy();
            stack.setCount(amount);
            addItemStack(stack);
        }
    }

    public static void addItemStack(ItemStack stack) {
        if (!merge(new ItemStackPickup(stack.copy()))) PICKUP_ENTRIES.add(new ItemStackPickup(stack.copy()));
    }

    public static boolean merge(Pickup pickup) {
        for (Pickup pickupEntry : List.copyOf(PICKUP_ENTRIES)) if (pickupEntry.merge(pickup)) return true;
        return false;
    }

    public static List<Pickup> getList() {
        return List.copyOf(PICKUP_ENTRIES);
    }
}

