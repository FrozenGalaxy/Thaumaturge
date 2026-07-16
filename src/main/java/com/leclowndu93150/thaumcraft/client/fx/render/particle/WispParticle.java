package com.leclowndu93150.thaumcraft.client.fx.render.particle;

import com.leclowndu93150.thaumcraft.content.fx.data.WispData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class WispParticle extends TextureSheetParticle {
    private static final float BASE_ALPHA = 0.2F;
    private static final float MAX_DIST_SQ = 1024.0F;
    private static final float SCALE_MULTIPLIER = 0.4F;
    private static final float COLOR_MAX = 0.05F;
    private static final int FRAME_COUNT = 13;

    private final SpriteSet sprites;
    private final int targetEntityId;
    private Entity target;

    private WispParticle(ClientLevel level, double x, double y, double z, WispData data, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.setSprite(sprites.get(0, FRAME_COUNT));
        this.sprites = sprites;
        this.targetEntityId = data.entityId();
        this.xd = level.getRandom().nextGaussian() * 0.03;
        this.yd = -0.05;
        this.zd = level.getRandom().nextGaussian() * 0.03;
        this.quadSize *= SCALE_MULTIPLIER;
        this.lifetime = (int)(40.0 / (level.getRandom().nextDouble() * 0.3 + 0.7));
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.setSize(0.01F, 0.01F);
        this.rCol = level.getRandom().nextFloat() * COLOR_MAX;
        this.gCol = level.getRandom().nextFloat() * COLOR_MAX;
        this.bCol = level.getRandom().nextFloat() * COLOR_MAX;
        this.alpha = BASE_ALPHA;
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSprite(this.sprites.get(this.age % FRAME_COUNT, FRAME_COUNT));
        if (this.target == null && this.targetEntityId != WispData.NO_ENTITY) {
            this.target = this.level.getEntity(this.targetEntityId);
        }
        if (this.target != null && !this.onGround) {
            Vec3 motion = this.target.getDeltaMovement();
            this.x += motion.x;
            this.z += motion.z;
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        float ageScale = 1.0F - (float)this.age / this.lifetime;
        Vec3 camPos = camera.getPosition();
        double dx = camPos.x() - this.x;
        double dy = camPos.y() - this.y;
        double dz = camPos.z() - this.z;
        double distSq = dx * dx + dy * dy + dz * dz;
        float base = (float)(1.0 - Math.min((double)MAX_DIST_SQ, distSq) / MAX_DIST_SQ);
        float prev = this.alpha;
        this.alpha = Mth.clamp(BASE_ALPHA * ageScale * base, 0.0F, 1.0F);
        super.render(buffer, camera, partialTick);
        this.alpha = prev;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static final class Provider implements ParticleProvider<WispData> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(WispData options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
            return new WispParticle(level, x, y, z, options, sprites);
        }
    }
}
