package com.leclowndu93150.thaumcraft.content.equipment;

import com.leclowndu93150.thaumcraft.api.items.IWarpingGear;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;

public final class VoidShovelItem extends ShovelItem implements IWarpingGear {
    public VoidShovelItem(Properties properties) {
        super(TCMaterials.TOOL_VOID, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide()) {
            VoidGearItem.selfRepairTick(stack, entity);
        }
    }

    @Override
    public int getWarp(ItemStack stack, LivingEntity wearer) {
        return VoidGearItem.VOID_GEAR_WARP;
    }
}
