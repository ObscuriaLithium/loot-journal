package dev.obscuria.lootjournal.client.render;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.pickup.ExperiencePickup;
import dev.obscuria.lootjournal.client.pickup.ItemPickup;
import dev.obscuria.lootjournal.client.pickup.AggregatedPickup;
import dev.obscuria.lootjournal.client.pickup.IPickupEntry;
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
    private static final List<PickupInstance> displayed = Lists.newArrayList();
    private static final Deque<IPickupEntry> queued = Queues.newArrayDeque();

    public static void render(GuiGraphics graphics)
    {
        final var window = Minecraft.getInstance().getWindow();

        graphics.pose().pushPose();
        LootJournal.CONFIG.anchor.transform(graphics, window);

        displayed.removeIf(instance -> {
            if (!instance.render(graphics)) return false;
            occupied.remove(instance.index);
            return true;
        });

        graphics.pose().popPose();

        if (queued.isEmpty() || isAllSlotsOccupied()) return;

        queued.removeIf(pickup -> {
            final var index = findFreeSlot();
            if (index <= -1) return false;
            final var instance = new PickupInstance(pickup, index);
            displayed.add(instance);
            occupied.put(index, instance);
            return true;
        });
    }

    public static void appendItem(int itemId, int playerId, int amount)
    {
        if (!LootJournal.CONFIG.itemEntryDisplay) return;
        final var player = Minecraft.getInstance().player;
        if (player == null || player.getId() != playerId) return;
        if (!(player.level().getEntity(itemId) instanceof ItemEntity entity)) return;
        final var stack = entity.getItem().copy();
        stack.setCount(amount);
        appendItem(stack);
    }

    public static void appendItem(ItemStack stack)
    {
        if (!LootJournal.CONFIG.itemEntryDisplay) return;
        if (!LootJournal.isAllowed(stack)) return;
        final var shouldAggregate = shouldAggregate();
        if (shouldAggregate && !LootJournal.CONFIG.aggregatedEntryDisplay) return;
        append(shouldAggregate ? new AggregatedPickup(stack) : new ItemPickup(stack));
    }

    public static void appendExperience(int amount)
    {
        if (!LootJournal.CONFIG.experienceEntryDisplay) return;
        append(new ExperiencePickup(amount));
    }

    private static void append(IPickupEntry pickup)
    {
        if (maybeMerge(pickup)) return;
        final var index = findFreeSlot();

        if (index > -1)
        {
            final var instance = new PickupInstance(pickup, index);
            displayed.add(instance);
            occupied.put(index, instance);
        } else if (shouldEnqueue(pickup))
        {
            queued.add(pickup);
        }
    }

    private static boolean maybeMerge(IPickupEntry pickup)
    {
        for (var instance : displayed)
            if (instance.maybeMerge(pickup))
                return true;
        for (var other : queued)
            if (other.maybeMerge(pickup))
                return true;
        return false;
    }

    private static boolean shouldEnqueue(IPickupEntry pickup)
    {
        return pickup instanceof AggregatedPickup || queued.size() < LootJournal.CONFIG.queueCapacity;
    }

    private static int findFreeSlot()
    {
        for (var i = 0; i < LootJournal.CONFIG.displayCapacity; i++)
            if (!occupied.containsKey(i))
                return i;
        return -1;
    }

    private static boolean isAllSlotsOccupied()
    {
        return displayed.size() >= LootJournal.CONFIG.displayCapacity;
    }

    private static boolean shouldAggregate()
    {
        return isAllSlotsOccupied() && queued.size() >= LootJournal.CONFIG.queueCapacity - 1;
    }

    private static final class PickupInstance
    {
        private static final long FADE_IN = 750L;
        private static final long FADE_OUT = 1500L;
        private final IPickupEntry pickup;
        private long startTime = -1L;
        private long lastTime;
        private double progress;
        private double delta;
        public int index;

        public PickupInstance(IPickupEntry pickup, int index)
        {
            this.pickup = pickup;
            this.index = index;
        }

        public boolean render(GuiGraphics graphics)
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
            {
                var offset = LootJournal.CONFIG.anchor.getStep() * index;
                LootJournal.CONFIG.style.render(pickup, graphics, offset, progress, time);
            }

            this.updateProgress(time);
            return time > getDisplayTime();
        }

        public boolean maybeMerge(IPickupEntry other)
        {
            if (pickup.maybeMerge(other))
            {
                startTime = Util.getMillis();
                return true;
            }

            return pickup.maybeMerge(other);
        }

        private void updateProgress(long time)
        {
            if (time <= FADE_IN) progress = Math.min(progress + delta * 2.0, 1.0);
            if (time >= getDisplayTime() - FADE_OUT) progress = Math.max(progress - delta, 0.0);
        }

        private long getLifetime()
        {
            return 1000L * LootJournal.CONFIG.lifetime;
        }

        private long getDisplayTime()
        {
            return FADE_IN + FADE_OUT + getLifetime();
        }
    }
}