package com.leclowndu93150.thaumcraft.registry;

import com.leclowndu93150.thaumcraft.TCIds;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

public final class TCEffectTags {
    public static final TagKey<MobEffect> MANA_BEAN_EFFECTS = key("mana_bean_effects");

    private TCEffectTags() {}

    private static TagKey<MobEffect> key(String path) {
        return TagKey.create(Registries.MOB_EFFECT, TCIds.rl(path));
    }
}
