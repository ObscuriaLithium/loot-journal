package dev.obscuria.lootjournal.client.themes.variables;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.obscuria.fragmentum.v2.api.common.Color;
import dev.obscuria.lootjournal.client.themes.BakedTheme;

public record ColorVariable(
        Color defaultValue,
        String key,
        String displayName,
        String description
) implements Variable<Color> {

    public static final MapCodec<ColorVariable> CODEC;

    @Override
    public MapCodec<ColorVariable> codec() {
        return CODEC;
    }

    @Override
    public Color actualValue(BakedTheme theme) {
        return theme.overrides.getString(key).map(Color::parse).orElse(defaultValue);
    }

    @Override
    public Option<?> createOption(BakedTheme theme) {
        return Variable.<java.awt.Color>createOption(this)
                .binding(Binding.generic(toAwtColor(defaultValue),
                        () -> toAwtColor(theme.getAsColor(key)),
                        value -> theme.overrides.setString(key, fromAwtColor(value).toHex())))
                .controller(it -> ColorControllerBuilder.create(it).allowAlpha(true))
                .build();
    }

    private static java.awt.Color toAwtColor(Color argb) {
        return new java.awt.Color(argb.red(), argb.green(), argb.blue(), argb.alpha());
    }

    private static Color fromAwtColor(java.awt.Color color) {
        return Color.packed(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(codec -> codec.group(
                Color.codec().fieldOf("default").forGetter(ColorVariable::defaultValue)
        ).and(Variable.baseFields(codec)).apply(codec, ColorVariable::new));
    }
}
