package dev.obscuria.lootjournal.client.pickup;

import dev.obscuria.lootjournal.LootJournal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class ItemPickup implements IPickup
{
    public static final CompoundTag EMPTY_TAG = new CompoundTag();
    private final ItemStack stack;
    private int count;
    private int total;

    public ItemPickup(ItemStack stack)
    {
        this.stack = stack;
        this.count = stack.getCount();
        this.countTotal(count);
    }

    @Override
    public void renderIcon(GuiGraphics graphics, long time)
    {
        graphics.renderFakeItem(stack, -8, -8);
    }

    @Override
    public boolean tryMerge(IPickup pickup)
    {
        if (!(pickup instanceof ItemPickup other)) return false;
        if (!ItemStack.isSameItemSameTags(stack, other.stack)) return false;
        this.count += other.count;
        this.countTotal(other.count);
        return true;
    }

    @Override
    public MutableComponent getDisplayName()
    {
        final var result = count <= 1
                ? Component.translatable("pickup.loot_journal.item_single", stack.getHoverName().getString())
                : Component.translatable("pickup.loot_journal.item_multiple", stack.getHoverName().getString(), count);
        return LootJournal.CONFIG.useRarityColor
                ? result.withStyle(stack.getRarity().color)
                : result.withStyle(Style.EMPTY.withColor(LootJournal.CONFIG.itemsColor));
    }

    @Override
    public boolean shouldDisplayTotal()
    {
        return LootJournal.CONFIG.displayTotal && total > 1;
    }

    @Override
    public int getTotal()
    {
        return this.total;
    }

    private void countTotal(int origin)
    {
        this.total = origin;
        final var player = Minecraft.getInstance().player;
        if (player == null) return;
        for (var stack : player.getInventory().items)
            this.total += countSameItems(stack);
    }

    private int countSameItems(ItemStack stack)
    {
        int searched = 0;
        if (ItemStack.isSameItemSameTags(this.stack, stack))
            searched += stack.getCount();

        //Skulker Box
        if (Optional.ofNullable(stack.getTag())
                .orElse(EMPTY_TAG)
                .getCompound("BlockEntityTag")
                .get("Items") instanceof ListTag listTag)
            for (var tagIn : listTag.stream().toList())
                if (tagIn instanceof CompoundTag compoundTag)
                    searched += countSameItems(ItemStack.of(compoundTag));

        //Items Tag
        if (Optional.ofNullable(stack.getTag())
                .orElse(EMPTY_TAG)
                .get("Items") instanceof ListTag listTag)
            for (var tagIn : listTag.stream().toList())
                if (tagIn instanceof CompoundTag compoundTag)
                    searched += countSameItems(ItemStack.of(compoundTag));
        return searched;
    }
}