package com.leclowndu93150.thaumcraft.client.fx.render.particle;

import com.leclowndu93150.thaumcraft.content.fx.data.EssentiaDropParticleData;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.FastColor.ARGB32;
import org.joml.Vector3f;

public final class EssentiaDropParticle extends TextureSheetParticle {
    private final float baseAlpha;
    private final float baseScale;

    private EssentiaDropParticle(ClientLevel level, double x, double y, double z, EssentiaDropParticleData data, SpriteSet sprites) {
        super(level, x, y, z,
                level.getRandom().nextGaussian() * 0.005,
                level.getRandom().nextGaussian() * 0.005,
                level.getRandom().nextGaussian() * 0.005);
        this.setSprite(sprites.get(level.getRandom()));
        Vector3f rgb = new Vector3f(ARGB32.red(data.color()) / 255.0F, ARGB32.green(data.color()) / 255.0F, ARGB32.blue(data.color()) / 255.0F);
        this.rCol = rgb.x();
        this.gCol = rgb.y();
        this.bCol = rgb.z();
        this.baseAlpha = data.alpha();
        this.alpha = data.alpha();
        this.lifetime = 20 + level.getRandom().nextInt(10);
        this.baseScale = 0.4F + level.getRandom().nextFloat() * 0.2F;
        this.quadSize = this.baseScale;
        this.gravity = 0.01F;
        this.hasPhysics = true;
        this.friction = 0.98F;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.yd -= 0.04 * this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;
        float fade = 1.0F - (float)this.age / this.lifetime;
        this.alpha = this.baseAlpha * fade;
        this.quadSize = this.baseScale * (0.5F + fade * 0.5F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static final class Provider implements ParticleProvider<EssentiaDropParticleData> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(EssentiaDropParticleData options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
            return new EssentiaDropParticle(level, x, y, z, options, sprites);
        }
    }
}
