package com.leclowndu93150.thaumaturge.data.model.crystal;

import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.registry.TCBlocks;
import java.util.function.Consumer;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public final class CrystalBlockstateGenerator {
    private static final ResourceLocation CRYSTAL_MODEL = TCIds.rl("block/crystal");

    private CrystalBlockstateGenerator() {}

    public static void register(Consumer<BlockStateGenerator> blockStateOutput) {
        emit(blockStateOutput, TCBlocks.CRYSTAL_AER.get());
        emit(blockStateOutput, TCBlocks.CRYSTAL_IGNIS.get());
        emit(blockStateOutput, TCBlocks.CRYSTAL_AQUA.get());
        emit(blockStateOutput, TCBlocks.CRYSTAL_TERRA.get());
        emit(blockStateOutput, TCBlocks.CRYSTAL_ORDO.get());
        emit(blockStateOutput, TCBlocks.CRYSTAL_PERDITIO.get());
        emit(blockStateOutput, TCBlocks.CRYSTAL_VITIUM.get());
    }

    private static void emit(Consumer<BlockStateGenerator> blockStateOutput, Block block) {
        blockStateOutput.accept(MultiVariantGenerator.multiVariant(block,
                Variant.variant().with(VariantProperties.MODEL, CRYSTAL_MODEL)));
    }
}
