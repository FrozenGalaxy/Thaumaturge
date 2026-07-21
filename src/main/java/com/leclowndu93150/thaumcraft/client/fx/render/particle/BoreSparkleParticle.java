package com.leclowndu93150.thaumcraft.client.fx.render.particle;

import com.leclowndu93150.thaumcraft.content.fx.data.BoreSparkleData;
import com.leclowndu93150.thaumcraft.client.fx.render.texture.TCParticleLayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

public final class BoreSparkleParticle extends HomingParticleBase {
    private static final int GRID_SIZE = 64;
    private static final int FRAME_START = 256;
    private static final int FRAME_COUNT = 4;

    private BoreSparkleParticle(ClientLevel level, double x, double y, double z, BoreSparkleData data) {
        super(level, x, y, z, data.targetEntityId(), data.tx(), data.ty(), data.tz(),
                0.0, 0.0, 0.0, 0.5F, 0.5F, 0.2F, 0.01F);
        this.rCol = data.r();
        this.gCol = data.g();
        this.bCol = data.b();
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float bob = Mth.sin(this.age / 3.0F) * 0.5F + 1.0F;
        return this.quadSize * 0.1F * bob;
    }

    @Override
    protected float getU0() {
        return currentCell() % GRID_SIZE / (float) GRID_SIZE;
    }

    @Override
    protected float getU1() {
        return getU0() + 1.0F / GRID_SIZE;
    }

    @Override
    protected float getV0() {
        return currentCell() / GRID_SIZE / (float) GRID_SIZE;
    }

    @Override
    protected float getV1() {
        return getV0() + 1.0F / GRID_SIZE;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return TCParticleLayer.ADDITIVE;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    private int currentCell() {
        return FRAME_START + this.age % FRAME_COUNT;
    }

    public static final class Provider implements ParticleProvider<BoreSparkleData> {
        public Provider(SpriteSet sprites) {
        }

        @Override
        public @Nullable Particle createParticle(BoreSparkleData options, ClientLevel level, double x, double y, double z, double xa, double ya, double za) {
            return new BoreSparkleParticle(level, x, y, z, options);
        }
    }
}
