package com.leclowndu93150.thaumaturge.content.research.scan;

import java.util.Objects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class ScanRaycastHelper {
    private static final float ENTITY_BOX_PADDING = 1.0F;

    private ScanRaycastHelper() {}

    public static HitResult performRaycast(Player player) {
        return performRaycast(player, ClipContext.Fluid.ANY);
    }

    public static HitResult performRaycast(Player player, ClipContext.Fluid fluid) {
        return performRaycast(new ScanRaycastContext(
                player.level(),
                player.getEyePosition(),
                player.getViewVector(1.0F),
                player.blockInteractionRange(),
                player.entityInteractionRange(),
                fluid,
                player));
    }

    public static HitResult performRaycast(ScanRaycastContext ctx) {
        Level level = ctx.level();
        Vec3 start = ctx.start();
        Vec3 direction = ctx.direction();

        BlockHitResult blockResult = level.clip(new ClipContext(start,
                start.add(direction.scale(ctx.blockReach())),
                ClipContext.Block.OUTLINE, ctx.fluidFilter(), ctx.entity()));
        EntityHitResult entityResult = clipEntity(start, direction, ctx.entityReach(), ctx.entity());

        if (blockResult.getType() != HitResult.Type.MISS && entityResult != null) {
            double blockDistance = blockResult.getLocation().distanceToSqr(start);
            double entityDistance = entityResult.getLocation().distanceToSqr(start);
            return blockDistance < entityDistance ? blockResult : entityResult;
        }
        if (blockResult.getType() != HitResult.Type.MISS) {
            return blockResult;
        }
        return entityResult != null ? entityResult
                : BlockHitResult.miss(ctx.entity().getEyePosition(), ctx.entity().getDirection(),
                        ctx.entity().blockPosition());
    }

    private static @Nullable EntityHitResult clipEntity(Vec3 start, Vec3 direction, double entityReach, Entity entity) {
        AABB box = entity.getBoundingBox()
                .expandTowards(direction.scale(entityReach))
                .inflate(ENTITY_BOX_PADDING, ENTITY_BOX_PADDING, ENTITY_BOX_PADDING);
        return ProjectileUtil.getEntityHitResult(entity, start, start.add(direction.scale(entityReach)), box,
                candidate -> !candidate.isSpectator(), entityReach * entityReach);
    }

    public record ScanRaycastContext(Level level, Vec3 start, Vec3 direction, double blockReach, double entityReach,
                                     ClipContext.Fluid fluidFilter, Entity entity) {
        public ScanRaycastContext {
            Objects.requireNonNull(level, "level cannot be null");
            Objects.requireNonNull(start, "start cannot be null");
            Objects.requireNonNull(direction, "direction cannot be null");
            Objects.requireNonNull(fluidFilter, "fluidFilter cannot be null");
            Objects.requireNonNull(entity, "entity cannot be null");
        }

        public ScanRaycastContext withBlockReach(double blockReach) {
            return new ScanRaycastContext(level, start, direction, blockReach, entityReach, fluidFilter, entity);
        }

        public ScanRaycastContext withEntityReach(double entityReach) {
            return new ScanRaycastContext(level, start, direction, blockReach, entityReach, fluidFilter, entity);
        }

        public ScanRaycastContext withFluidFilter(ClipContext.Fluid fluidFilter) {
            return new ScanRaycastContext(level, start, direction, blockReach, entityReach, fluidFilter, entity);
        }
    }
}
