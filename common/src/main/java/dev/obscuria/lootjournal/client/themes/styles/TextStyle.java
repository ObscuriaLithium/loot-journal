package dev.obscuria.lootjournal.client.themes.styles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.fragmentum.v2.api.common.Color;
import dev.obscuria.lootjournal.client.themes.styles.vars.Var;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record TextStyle(
        Optional<Identifier> font,
        Var<Color> nameColor,
        Var<Color> pickupCountColor,
        Var<Color> totalCountColor,
        Var<Boolean> dropShadow,
        Var<Boolean> ignoreFormatting
) {

    public static final Codec<TextStyle> CODEC;
    public static final TextStyle DEFAULT;

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                Identifier.CODEC.optionalFieldOf("font").forGetter(TextStyle::font),
                Var.COLOR.fieldOf("name_color").forGetter(TextStyle::nameColor),
                Var.COLOR.fieldOf("pickup_count_color").forGetter(TextStyle::pickupCountColor),
                Var.COLOR.fieldOf("total_count_color").forGetter(TextStyle::totalCountColor),
                Var.BOOL.fieldOf("drop_shadow").forGetter(TextStyle::dropShadow),
                Var.BOOL.fieldOf("ignore_formatting").forGetter(TextStyle::ignoreFormatting)
        ).apply(codec, TextStyle::new));
        DEFAULT = new TextStyle(
                Optional.empty(),
                new Var.DirectVar<>(Color.WHITE),
                new Var.DirectVar<>(Color.WHITE),
                new Var.DirectVar<>(Color.WHITE),
                new Var.DirectVar<>(true),
                new Var.DirectVar<>(false));
    }
}
