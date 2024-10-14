package dev.obscuria.lootjournal.client.pickup;

import dev.obscuria.lootjournal.LootJournal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public final class GroupedItemsPickup implements IPickup
{
    private final List<ItemStack> stacks = Lists.newArrayList();
    private int count;

    public GroupedItemsPickup(ItemStack stack)
    {
        this.stacks.add(stack);
        this.count = stack.getCount();
    }

    @Override
    public void renderIcon(GuiGraphics graphics, long time)
    {
        if (stacks.isEmpty()) return;
        final var interval = Math.max(200, 1000 - 50 * stacks.size());
        final var stack = stacks.get((int) (time / interval % stacks.size()));
        graphics.renderFakeItem(stack, -8, -8);
    }

    @Override
    public boolean tryMerge(IPickup pickup)
    {
        if (!(pickup instanceof GroupedItemsPickup other)) return false;
        this.stacks.addAll(other.stacks);
        this.count += other.count;
        return true;
    }

    @Override
    public MutableComponent getDisplayName()
    {
        return (count <= 1
                ? Component.translatable("pickup.loot_journal.grouped_items_single")
                : Component.translatable("pickup.loot_journal.grouped_items_multiple", count))
                .withStyle(Style.EMPTY.withColor(LootJournal.CONFIG.groupedItemsColor));
    }

    @Override
    public boolean shouldDisplayTotal()
    {
        return false;
    }

    @Override
    public int getTotal()
    {
        return 0;
    }
}