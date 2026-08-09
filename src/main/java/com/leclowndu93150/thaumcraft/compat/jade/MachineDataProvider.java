package com.leclowndu93150.thaumcraft.compat.jade;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.content.essentia.smeltery.BlockEntitySmelter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum MachineDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = TCIds.rl("machine");
    static final int PERCENT = 100;

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof BlockEntitySmelter smelter) {
            tag.putInt("SmeltProgress", smelter.getCookProgressScaled(PERCENT));
            tag.putInt("BurnRemaining", smelter.getBurnTimeRemainingScaled(PERCENT));
        }
    }
}
