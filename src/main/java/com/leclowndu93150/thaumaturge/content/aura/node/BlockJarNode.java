package com.leclowndu93150.thaumaturge.content.aura.node;

import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.api.capability.KnowledgeAccess;
import com.leclowndu93150.thaumaturge.api.casters.ICaster;
import com.leclowndu93150.thaumaturge.config.ThaumaturgeCommonConfig;
import com.leclowndu93150.thaumaturge.content.misc.TCActionBar;
import com.leclowndu93150.thaumaturge.registry.TCBlockEntities;
import com.leclowndu93150.thaumaturge.registry.TCBlocks;
import com.leclowndu93150.thaumaturge.registry.TCSounds;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public final class BlockJarNode extends Block implements EntityBlock {
    public static final MapCodec<BlockJarNode> CODEC = simpleCodec(BlockJarNode::new);
    public static final ResourceLocation EFFECTS_RESEARCH = TCIds.rl("jar_node_effects");

    private static final VoxelShape SHAPE = box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0);

    public BlockJarNode(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<BlockJarNode> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        if (stack.getItem() instanceof ICaster caster) {
            if (!(level instanceof ServerLevel serverLevel)) {
                return ItemInteractionResult.SUCCESS;
            }
            if (serverLevel.getBlockEntity(pos) instanceof BlockEntityJarNode jar) {
                if (player.isShiftKeyDown()
                        && ThaumaturgeCommonConfig.hasJarNodeEffectsEnabled()
                        && KnowledgeAccess.of(player).isResearchComplete(EFFECTS_RESEARCH)) {
                    boolean active = !jar.areEffectsActive();
                    if (active) {
                        if (!jar.canAwakenEffects()) {
                            return ItemInteractionResult.SUCCESS;
                        }
                        int activationVisCost = ThaumaturgeCommonConfig.JAR_NODE_EFFECTS_ACTIVATION_VIS_COST.get();
                        if (activationVisCost > 0
                                && !caster.consumeVis(stack, player, activationVisCost, false, false)) {
                            TCActionBar.sendPurple(player, "tc.jar.effects.vis");
                            return ItemInteractionResult.SUCCESS;
                        }
                    }
                    jar.setEffectsActive(active);
                    serverLevel.playSound(
                            null, pos, TCSounds.WAND.get(), SoundSource.BLOCKS, 0.7F, active ? 0.8F : 1.2F);
                    return ItemInteractionResult.SUCCESS;
                }

                NodeData data = new NodeData(
                        jar.getNodeType(),
                        Optional.ofNullable(jar.getNodeModifier()),
                        jar.getAspects(),
                        jar.getAspectsBase());
                serverLevel.removeBlockEntity(pos);
                serverLevel.setBlock(pos, TCBlocks.NODE.get().defaultBlockState(), Block.UPDATE_ALL);
                if (serverLevel.getBlockEntity(pos) instanceof BlockEntityNode node) {
                    node.applyNodeData(data);
                    node.setChanged();
                    serverLevel.sendBlockUpdated(pos, node.getBlockState(), node.getBlockState(), Block.UPDATE_ALL);
                }
                serverLevel.levelEvent(2001, pos, Block.getId(state));
            }
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityJarNode(pos, state);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (type != TCBlockEntities.JAR_NODE.get()) {
            return null;
        }
        if (level.isClientSide()) {
            return (tickLevel, pos, tickState, node) -> ((BlockEntityJarNode) node).clientTick(tickLevel, pos);
        }
        return (tickLevel, pos, tickState, node) -> ((BlockEntityJarNode) node).serverTick(tickLevel, pos);
    }
}
