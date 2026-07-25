package com.leclowndu93150.thaumcraft.content.entity;

import net.minecraft.world.entity.AnimationState;

public interface IBatAnimated {
    AnimationState flyAnimation();

    AnimationState restAnimation();

    boolean isResting();
}
