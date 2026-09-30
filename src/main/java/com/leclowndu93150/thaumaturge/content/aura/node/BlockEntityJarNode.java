package com.leclowndu93150.thaumaturge.content.aura.node;

import com.leclowndu93150.thaumaturge.api.nodes.NodeType;
import com.leclowndu93150.thaumaturge.config.ThaumaturgeCommonConfig;
import com.leclowndu93150.thaumaturge.registry.TCBlockEntities;
import com.leclowndu93150.thaumaturge.registry.TCDataComponents;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityJarNode extends BlockEntityNode {
    private static final int JARRED_FEED_FACTOR = 2;
    private static final int TAINTED_EFFECT_INTERVAL_FACTOR = 4;
    private boolean effectsActive;

    public BlockEntityJarNode(BlockPos pos, BlockState state) {
        super(TCBlockEntities.JAR_NODE.get(), pos, state);
    }

    @Override
    protected boolean allowDischarge() {
        return false;
    }

    @Override
    protected boolean allowTypeBehavior() {
        return areEffectsRunning();
    }

    public boolean canAwakenEffects() {
        if (!ThaumaturgeCommonConfig.JAR_NODE_EFFECTS_ENABLED.get()) {
            return false;
        }
        return switch (getNodeType()) {
            case DARK -> ThaumaturgeCommonConfig.JAR_DARK_NODE_EFFECTS_ENABLED.get();
            case TAINTED -> ThaumaturgeCommonConfig.JAR_TAINTED_NODE_EFFECTS_ENABLED.get();
            case HUNGRY -> ThaumaturgeCommonConfig.JAR_HUNGRY_NODE_EFFECTS_ENABLED.get();
            case PURE -> ThaumaturgeCommonConfig.JAR_PURE_NODE_EFFECTS_ENABLED.get();
            case NORMAL, UNSTABLE -> false;
        };
    }

    public boolean areEffectsRunning() {
        return effectsActive && canAwakenEffects();
    }

    @Override
    protected boolean consumesDarkSpawnAspects() {
        return areEffectsRunning() && getNodeType() == NodeType.DARK;
    }

    @Override
    protected int taintedEffectIntervalFactor() {
        return areEffectsRunning() && getNodeType() == NodeType.TAINTED ? TAINTED_EFFECT_INTERVAL_FACTOR : 1;
    }

    @Override
    protected float taintedFluxOutputFactor() {
        return areEffectsRunning() && getNodeType() == NodeType.TAINTED ? 0.25F : 1.0F;
    }

    @Override
    protected boolean shouldGainDevouredAspects(RandomSource random) {
        if (!areEffectsRunning() || getNodeType() != NodeType.HUNGRY) {
            return true;
        }
        return random.nextDouble() * 100.0 < ThaumaturgeCommonConfig.JAR_HUNGRY_NODE_ASPECT_GAIN_CHANCE.get();
    }

    public boolean areEffectsActive() {
        return effectsActive;
    }

    public void setEffectsActive(boolean active) {
        if (effectsActive == active) {
            return;
        }
        effectsActive = active;
        nodeChange();
    }

    @Override
    protected int feedingIntervalFactor() {
        return JARRED_FEED_FACTOR * super.feedingIntervalFactor();
    }

    @Override
    public void applyImplicitComponents(DataComponentInput components) {
        super.applyImplicitComponents(components);
        NodeData data = components.get(TCDataComponents.NODE_DATA.get());
        if (data != null) {
            applyNodeData(data);
        }
        effectsActive = Boolean.TRUE.equals(components.get(TCDataComponents.JAR_NODE_EFFECTS_ACTIVE.get()));
    }

    @Override
    public void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(
                TCDataComponents.NODE_DATA.get(),
                new NodeData(getNodeType(), Optional.ofNullable(getNodeModifier()), getAspects(), getAspectsBase()));
        components.set(TCDataComponents.JAR_NODE_EFFECTS_ACTIVE.get(), effectsActive);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag output) {
        output.remove("Type");
        output.remove("Modifier");
        output.remove("Aspects");
        output.remove("AspectsBase");
        output.remove("JarNodeEffectsActive");
    }

    @Override
    protected void saveAdditional(CompoundTag output, HolderLookup.Provider registries) {
        super.saveAdditional(output, registries);
        if (effectsActive) {
            output.putBoolean("JarNodeEffectsActive", true);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag input, HolderLookup.Provider registries) {
        super.loadAdditional(input, registries);
        effectsActive = input.getBoolean("JarNodeEffectsActive");
    }
}
