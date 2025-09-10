package dev.obscuria.lootjournal;

import dev.obscuria.lootjournal.client.ItemPolicy;
import dev.obscuria.lootjournal.client.render.Anchor;
import dev.obscuria.lootjournal.client.render.PickupDrawStyle;

public final class ModConfigDefaults
{
    public static final PickupDrawStyle STYLE = PickupDrawStyle.CLASSIC;
    //
    public static final boolean ITEM_ENTRY_DISPLAY = true;
    public static final boolean ITEM_ENTRY_DISPLAY_TOTAL_AMOUNT = true;
    public static final boolean ITEM_ENTRY_USE_ITEM_FORMATTING = true;
    public static final int ITEM_ENTRY_COLOR = 0xFFFFFF;
    public static final boolean ITEM_ENTRY_ITALIC = false;
    //
    public static final boolean AGGREGATED_ENTRY_DISPLAY = true;
    public static final int AGGREGATED_ENTRY_COLOR = 0xFFFFFF;
    public static final boolean AGGREGATED_ENTRY_ITALIC = true;
    //
    public static final boolean EXPERIENCE_ENTRY_DISPLAY = true;
    public static final int EXPERIENCE_ENTRY_COLOR = 0x55FF55;
    public static final boolean EXPERIENCE_ENTRY_ITALIC = false;
    //
    public static final Anchor ANCHOR = Anchor.BOTTOM_RIGHT;
    public static final int ANCHOR_PIXEL_OFFSET = 3;
    public static final int ANCHOR_PERCENT_OFFSET = 10;
    public static final int SCALE = 100;
    public static final int SEPARATION = 3;
    public static final int LIFETIME = 6;
    public static final int DISPLAY_CAPACITY = 9;
    public static final int QUEUE_CAPACITY = 9;
    //
    public static final ItemPolicy DEFAULT_ITEM_POLICY = ItemPolicy.ALLOW_ALL;
}
