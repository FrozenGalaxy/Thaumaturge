package com.leclowndu93150.thaumaturge.content.entity;

import net.minecraft.world.entity.AnimationState;

public interface IBatAnimated {
    AnimationState flyAnimation();

    AnimationState restAnimation();

    boolean isResting();
}
