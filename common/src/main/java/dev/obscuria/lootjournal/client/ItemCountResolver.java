package dev.obscuria.lootjournal.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.Nullable;

public enum ItemCountResolver {

    SELF {
        @Override public int resolve(ItemInstance target, ItemInstance stack) {
            return target.is(stack.typeHolder()) ? stack.count() : 0;
        }
    },
    GENERIC_CONTAINER {
        @Override public int resolve(ItemInstance target, ItemInstance stack) {
            @Nullable var container = stack.get(DataComponents.CONTAINER);
            if (container == null || container == ItemContainerContents.EMPTY) return 0;
            return resolveIterable(target, container.nonEmptyItems());
        }
    },
    BUNDLE_CONTENTS {
        @Override public int resolve(ItemInstance target, ItemInstance stack) {
            @Nullable var contents = stack.get(DataComponents.BUNDLE_CONTENTS);
            if (contents == null || contents == BundleContents.EMPTY) return 0;
            return resolveIterable(target, contents.items());
        }
    };

    public abstract int resolve(ItemInstance target, ItemInstance stack);

    private static int resolveIterable(ItemInstance target, Iterable<? extends ItemInstance> list) {
        var total = 0;
        for (var stack : list) {
            total += resolveRecursive(target, stack);
        }
        return total;
    }

    public static int resolveRecursive(ItemInstance target, ItemInstance stack) {
        var total = 0;
        for (var resolver : values()) {
            total += resolver.resolve(target, stack);
        }
        return total;
    }
}
