package com.obscuria.lootjournal.client.pickup;

import com.obscuria.lootjournal.LootJournalConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class Pickup {
    private static final float MAX_LIFETIME = LootJournalConfig.Client.lifetime.get();
    private static final float MAX_OFFSET = 180f;
    public int lifetime;
    public float offset = MAX_OFFSET;
    public float offsetLerp = offset;

    public Pickup() {}

    public void tick(List<Pickup> list) {
        this.lifetime++;
        this.offsetLerp = offset;
        this.offset = Math.max(0, Math.min(MAX_OFFSET, lifetime < MAX_LIFETIME * 20 ? offset - 15 : offset + 5));
        if (lifetime > MAX_LIFETIME * 20 + 30) list.remove(this);
    }

    public abstract boolean merge(Pickup pickup);

    public abstract void countTotal(int addition);
}
