package com.leclowndu93150.thaumaturge.content.crucible;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public abstract class CrucibleEvent extends BlockEvent {
    private final Player player;
    private final BlockEntityCrucible crucible;

    protected CrucibleEvent(Player player, BlockPos pos, BlockState state, BlockEntityCrucible crucible) {
        super(player.level(), pos, state);
        this.player = player;
        this.crucible = crucible;
    }

    public Player getPlayer() {
        return player;
    }

    public BlockEntityCrucible getCrucible() {
        return crucible;
    }

    public static final class CrucibleCraftedEvent extends CrucibleEvent {
        private final ItemStack craftedStack;
        private final AspectList consumedAspects;

        public CrucibleCraftedEvent(
                Player player,
                BlockPos pos,
                BlockState state,
                BlockEntityCrucible crucible,
                ItemStack stack,
                AspectList consumedAspects) {
            super(player, pos, state, crucible);
            this.craftedStack = stack;
            this.consumedAspects = consumedAspects;
        }

        public ItemStack getCraftedStack() {
            return craftedStack;
        }

        public AspectList getConsumedAspects() {
            return consumedAspects;
        }
    }

    public static final class CrucibleDecomposeItemEvent extends CrucibleEvent implements ICancellableEvent {
        private final ItemStack stack;
        private AspectList aspects;

        public CrucibleDecomposeItemEvent(
                Player player,
                BlockPos pos,
                BlockState state,
                BlockEntityCrucible crucible,
                ItemStack stack,
                AspectList aspects) {
            super(player, pos, state, crucible);
            this.stack = stack;
            this.aspects = aspects;
        }

        public ItemStack getStack() {
            return stack;
        }

        public AspectList getAspects() {
            return aspects;
        }

        public void setAspects(AspectList aspects) {
            this.aspects = aspects;
        }
    }
}
