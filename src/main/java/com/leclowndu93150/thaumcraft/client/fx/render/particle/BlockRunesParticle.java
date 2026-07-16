package com.leclowndu93150.thaumcraft.client.fx.render.particle;

import com.leclowndu93150.thaumcraft.client.fx.render.texture.TCParticleLayer;
import com.leclowndu93150.thaumcraft.content.fx.data.BlockRunesData;
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
import org.joml.Vector3f;

public final class BlockRunesParticle extends SingleQuadParticle {
    private static final int GRID = 64;
    private static final int RUNE_ROW = 6;
    private static final int RUNE_COUNT = 16;
    private static final float QUAD_RADIUS = 0.3F;
    private static final float FACE_OFFSET = -0.51F;
    private static final int EMISSIVE_LIGHT = 0x00F000F0;

    private final int runeColumn;
    private final float rotationY;
    private final double offsetX;
    private final double offsetY;
    private final float baseScale;
    private float currentAlpha = 0.0F;

    private BlockRunesParticle(ClientLevel level, double x, double y, double z, BlockRunesData data) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.setSize(0.01F, 0.01F);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.hasPhysics = false;
        this.gravity = data.gravity();
        float r = data.r() == 0.0F ? 1.0F : data.r();
        this.rCol = r;
        this.gCol = data.g();
        this.bCol = data.b();
        this.lifetime = 3 * Math.max(1, data.duration());
        this.runeColumn = this.random.nextInt(RUNE_COUNT);
        this.rotationY = this.random.nextInt(4) * 90.0F;
        if (data.variant()) {
            this.offsetX = 0.0;
        } else {
            this.offsetX = this.random.nextFloat() * 0.2;
        }
        this.offsetY = -0.3 + this.random.nextFloat() * 0.6;
        this.baseScale = data.variant()
                ? (float)(0.5 + this.random.nextGaussian() * 0.1F)
                : (float)(1.0 + this.random.nextGaussian() * 0.1F);
        this.alpha = 0.0F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float threshold = this.lifetime / 5.0F;
        if (this.age <= threshold) {
            this.currentAlpha = this.age / threshold;
        } else {
            this.currentAlpha = (float)(this.lifetime - this.age) / this.lifetime;
        }
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.alpha = this.currentAlpha / 2.0F;
        this.yd -= 0.04 * this.gravity;
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        Vec3 pos = camera.getPosition();
        float dx = (float)(Mth.lerp((double)partialTick, this.xo, this.x) - pos.x());
        float dy = (float)(Mth.lerp((double)partialTick, this.yo, this.y) - pos.y());
        float dz = (float)(Mth.lerp((double)partialTick, this.zo, this.z) - pos.z());
        Quaternionf rot = new Quaternionf().rotateY((float)Math.toRadians(this.rotationY));
        Vector3f offset = rot.transform(new Vector3f((float)-this.offsetY, (float)this.offsetX, FACE_OFFSET));
        renderRotatedQuad(buffer, rot, dx + offset.x(), dy + offset.y(), dz + offset.z(), partialTick);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return EMISSIVE_LIGHT;
    }

    @Override
    protected float getU0() {
        return this.runeColumn * (1.0F / GRID);
    }

    @Override
    protected float getU1() {
        return this.runeColumn * (1.0F / GRID) + (1.0F / GRID);
    }

    @Override
    protected float getV0() {
        return RUNE_ROW * (1.0F / GRID);
    }

    @Override
    protected float getV1() {
        return RUNE_ROW * (1.0F / GRID) + (1.0F / GRID);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return TCParticleLayer.ADDITIVE;
    }

    @Override
    public float getQuadSize(float partialTick) {
        return QUAD_RADIUS * this.baseScale;
    }

    public static final class Provider implements ParticleProvider<BlockRunesData> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(BlockRunesData options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
            return new BlockRunesParticle(level, x, y, z, options);
        }
    }
}
