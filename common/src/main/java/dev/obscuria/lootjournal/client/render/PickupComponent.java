package dev.obscuria.lootjournal.client.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.pickup.ExperiencePickup;
import dev.obscuria.lootjournal.client.pickup.ItemPickup;
import dev.obscuria.lootjournal.client.pickup.GroupedItemsPickup;
import dev.obscuria.lootjournal.client.pickup.IPickup;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Deque;
import java.util.HashMap;
import java.util.List;

public final class PickupComponent
{
    private static final HashMap<Integer, PickupInstance> occupied = Maps.newHashMap();
    private static final List<PickupInstance> visible = Lists.newArrayList();
    private static final Deque<IPickup> queued = Queues.newArrayDeque();

    public static void render(GuiGraphics graphics)
    {
        final var window = Minecraft.getInstance().getWindow();
        final var maxVisible = LootJournal.CONFIG.maxVisibleNotifications;
        final var style = LootJournal.CONFIG.style;
        final var anchor = LootJournal.CONFIG.anchor;
        final var originX = anchor.originX(window);
        final var originY = anchor.originY(window);
        final var step = anchor.step();

        visible.removeIf(instance -> {
            if (!instance.render(graphics, style, anchor, originX, originY + step * instance.index)) return false;
            occupied.remove(instance.index);
            return true;
        });

        if (queued.isEmpty() || visible.size() >= maxVisible) return;
        queued.removeIf(pickup -> {
            final var index = findFreeIndex(maxVisible);
            if (index <= -1) return false;
            final var instance = new PickupInstance(pickup, index);
            visible.add(instance);
            occupied.put(index, instance);
            return true;
        });
    }

    public static void appendItem(int itemId, int playerId, int amount)
    {
        final var player = Minecraft.getInstance().player;
        if (player == null || player.getId() != playerId) return;
        if (!(player.level().getEntity(itemId) instanceof ItemEntity entity)) return;
        final var stack = entity.getItem().copy();
        stack.setCount(amount);
        appendItem(stack);
    }

    public static void appendItem(ItemStack stack)
    {
        if (!LootJournal.isAllowed(stack)) return;
        final var maxVisible = LootJournal.CONFIG.maxVisibleNotifications;
        final var maxQueued = LootJournal.CONFIG.maxQueuedNotifications;
        append(visible.size() > maxVisible && queued.size() > maxQueued
                ? new GroupedItemsPickup(stack)
                : new ItemPickup(stack));
    }

    public static void appendExperience(int amount)
    {
        if (!LootJournal.CONFIG.displayExperience) return;
        append(new ExperiencePickup(amount));
    }

    private static void append(IPickup pickup)
    {
        if (tryMerge(pickup)) return;
        final var index = findFreeIndex(LootJournal.CONFIG.maxVisibleNotifications);
        if (index > -1)
        {
            final var instance = new PickupInstance(pickup, index);
            visible.add(instance);
            occupied.put(index, instance);
        }
        else if (queued.size() < LootJournal.CONFIG.maxQueuedNotifications)
        {
            queued.add(pickup);
        }
    }

    private static boolean tryMerge(IPickup pickup)
    {
        for (var instance : visible)
            if (instance.tryMerge(pickup))
                return true;
        for (var other : queued)
            if (other.tryMerge(pickup))
                return true;
        return false;
    }

    private static int findFreeIndex(int size)
    {
        for (var i = 0; i < size; i++)
            if (!occupied.containsKey(i))
                return i;
        return -1;
    }

    private static final class PickupInstance
    {
        private static final long FADE_IN = 750L;
        private static final long FADE_OUT = 1500L;
        private final IPickup pickup;
        private long startTime = -1L;
        private long lastTime;
        private double delta;
        private double ratio;
        public int index;

        public PickupInstance(IPickup pickup, int index)
        {
            this.pickup = pickup;
            this.index = index;
        }

        public boolean render(GuiGraphics graphics, Style style, Anchor anchor, int x, int y)
        {
            final var currentTime = Util.getMillis();
            if (startTime < 0L)
            {
                startTime = currentTime;
                lastTime = currentTime;
            }
            delta = (double) (currentTime - lastTime) / 1000.0;
            lastTime = currentTime;

            final var time = currentTime - startTime;
            if (!Minecraft.getInstance().options.hideGui)
                style.render(pickup, graphics, anchor, x, y, ratio, time);
            this.updateRatio(time);
            return time > getDisplayTime();
        }

        public boolean tryMerge(IPickup other)
        {
            if (pickup.tryMerge(other))
            {
                startTime = Util.getMillis();
                return true;
            }
            return pickup.tryMerge(other);
        }

        private void updateRatio(long time)
        {
            if (time <= FADE_IN) ratio = Math.min(ratio + delta * 2.0, 1.0);
            if (time >= getDisplayTime() - FADE_OUT) ratio = Math.max(ratio - delta, 0.0);
        }

        private long getLifetime()
        {
            return (long) (1000L * LootJournal.CONFIG.notificationLifetime);
        }

        private long getDisplayTime()
        {
            return FADE_IN + FADE_OUT + getLifetime();
        }
    }
}