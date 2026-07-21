package com.leclowndu93150.thaumcraft.client.fx.render.particle;

import com.leclowndu93150.thaumcraft.client.fx.render.texture.TCParticleLayer;
import com.leclowndu93150.thaumcraft.content.fx.data.SmokeSpiralData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public final class SmokeSpiralParticle extends SingleQuadParticle {
    private static final int ROW_WIDTH = 16;
    private static final float CELL = 1.0F / 64.0F;
    private static final float QUAD_RADIUS = 0.15F;
    private static final float ALPHA = 0.66F;

    private final float radius;
    private final int startDeg;
    private final int minY;
    private int currentCell;
    private float currentAlpha = 1.0F;

    private SmokeSpiralParticle(ClientLevel level, double x, double y, double z, SmokeSpiralData data) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.setSize(0.01F, 0.01F);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.hasPhysics = false;
        this.gravity = -0.01F;
        this.lifetime = 20 + this.random.nextInt(10);
        this.radius = data.radius();
        this.startDeg = data.start();
        this.minY = data.minY();
        this.rCol = data.r();
        this.gCol = data.g();
        this.bCol = data.b();
        this.alpha = 1.0F;
        this.currentCell = 1;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.currentAlpha = (float)(this.lifetime - this.age) / this.lifetime;
        this.alpha = ALPHA * this.currentAlpha;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.currentCell = (int)(1.0F + (float)this.age / this.lifetime * 4.0F);
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        float ageFrac = (this.age + partialTick) / this.lifetime;
        float r1 = this.startDeg + 720.0F * ageFrac;
        float r2 = 90.0F - 180.0F * ageFrac;
        float r1r = (float)Math.toRadians(r1);
        float r2r = (float)Math.toRadians(r2);
        float mX = -Mth.sin(r1r) * Mth.cos(r2r) * this.radius;
        float mY = -Mth.sin(r2r) * this.radius;
        float mZ = Mth.cos(r1r) * Mth.cos(r2r) * this.radius;
        Vec3 pos = camera.getPosition();
        float baseX = (float)(Mth.lerp((double)partialTick, this.xo, this.x) - pos.x());
        float baseY = (float)(Math.max(Mth.lerp((double)partialTick, this.yo, this.y) + mY, this.minY + 0.1) - pos.y());
        float baseZ = (float)(Mth.lerp((double)partialTick, this.zo, this.z) - pos.z());
        Quaternionf rotation = new Quaternionf();
        this.getFacingCameraMode().setRotation(rotation, camera, partialTick);
        renderRotatedQuad(buffer, rotation, baseX + mX, baseY, baseZ + mZ, partialTick);
    }

    @Override
    protected float getU0() {
        return (this.currentCell % ROW_WIDTH) * CELL;
    }

    @Override
    protected float getU1() {
        return getU0() + CELL;
    }

    @Override
    protected float getV0() {
        return (this.currentCell / ROW_WIDTH) * CELL;
    }

    @Override
    protected float getV1() {
        return getV0() + CELL;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return TCParticleLayer.TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float partialTick) {
        return QUAD_RADIUS;
    }

    public static final class Provider implements ParticleProvider<SmokeSpiralData> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SmokeSpiralData options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
            return new SmokeSpiralParticle(level, x, y, z, options);
        }
    }
}
