package com.leclowndu93150.thaumcraft.content.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;

public interface ISidedHurt {
    boolean hurtServer(ServerLevel level, DamageSource source, float damage);

    boolean hurtClient(DamageSource source, float damage);

    default boolean hurtSided(Level level, DamageSource source, float damage) {
        return level instanceof ServerLevel serverLevel
                ? hurtServer(serverLevel, source, damage)
                : hurtClient(source, damage);
    }
}
