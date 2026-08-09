package com.leclowndu93150.thaumaturge.content.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public final class GhastLikeFlight {
    private static final float FLUID_IMPULSE = 0.02F;
    private static final double WATER_FRICTION = 0.8;
    private static final double LAVA_FRICTION = 0.5;
    private static final double AIR_FRICTION = 0.91;

    private GhastLikeFlight() {}

    public static void travel(Mob flyer, Vec3 input, float impulse) {
        if (flyer.isInWater()) {
            flyer.moveRelative(FLUID_IMPULSE, input);
            flyer.move(MoverType.SELF, flyer.getDeltaMovement());
            flyer.setDeltaMovement(flyer.getDeltaMovement().scale(WATER_FRICTION));
        } else if (flyer.isInLava()) {
            flyer.moveRelative(FLUID_IMPULSE, input);
            flyer.move(MoverType.SELF, flyer.getDeltaMovement());
            flyer.setDeltaMovement(flyer.getDeltaMovement().scale(LAVA_FRICTION));
        } else {
            flyer.moveRelative(impulse, input);
            flyer.move(MoverType.SELF, flyer.getDeltaMovement());
            flyer.setDeltaMovement(flyer.getDeltaMovement().scale(AIR_FRICTION));
        }
        flyer.calculateEntityAnimation(false);
    }
}
