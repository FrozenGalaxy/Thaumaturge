package com.leclowndu93150.thaumcraft.client.fx.render.particle;

import com.leclowndu93150.thaumcraft.content.fx.data.InfusionCrumbsData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public final class InfusionCrumbsParticle extends HomingParticleBase {
    private final TextureAtlasSprite sprite;
    private final float uo;
    private final float vo;

    private InfusionCrumbsParticle(ClientLevel level, double x, double y, double z, InfusionCrumbsData data, TextureAtlasSprite sprite) {
        super(level, x, y, z, data.tx(), data.ty(), data.tz(), 0.005F);
        this.sprite = sprite;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
        this.gravity = 0.01F;
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;
        this.alpha = 0.3F;
        this.quadSize = this.random.nextFloat() * 0.3F + 0.4F;
        this.xd = data.sx() + this.random.nextGaussian() * 0.01;
        this.yd = data.sy() + this.random.nextGaussian() * 0.01;
        this.zd = data.sz() + this.random.nextGaussian() * 0.01;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F);
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize * 0.1F;
    }

    public static final class Provider implements ParticleProvider<InfusionCrumbsData> {
        @Override
        public @Nullable Particle createParticle(InfusionCrumbsData options, ClientLevel level, double x, double y, double z, double xa, double ya, double za) {
            TextureAtlasSprite sprite = resolveSprite(options, level);
            if (sprite == null) {
                return null;
            }
            return new InfusionCrumbsParticle(level, x, y, z, options, sprite);
        }

        private @Nullable TextureAtlasSprite resolveSprite(InfusionCrumbsData options, ClientLevel level) {
            ItemStack stack = options.stack();
            if (stack.getItem() instanceof BlockItem blockItem) {
                BlockState state = blockItem.getBlock().defaultBlockState();
                if (!state.isAir()) {
                    return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state);
                }
            }
            return Minecraft.getInstance().getItemRenderer().getModel(stack, level, null, 0).getParticleIcon();
        }
    }
}
