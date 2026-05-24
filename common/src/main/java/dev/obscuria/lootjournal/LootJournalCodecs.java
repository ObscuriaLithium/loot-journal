package dev.obscuria.lootjournal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;

public final class LootJournalCodecs {

    public static <T> Codec<Holder.Reference<T>> holderByNameCodec(Registry<T> registry) {
        return Identifier.CODEC
                .xmap(
                        LootJournalCodecs::swapDefaultNamespace,
                        Function.identity())
                .comapFlatMap(
                        id -> findReference(registry, id),
                        ref -> ref.key().identifier());
    }

    public static <T> Codec<T> byNameCodec(Registry<T> registry) {
        return ExtraCodecs
                .overrideLifecycle(
                        holderByNameCodec(registry),
                        ref -> registry.registrationInfo(ref.key())
                                .map(RegistrationInfo::lifecycle)
                                .orElse(Lifecycle.experimental()))
                .flatComapMap(
                        Holder.Reference::value,
                        value -> wrapAsHolder(registry, value));
    }

    private static Identifier swapDefaultNamespace(Identifier identifier) {
        return "minecraft".equals(identifier.getNamespace())
                ? Identifier.fromNamespaceAndPath(LootJournal.MOD_ID, identifier.getPath())
                : identifier;
    }

    private static <T> DataResult<Holder.Reference<T>> findReference(Registry<T> registry, Identifier id) {
        return registry.get(id)
                .map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in " + registry.key() + ": " + id));
    }

    private static <T> DataResult<Holder.Reference<T>> wrapAsHolder(Registry<T> registry, T value) {
        return registry.wrapAsHolder(value) instanceof Holder.Reference<T> ref
                ? DataResult.success(ref)
                : DataResult.error(() -> "Unregistered holder in " + registry.key() + ": " + value);
    }
}
