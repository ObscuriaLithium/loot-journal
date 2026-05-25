package dev.obscuria.lootjournal.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.obscuria.fragmentum.config.ConfigValue;
import dev.obscuria.fragmentum.util.easing.Easing;
import dev.obscuria.lootjournal.client.DefaultFilterRule;
import dev.obscuria.lootjournal.client.registry.ThemeRegistry;
import dev.obscuria.lootjournal.client.renderer.*;
import dev.obscuria.lootjournal.client.themes.BakedTheme;
import lombok.experimental.UtilityClass;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@UtilityClass
public final class ConfigBuilder {

    public static Screen createConfigScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Loot Journal Options"))
                .category(buildGeneralCategory())
                .category(buildThemesCategory())
                .category(buildLayoutCategory())
                .category(buildEffectsCategory())
                .category(buildTrackingCategory())
                .category(buildSoundsCategory())
                .category(buildFilteringCategory())
                .save(ConfigBuilder::saveAll)
                .build()
                .generateScreen(parent);
    }

    private static ConfigCategory buildGeneralCategory() {
        return category("general")
                .option(Opts.bool(Config.ENABLE_LOOT_JOURNAL))
                .option(Opts.bool(Config.SHOW_ITEM_PICKUPS))
                .option(Opts.bool(Config.SHOW_XP_PICKUPS))
                .option(Opts.bool(Config.SHOW_OVERFLOW_PICKUPS))
                .option(Opts.bool(Config.ABBREVIATE_NUMBERS))
                .group(group("display")
                        .option(Opts.enumCycle(Config.MERGE_MODE, MergeMode.class))
                        .option(Opts.enumCycle(Config.STACKING_MODE, StackingMode.class))
                        .option(Opts.intSlider(Config.MAX_NAME_WIDTH, 0, 500, 5, ValueFormat.PIXELS))
                        .option(Opts.doubleSlider(Config.DISPLAY_TIME, 1, 60, 0.5, ValueFormat.SECONDS))
                        .option(Opts.intSlider(Config.DISPLAY_CAPACITY, 1, 64, 1, ValueFormat.ENTRIES))
                        .option(Opts.intSlider(Config.QUEUE_SIZE, 0, 256, 1, ValueFormat.ENTRIES))
                        .build())
                .build();
    }

    private static ConfigCategory buildLayoutCategory() {
        return category("layout")
                .option(Opts.enumCycle(Config.SCREEN_ANCHOR, ScreenAnchor.class))
                .option(Opts.enumCycle(Config.GROWTH_DIRECTION, GrowthDirection.class))
                .option(Opts.intSlider(Config.ANCHOR_X_OFFSET, -256, 256, 1, ValueFormat.PIXELS))
                .option(Opts.intSlider(Config.ANCHOR_Y_OFFSET, -256, 256, 1, ValueFormat.PIXELS))
                .option(Opts.intSlider(Config.SEPARATION, 0, 16, 1, ValueFormat.PIXELS))
                .option(Opts.doubleSlider(Config.SCALE, 0.1, 3.0, 0.1, ValueFormat.PERCENT))
                .group(group("elements")
                        .option(Opts.string(Config.ELEMENT_ORDER))
                        .option(Opts.intSlider(Config.ELEMENT_PADDING_LEFT, 0, 32, 1, ValueFormat.PIXELS))
                        .option(Opts.intSlider(Config.ELEMENT_PADDING_RIGHT, 0, 32, 1, ValueFormat.PIXELS))
                        .option(Opts.intSlider(Config.ELEMENT_PADDING_TOP, 0, 16, 1, ValueFormat.PIXELS))
                        .option(Opts.intSlider(Config.ELEMENT_PADDING_BOTTOM, 0, 16, 1, ValueFormat.PIXELS))
                        .build())
                .build();
    }

    private static ConfigCategory buildEffectsCategory() {
        return category("effects")
                .option(Opts.doubleSlider(Config.FADE_IN_TIME, 0, 5, 0.1, ValueFormat.SECONDS))
                .option(Opts.doubleSlider(Config.FADE_OUT_TIME, 0, 5, 0.1, ValueFormat.SECONDS))
                .option(Opts.easing(Config.FADE_IN_EASING))
                .option(Opts.easing(Config.FADE_OUT_EASING))
                .group(group("pulse")
                        .option(Opts.doubleSlider(Config.PULSE_STRENGTH, 0, 10, 0.1, ValueFormat.PERCENT))
                        .option(Opts.doubleSlider(Config.PULSE_TIME, 0, 5, 0.1, ValueFormat.SECONDS))
                        .option(Opts.doubleSlider(Config.PULSE_PEAK, 0, 1, 0.05, ValueFormat.RAW))
                        .option(Opts.easing(Config.PULSE_EASE_IN))
                        .option(Opts.easing(Config.PULSE_EASE_OUT))
                        .build())
                .group(group("specialEffects")
                        .option(Opts.bool(Config.RAY_GLOW_ENABLED))
                        .build())
                .build();
    }

    private static ConfigCategory buildTrackingCategory() {
        return category("tracking")
                .option(LabelOption.create(
                        translate("option.tracking.label")
                                .withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))))
                .option(Opts.bool(Config.TRACK_ITEM_PICKUPS))
                .option(Opts.bool(Config.TRACK_XP_PICKUPS))
                .option(Opts.bool(Config.ENABLE_PLAYER_FILTERING))
                .option(Opts.stringList(Config.PLAYER_WHITELIST, "Nickname"))
                .build();
    }

    private static ConfigCategory buildSoundsCategory() {
        return category("sounds")
                .option(Opts.bool(Config.ENABLE_SOUNDS))
                .option(Opts.sound(Config.SOUND_ID))
                .option(Opts.doubleSlider(Config.SOUND_VOLUME, 0, 1, 0.05, ValueFormat.PERCENT))
                .option(Opts.doubleSlider(Config.SOUND_PITCH, 0, 2, 0.05, ValueFormat.RAW))
                .build();
    }

    private static ConfigCategory buildFilteringCategory() {
        return category("filtering")
                .option(Opts.bool(Config.ENABLE_FILTERING))
                .option(Opts.bool(Config.HIDE_YOUR_COMMON_ITEMS))
                .option(Opts.bool(Config.HIDE_OTHER_COMMON_ITEMS))
                .option(Opts.enumCycle(Config.DEFAULT_FILTER_RULE, DefaultFilterRule.class))
                .group(group("filters")
                        .option(Opts.screenButton("itemIdFilters", ConfigBuilder::createItemIdFiltersScreen))
                        .option(Opts.screenButton("itemTagFilters", ConfigBuilder::createItemTagFiltersScreen))
                        .option(Opts.screenButton("modIdFilters", ConfigBuilder::createModIdFiltersScreen))
                        .build())
                .build();
    }

    private static ConfigCategory buildThemesCategory() {
        var builder = category("themes")
                .option(Opts.base(Config.THEME)
                        .controller(option -> DropdownStringControllerBuilder.create(option)
                                .values(ThemeRegistry.listThemeNames()))
                        .build())
                .option(LabelOption.create(translate("option.activeTheme.label")
                        .withStyle(Style.EMPTY.withItalic(true).withColor(ChatFormatting.GRAY))));
        ThemeRegistry.stream().forEach(theme -> builder.group(buildThemeGroup(theme)));
        return builder.build();
    }

    private static OptionGroup buildThemeGroup(BakedTheme theme) {
        Component name = ThemeRegistry.isBuiltin(theme)
                ? Component.literal(theme.displayName())
                : Component.literal(theme.displayName())
                .append(CommonComponents.SPACE)
                .append(Component.literal("Custom").withStyle(ChatFormatting.DARK_GRAY));

        var builder = OptionGroup.createBuilder()
                .name(name)
                .description(OptionDescription.of(
                        Component.literal(theme.description()).withStyle(ChatFormatting.GRAY),
                        CommonComponents.EMPTY,
                        Component.literal("ID: ")
                                .append(Component.literal(ThemeRegistry.getId(theme).toString())
                                        .withStyle(ChatFormatting.GRAY))));

        theme.theme.variables().forEach(variable -> builder.option(variable.createOption(theme)));
        return builder.collapsed(true).build();
    }

    private static Screen createItemIdFiltersScreen(Screen parent) {
        return createFilterScreen(parent, "itemIdFilters",
                Opts.itemIdList(Config.ITEM_ID_BLACKLIST),
                Opts.itemIdList(Config.ITEM_ID_WHITELIST));
    }

    private static Screen createItemTagFiltersScreen(Screen parent) {
        return createFilterScreen(parent, "itemTagFilters",
                Opts.itemTagList(Config.ITEM_TAG_BLACKLIST),
                Opts.itemTagList(Config.ITEM_TAG_WHITELIST));
    }

    private static Screen createModIdFiltersScreen(Screen parent) {
        return createFilterScreen(parent, "modIdFilters",
                Opts.modIdList(Config.MOD_ID_BLACKLIST),
                Opts.modIdList(Config.MOD_ID_WHITELIST));
    }

    private static Screen createFilterScreen(
            Screen parent,
            String categoryKey,
            ListOption<String> blacklist,
            ListOption<String> whitelist
    ) {
        return YetAnotherConfigLib.createBuilder()
                .title(translate("category." + categoryKey))
                .category(category(categoryKey)
                        .option(LabelOption.create(
                                translate("option." + categoryKey + ".label")
                                        .withStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))))
                        .group(blacklist)
                        .group(whitelist)
                        .build())
                .save(ConfigBuilder::saveAll)
                .build()
                .generateScreen(parent);
    }

    private static void saveAll() {
        Config.VALUES.forEach(ConfigValue::save);
        ConfigCache.refresh();
        ThemeRegistry.clearCache();
        ThemeRegistry.updateActiveTheme();
        ThemeRegistry.stream().forEach(BakedTheme::saveOverrides);
    }

    private static ConfigCategory.Builder category(String key) {
        return ConfigCategory.createBuilder()
                .name(translate("category." + key));
    }

    private static OptionGroup.Builder group(String key) {
        return OptionGroup.createBuilder()
                .name(translate("group." + key))
                .description(Opts.descriptionOf(translate("group." + key + ".desc")));
    }

    static MutableComponent translate(String key) {
        return Component.translatable("config.loot_journal." + key);
    }

    static MutableComponent translate(String key, Object... args) {
        return Component.translatable("config.loot_journal." + key, args);
    }

    public static final class Opts {

        private Opts() {}

        public static OptionDescription descriptionOf(Component text) {
            return OptionDescription.of(text.copy().withStyle(ChatFormatting.GRAY));
        }

        static <T> Option.Builder<T> base(ConfigValue<T> value) {
            return Option.<T>createBuilder()
                    .name(translate("option." + value.name()))
                    .description(descriptionOf(translate("option." + value.name() + ".desc")))
                    .binding(binding(value));
        }

        static Option<Boolean> bool(ConfigValue<Boolean> value) {
            return base(value).controller(TickBoxControllerBuilder::create).build();
        }

        static <T extends Enum<T>> Option<T> enumCycle(ConfigValue<T> value, Class<T> enumClass) {
            return base(value)
                    .controller(o -> EnumControllerBuilder.create(o).enumClass(enumClass))
                    .build();
        }

        static Option<Double> doubleSlider(ConfigValue<Double> value, double min, double max, double step, ValueFormat format) {
            return base(value)
                    .controller(o -> DoubleSliderControllerBuilder.create(o)
                            .range(min, max)
                            .step(step)
                            .formatValue(format.formatter))
                    .build();
        }

        static Option<Integer> intSlider(ConfigValue<Integer> value, int min, int max, int step, ValueFormat format) {
            return base(value)
                    .controller(o -> IntegerSliderControllerBuilder.create(o)
                            .range(min, max)
                            .step(step)
                            .formatValue(v -> format.formatter.format(v.doubleValue())))
                    .build();
        }

        static Option<String> string(ConfigValue<String> value) {
            return base(value).controller(StringControllerBuilder::create).build();
        }

        static Option<String> easing(ConfigValue<Easing> value) {
            return Option.<String>createBuilder()
                    .name(translate("option." + value.name()))
                    .description(descriptionOf(translate("option." + value.name() + ".desc")))
                    .binding(binding(value, Easing::name, Easing::valueOf))
                    .controller(o -> DropdownStringControllerBuilder.create(o)
                            .values(Arrays.stream(Easing.values()).map(Easing::name).toList()))
                    .build();
        }

        static Option<String> sound(ConfigValue<String> value) {
            return base(value)
                    .controller(o -> DropdownStringControllerBuilder.create(o)
                            .values(BuiltInRegistries.SOUND_EVENT.stream()
                                    .map(SoundEvent::getLocation)
                                    .map(ResourceLocation::toString)
                                    .toList()))
                    .build();
        }

        static ListOption<String> stringList(ConfigValue<List<? extends String>> value, String initialEntry) {
            return ListOption.<String>createBuilder()
                    .name(translate("option." + value.name()))
                    .description(descriptionOf(translate("option." + value.name() + ".desc")))
                    .binding(listBinding(value))
                    .controller(StringControllerBuilder::create)
                    .initial(initialEntry)
                    .build();
        }

        static ListOption<String> itemIdList(ConfigValue<List<? extends String>> value) {
            return dropdownList(value, () -> BuiltInRegistries.ITEM.keySet().stream()
                    .map(ResourceLocation::toString)
                    .sorted()
                    .toList());
        }

        static ListOption<String> itemTagList(ConfigValue<List<? extends String>> value) {
            return dropdownList(value, () -> BuiltInRegistries.ITEM.getTagNames()
                    .map(TagKey::location)
                    .map(ResourceLocation::toString)
                    .sorted()
                    .toList());
        }

        static ListOption<String> modIdList(ConfigValue<List<? extends String>> value) {
            return dropdownList(value, () -> BuiltInRegistries.ITEM.keySet().stream()
                    .map(ResourceLocation::getNamespace)
                    .distinct()
                    .sorted()
                    .toList());
        }

        static ButtonOption screenButton(String key, Function<Screen, Screen> screenFactory) {
            return ButtonOption.createBuilder()
                    .name(translate("option." + key))
                    .text(translate("option." + key + ".button"))
                    .description(descriptionOf(translate("option." + key + ".desc")))
                    .action((screen, option) -> Minecraft.getInstance().setScreen(screenFactory.apply(screen)))
                    .build();
        }

        private static ListOption<String> dropdownList(
                ConfigValue<List<? extends String>> value,
                Supplier<List<String>> valuesSupplier
        ) {
            return ListOption.<String>createBuilder()
                    .name(translate("option." + value.name()))
                    .description(descriptionOf(translate("option." + value.name() + ".desc")))
                    .binding(listBinding(value))
                    .controller(option -> DropdownStringControllerBuilder.create(option)
                            .values(valuesSupplier.get())
                            .allowAnyValue(false)
                            .allowEmptyValue(false))
                    .initial("")
                    .build();
        }

        private static <T> Binding<T> binding(ConfigValue<T> value) {
            return Binding.generic(value.getDefault(), value::get, value::set);
        }

        private static <T, V> Binding<V> binding(ConfigValue<T> value, Function<T, V> encode, Function<V, T> decode) {
            return Binding.generic(
                    encode.apply(value.getDefault()),
                    () -> encode.apply(value.get()),
                    decoded -> value.set(decode.apply(decoded)));
        }

        private static <T> Binding<List<T>> listBinding(ConfigValue<List<? extends T>> value) {
            return Binding.generic(
                    new ArrayList<>(value.getDefault()),
                    () -> new ArrayList<>(value.get()),
                    updated -> value.set(new ArrayList<>(updated)));
        }
    }

    enum ValueFormat {
        RAW(v -> Component.literal("%.1f".formatted(v))),
        SECONDS(v -> translate("format.seconds", "%.1f".formatted(v))),
        PERCENT(v -> translate("format.percent", "%.0f".formatted(v * 100))),
        PIXELS(v -> translate("format.pixels", v.intValue())),
        ENTRIES(v -> translate("format.entries", v.intValue()));

        final ValueFormatter<Double> formatter;

        ValueFormat(ValueFormatter<Double> formatter) {
            this.formatter = formatter;
        }
    }
}