package dev.obscuria.lootjournal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.obscuria.fragmentum.content.registry.DelegatedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public final class LootJournalCodecs {

    public static <T> Codec<Holder.Reference<T>> holderByNameCodec(DelegatedRegistry<T> registry) {
        return ResourceLocation.CODEC
                .xmap(
                        LootJournalCodecs::swapDefaultNamespace,
                        Function.identity())
                .comapFlatMap(
                        id -> findReference(registry, id),
                        ref -> ref.key().location());
    }

    public static <T> Codec<T> byNameCodec(DelegatedRegistry<T> registry) {
        return holderByNameCodec(registry).flatComapMap(
                Holder.Reference::value,
                value -> wrapAsHolder(registry, value));
    }

    private static ResourceLocation swapDefaultNamespace(ResourceLocation identifier) {
        return "minecraft".equals(identifier.getNamespace())
                ? ResourceLocation.fromNamespaceAndPath(LootJournal.MOD_ID, identifier.getPath())
                : identifier;
    }

    private static <T> DataResult<Holder.Reference<T>> findReference(DelegatedRegistry<T> registry, ResourceLocation id) {
        @Nullable var holder = registry.getHolder(id);
        if (holder != null) return DataResult.success(holder);
        return DataResult.error(() -> "Unknown registry key in " + registry.key() + ": " + id);
    }

    private static <T> DataResult<Holder.Reference<T>> wrapAsHolder(DelegatedRegistry<T> registry, T value) {
        var key = Objects.requireNonNull(registry.getKey(value));
        return DataResult.success(Objects.requireNonNull(registry.getHolder(key)));
    }
}
