package dev.obscuria.lootjournal.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.Nullable;

public enum ItemCountResolver {

    SELF {
        @Override
        public int resolve(ItemStack target, ItemStack stack) {
            return ItemStack.isSameItem(target, stack) ? stack.getCount() : 0;
        }
    },
    GENERIC_CONTAINER {
        @Override
        public int resolve(ItemStack target, ItemStack stack) {
            @Nullable var container = stack.get(DataComponents.CONTAINER);
            if (container == null || container == ItemContainerContents.EMPTY) return 0;
            return resolveIterable(target, container.nonEmptyItems());
        }
    };

    public abstract int resolve(ItemStack target, ItemStack stack);

    private static int resolveIterable(ItemStack target, Iterable<ItemStack> list) {
        var total = 0;
        for (var stack : list) {
            total += resolveRecursive(target, stack);
        }
        return total;
    }

    public static int resolveRecursive(ItemStack target, ItemStack stack) {
        var total = 0;
        for (var resolver : values()) {
            total += resolver.resolve(target, stack);
        }
        return total;
    }
}
