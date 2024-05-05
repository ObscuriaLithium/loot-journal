package com.obscuria.lootjournal.client.renderer;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.vertex.PoseStack;
import com.obscuria.lootjournal.LootJournalConfig;
import com.obscuria.lootjournal.client.Filtering;
import com.obscuria.lootjournal.client.pickup.ItemPickup;
import com.obscuria.lootjournal.client.pickup.MoreItemsPickup;
import com.obscuria.lootjournal.client.pickup.Pickup;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.ApiStatus;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class PickupComponent {
    private static final HashMap<Integer, PickupInstance> occupiedSlots = Maps.newHashMap();
    private static final List<PickupInstance> visiblePickups = Lists.newArrayList();
    private static final Deque<Pickup> queued = Queues.newArrayDeque();

    public static void append(int itemId, int playerId, int amount) {
        final Player player = Minecraft.getInstance().player;
        if (player != null
                && player.getId() == playerId
                && player.level.getEntity(itemId) instanceof ItemEntity entity) {
            final ItemStack stack = entity.getItem().copy();
            stack.setCount(amount);
            append(stack);
        }
    }

    public static void append(ItemStack stack) {
        if (!Filtering.isAllowed(stack)) return;
        final var displayCapacity = LootJournalConfig.displayCapacity.get();
        final var queueCapacity = LootJournalConfig.queueCapacity.get();
        if (queueCapacity > 0) {
            final var pickup = queued.size() < queueCapacity
                    ? ItemPickup.of(stack)
                    : MoreItemsPickup.of(stack);
            appendOrQueue(pickup);
        } else {
            final var pickup = visiblePickups.size() < displayCapacity
                    ? ItemPickup.of(stack)
                    : MoreItemsPickup.of(stack);
            appendNoQueue(pickup);
        }
    }

    private static void appendOrQueue(Pickup pickup) {
        if (merge(pickup)) return;
        final var index = findFreeIndex(LootJournalConfig.displayCapacity.get());
        if (index > -1) {
            final var instance = new PickupInstance(pickup, index);
            visiblePickups.add(instance);
            occupiedSlots.put(index, instance);
        } else queued.add(pickup);
    }

    private static void appendNoQueue(Pickup pickup) {
        if (merge(pickup)) return;
        final var index = findFreeIndex(256);
        if (index > -1) {
            final var instance = new PickupInstance(pickup, index);
            visiblePickups.add(instance);
            occupiedSlots.put(index, instance);
        }
    }

    private static boolean merge(Pickup pickup) {
        for (PickupInstance instance : visiblePickups)
            if (instance.merge(pickup))
                return true;
        for (Pickup other : queued)
            if (other.merge(pickup))
                return true;
        return false;
    }

    private static int findFreeIndex(int size) {
        for (int i = 0; i < size; i++)
            if (!occupiedSlots.containsKey(i))
                return i;
        return -1;
    }

    @ApiStatus.Internal
    public static void render(PoseStack pose) {
        final var window = Minecraft.getInstance().getWindow();
        final var capacity = LootJournalConfig.displayCapacity.get();
        final var style = LootJournalConfig.style.get();
        final var anchor = LootJournalConfig.anchor.get();
        final var originX = anchor.originX(window);
        final var originY = anchor.originY(window);
        final var step = anchor.step();
        visiblePickups.removeIf(instance -> {
            if (instance.render(pose, style, anchor, originX, originY + step * instance.index)) {
                occupiedSlots.remove(instance.index);
                return true;
            }
            return false;
        });

        if (queued.isEmpty() || occupiedSlots.size() >= capacity) return;
        queued.removeIf(pickup -> {
            final var index = findFreeIndex(capacity);
            if (index > -1) {
                final var instance = new PickupInstance(pickup, index);
                visiblePickups.add(instance);
                occupiedSlots.put(index, instance);
                return true;
            }
            return false;
        });
    }

    private static class PickupInstance {
        private static final long FADE_IN = 750L;
        private static final long FADE_OUT = 1500L;
        private final Pickup pickup;
        private long startTime = -1L;
        private long lastTime;
        private double delta;
        private double factor;
        int index;

        PickupInstance(Pickup pickup, int index) {
            this.pickup = pickup;
            this.index = index;
        }

        boolean render(PoseStack pose, Style style, Anchor anchor, int x, int y) {
            final var currentTime = Util.getMillis();
            if (startTime < 0L) {
                startTime = currentTime;
                lastTime = currentTime;
            }
            delta = (double) (currentTime - lastTime) / 1000.0;
            lastTime = currentTime;

            final var time = currentTime - startTime;
            if (!Minecraft.getInstance().options.hideGui)
                style.render(pickup, pose, anchor, x, y, factor, time);
            updateFactor(time);
            return time > getMaxTime();
        }

        boolean merge(Pickup other) {
            if (pickup.merge(other)) {
                startTime = Util.getMillis();
                return true;
            }
            return pickup.merge(other);
        }

        private void updateFactor(long time) {
            if (time <= FADE_IN) factor = Math.min(factor + delta * 2.0, 1.0);
            if (time >= getMaxTime() - FADE_OUT) factor = Math.max(factor - delta, 0.0);
        }

        private long getDisplayTime() {
            return (long) (1000L * LootJournalConfig.duration.get());
        }

        private long getMaxTime() {
            return FADE_IN + FADE_OUT + getDisplayTime();
        }
    }
}

