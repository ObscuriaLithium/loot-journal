package dev.obscuria.lootjournal.client.registry;

import com.mojang.serialization.*;

import java.util.function.Function;
import java.util.stream.Stream;

public final class CompactDispatchCodec<K, V> extends MapCodec<V> {

    private final Codec<K> keyCodec;
    private final Function<? super V, ? extends DataResult<? extends K>> type;
    private final Function<? super K, ? extends DataResult<? extends MapCodec<? extends V>>> codec;

    public static <K, V> Codec<V> create(
            Codec<K> keyCodec,
            Function<? super V, ? extends K> type,
            Function<? super K, ? extends MapCodec<? extends V>> codec) {
        return new CompactDispatchCodec<K, V>(
                keyCodec,
                type.andThen(DataResult::success),
                codec.andThen(DataResult::success)
        ).codec();
    }

    public static <K, V> Codec<V> partialDispatch(
            Codec<K> keyCodec,
            Function<? super V, ? extends DataResult<? extends K>> type,
            Function<? super K, ? extends DataResult<? extends MapCodec<? extends V>>> codec) {
        return new CompactDispatchCodec<>(keyCodec, type, codec).codec();
    }

    private CompactDispatchCodec(
            Codec<K> keyCodec,
            Function<? super V, ? extends DataResult<? extends K>> type,
            Function<? super K, ? extends DataResult<? extends MapCodec<? extends V>>> codec) {
        this.keyCodec = keyCodec;
        this.type = type;
        this.codec = codec;
    }

    @Override
    public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
        return input.entries()
                .map(entry -> keyCodec.parse(ops, entry.getFirst())
                        .<V>flatMap(k -> codec.apply(k)
                                .flatMap(mapCodec -> mapCodec.decode(ops, input)
                                        .map(Function.identity()))))
                .filter(DataResult::isSuccess)
                .findFirst()
                .orElseGet(() -> DataResult.error(() -> "No valid type key found in: " + input));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        var typeResult = type.apply(input);
        var builder = prefix.withErrorsFrom(typeResult);
        if (typeResult.isError()) return builder;

        var key = typeResult.getOrThrow();
        var codecResult = codec.apply(key);
        builder.withErrorsFrom(codecResult);
        if (codecResult.isError()) return builder;

        var mapCodec = (MapCodec<V>) codecResult.getOrThrow();
        return mapCodec.encode(input, ops, builder);
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return Stream.empty();
    }
}