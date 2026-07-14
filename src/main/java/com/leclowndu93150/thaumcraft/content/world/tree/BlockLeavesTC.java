package com.leclowndu93150.thaumcraft.content.world.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class BlockLeavesTC extends LeavesBlock {
    public static final MapCodec<BlockLeavesTC> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(e -> e.leafParticleChance),
                    propertiesCodec()
            ).apply(i, BlockLeavesTC::new)
    );

    private final float leafParticleChance;

    public BlockLeavesTC(float leafParticleChance, BlockBehaviour.Properties properties) {
        super(properties);
        this.leafParticleChance = leafParticleChance;
    }

    @Override
    public MapCodec<BlockLeavesTC> codec() {
        return CODEC;
    }
}
