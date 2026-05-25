package dev.obscuria.lootjournal.client.registry;

import dev.obscuria.lootjournal.LootJournal;
import dev.obscuria.lootjournal.client.themes.BakedTheme;
import dev.obscuria.lootjournal.client.themes.Theme;
import dev.obscuria.lootjournal.config.Config;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class ThemeRegistry {

    private static final HashMap<ResourceLocation, BakedTheme> THEMES = new HashMap<>();
    private static final List<ResourceLocation> BUILTIN_ORDER = List.of(
            LootJournal.id("classic"),
            LootJournal.id("minimal"),
            LootJournal.id("tooltip"),
            LootJournal.id("contrast"),
            LootJournal.id("java"),
            LootJournal.id("bedrock"),
            LootJournal.id("dungeons"));

    private static BakedTheme activeTheme = BakedTheme.DEFAULT;

    public static BakedTheme activeTheme() {
        return activeTheme;
    }

    public static Stream<BakedTheme> stream() {
        var builtIn = BUILTIN_ORDER.stream()
                .map(THEMES::get)
                .filter(Objects::nonNull);

        var others = THEMES.entrySet().stream()
                .filter(entry -> !BUILTIN_ORDER.contains(entry.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue);

        return Stream.concat(builtIn, others);
    }

    public static void add(ResourceLocation id, Theme theme) {
        add(id, theme.bake());
    }

    public static void add(ResourceLocation id, BakedTheme theme) {
        THEMES.put(id, theme);
    }

    public static ResourceLocation getId(BakedTheme theme) {
        if (theme == BakedTheme.DEFAULT) return LootJournal.id("fallback");
        return THEMES.entrySet().stream()
                .filter(entry -> entry.getValue().equals(theme))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    public static boolean isBuiltin(BakedTheme theme) {
        return BUILTIN_ORDER.contains(getId(theme));
    }

    public static void updateActiveTheme() {
        var activeThemeName = Config.THEME.get();
        activeTheme = THEMES.values().stream()
                .filter(theme -> theme.displayName().equals(activeThemeName))
                .findFirst().orElse(BakedTheme.DEFAULT);
    }

    public static void clearCache() {
        THEMES.values().forEach(BakedTheme::clearCache);
    }

    public static void clear() {
        THEMES.clear();
    }

    public static List<String> listThemeNames() {
        return stream().map(BakedTheme::displayName).toList();
    }
}
