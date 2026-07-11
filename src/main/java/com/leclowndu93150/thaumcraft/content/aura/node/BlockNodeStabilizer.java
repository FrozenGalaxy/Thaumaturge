package com.leclowndu93150.thaumcraft.content.aura.node;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class BlockNodeStabilizer extends Block {
    private static final MapCodec<BlockNodeStabilizer> CODEC = simpleCodec(props -> new BlockNodeStabilizer(props, false));

    private final boolean advanced;

    public BlockNodeStabilizer(BlockBehaviour.Properties properties, boolean advanced) {
        super(properties);
        this.advanced = advanced;
    }

    @Override
    protected MapCodec<BlockNodeStabilizer> codec() {
        return CODEC;
    }

    public boolean isAdvanced() {
        return advanced;
    }
}
