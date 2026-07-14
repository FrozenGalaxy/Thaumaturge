package com.leclowndu93150.thaumcraft.content.entity.ai;

import java.util.EnumSet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public final class GhastLikeLookGoal extends Goal {
    private static final double LOOK_RANGE_SQR = 4096.0;

    private final Mob flyer;

    public GhastLikeLookGoal(Mob flyer) {
        this.flyer = flyer;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.flyer.getTarget();
        if (target == null) {
            Vec3 movement = this.flyer.getDeltaMovement();
            this.flyer.setYRot(-((float) Mth.atan2(movement.x, movement.z)) * (180.0F / (float) Math.PI));
            this.flyer.yBodyRot = this.flyer.getYRot();
        } else if (target.distanceToSqr(this.flyer) < LOOK_RANGE_SQR) {
            double dx = target.getX() - this.flyer.getX();
            double dz = target.getZ() - this.flyer.getZ();
            this.flyer.setYRot(-((float) Mth.atan2(dx, dz)) * (180.0F / (float) Math.PI));
            this.flyer.yBodyRot = this.flyer.getYRot();
        }
    }
}
