package com.leclowndu93150.thaumcraft.client.fx.render.particle;

import com.leclowndu93150.thaumcraft.client.fx.render.texture.TCParticleLayer;
import com.leclowndu93150.thaumcraft.content.fx.data.BoltData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class BoltParticle extends SingleQuadParticle {
    private static final int GRID = 64;
    private static final int FRAME_START = 512;
    private static final int FRAME_COUNT = 16;
    private static final int BOLT_LIFETIME = 3;
    private static final float WAVE_AMPLITUDE_RATE = 10.0F;
    private static final float JITTER = 0.1F;
    private static final float MIN_ALPHA = 0.1F;
    private static final int SUBDIVISIONS = 4;
    private static final float BEAD_SIZE_FACTOR = 1.0F / 6.0F;
    private static final int EMISSIVE_LIGHT = 0x00F000F0;
    private static final int SEED_BOUND = 1000;

    private final double dx;
    private final double dy;
    private final double dz;
    private final float width;
    private final float boltLength;
    private final int steps;
    private final float phase;
    private final long seed;

    private BoltParticle(ClientLevel level, double x, double y, double z, BoltData data) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.setSize(0.02F, 0.02F);
        this.hasPhysics = false;
        this.xd = 0.0;
        this.yd = 0.0;
        this.zd = 0.0;
        this.rCol = data.r();
        this.gCol = data.g();
        this.bCol = data.b();
        this.dx = data.targetX() - x;
        this.dy = data.targetY() - y;
        this.dz = data.targetZ() - z;
        this.width = data.width();
        this.lifetime = BOLT_LIFETIME;
        this.boltLength = (float) (new Vec3(dx, dy, dz).length() * Math.PI);
        this.steps = Math.max(2, (int) boltLength);
        this.phase = (float) (this.random.nextInt(50) * Math.PI);
        this.seed = this.random.nextInt(SEED_BOUND);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTick) {
        Vec3 cam = camera.getPosition();
        float baseX = (float) (this.x - cam.x());
        float baseY = (float) (this.y - cam.y());
        float baseZ = (float) (this.z - cam.z());
        float alpha = Mth.clamp(1.0F - (this.age + partialTick) / this.lifetime, MIN_ALPHA, 1.0F);
        int color = ARGB32.colorFromFloat(alpha, this.rCol, this.gCol, this.bCol);
        float ampl = (this.age + partialTick) / WAVE_AMPLITUDE_RATE;
        RandomSource jitterSource = RandomSource.create(this.seed);
        float beadSize = this.width * BEAD_SIZE_FACTOR;
        int frame = FRAME_START + (this.age + (int) this.seed) % FRAME_COUNT;
        float texFrame = 1.0F / GRID;
        float u0 = (frame % GRID) * texFrame;
        float v0 = (frame / GRID) * texFrame;
        Quaternionf rotation = camera.rotation();

        float prevX = 0.0F;
        float prevY = 0.0F;
        float prevZ = 0.0F;
        for (int step = 1; step <= this.steps; step++) {
            float px;
            float py;
            float pz;
            if (step == this.steps) {
                px = (float) this.dx;
                py = (float) this.dy;
                pz = (float) this.dz;
            } else {
                float dist = step * (this.boltLength / this.steps) + this.phase;
                px = (float) (this.dx / this.steps * step) + Mth.sin(dist / 4.0F) * ampl
                        + (jitterSource.nextFloat() - jitterSource.nextFloat()) * JITTER;
                py = (float) (this.dy / this.steps * step) + Mth.sin(dist / 3.0F) * ampl
                        + (jitterSource.nextFloat() - jitterSource.nextFloat()) * JITTER;
                pz = (float) (this.dz / this.steps * step) + Mth.sin(dist / 2.0F) * ampl
                        + (jitterSource.nextFloat() - jitterSource.nextFloat()) * JITTER;
            }
            for (int sub = 0; sub < SUBDIVISIONS; sub++) {
                float t = (sub + 1.0F) / SUBDIVISIONS;
                float bx = baseX + Mth.lerp(t, prevX, px);
                float by = baseY + Mth.lerp(t, prevY, py);
                float bz = baseZ + Mth.lerp(t, prevZ, pz);
                emitQuad(buffer, rotation, bx, by, bz, beadSize,
                        u0, u0 + texFrame, v0, v0 + texFrame, color, EMISSIVE_LIGHT);
            }
            prevX = px;
            prevY = py;
            prevZ = pz;
        }
    }

    private static void emitQuad(VertexConsumer buffer, Quaternionf rotation, float x, float y, float z,
                                 float size, float u0, float u1, float v0, float v1, int color, int light) {
        emitVertex(buffer, rotation, x, y, z, 1.0F, -1.0F, size, u1, v1, color, light);
        emitVertex(buffer, rotation, x, y, z, 1.0F, 1.0F, size, u1, v0, color, light);
        emitVertex(buffer, rotation, x, y, z, -1.0F, 1.0F, size, u0, v0, color, light);
        emitVertex(buffer, rotation, x, y, z, -1.0F, -1.0F, size, u0, v1, color, light);
    }

    private static void emitVertex(VertexConsumer buffer, Quaternionf rotation, float x, float y, float z,
                                   float cornerX, float cornerY, float size, float u, float v, int color, int light) {
        Vector3f pos = new Vector3f(cornerX, cornerY, 0.0F).rotate(rotation).mul(size).add(x, y, z);
        buffer.addVertex(pos.x(), pos.y(), pos.z()).setUv(u, v).setColor(color).setLight(light);
    }

    @Override
    protected float getU0() {
        return 0.0F;
    }

    @Override
    protected float getU1() {
        return 1.0F / GRID;
    }

    @Override
    protected float getV0() {
        return 0.0F;
    }

    @Override
    protected float getV1() {
        return 1.0F / GRID;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return TCParticleLayer.ADDITIVE;
    }

    @Override
    public float getQuadSize(float partialTick) {
        return this.width * BEAD_SIZE_FACTOR;
    }

    public static final class Provider implements ParticleProvider<BoltData> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(BoltData options, ClientLevel level, double x, double y, double z,
                                       double xAux, double yAux, double zAux) {
            return new BoltParticle(level, x, y, z, options);
        }
    }
}
