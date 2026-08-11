package com.leclowndu93150.thaumaturge.content.essentia.jar;

import com.leclowndu93150.thaumaturge.api.essentia.IEssentiaJar;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public final class BlockJarCustom extends BlockJar {
    public static final MapCodec<BlockJarCustom> CODEC = simpleCodec(props -> new BlockJarCustom(
            props,
            IEssentiaJar.DEFAULT_CAPACITY,
            IEssentiaJar.DEFAULT_SIDE_TEXTURE,
            IEssentiaJar.DEFAULT_TOP_TEXTURE,
            IEssentiaJar.DEFAULT_BOTTOM_TEXTURE));

    private final int capacity;
    private final ResourceLocation sideTexture;
    private final ResourceLocation topTexture;
    private final ResourceLocation bottomTexture;

    public BlockJarCustom(
            Properties properties,
            int capacity,
            ResourceLocation sideTexture,
            ResourceLocation topTexture,
            ResourceLocation bottomTexture) {
        super(properties);
        this.capacity = capacity;
        this.sideTexture = sideTexture;
        this.topTexture = topTexture;
        this.bottomTexture = bottomTexture;
    }

    @Override
    protected MapCodec<? extends BlockJar> codec() {
        return CODEC;
    }

    @Override
    public int jarCapacity() {
        return capacity;
    }

    @Override
    public ResourceLocation jarSideTexture() {
        return sideTexture;
    }

    @Override
    public ResourceLocation jarTopTexture() {
        return topTexture;
    }

    @Override
    public ResourceLocation jarBottomTexture() {
        return bottomTexture;
    }
}
