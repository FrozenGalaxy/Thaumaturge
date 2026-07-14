package com.leclowndu93150.thaumcraft.content.equipment;

import com.leclowndu93150.thaumcraft.api.items.IGoggles;
import com.leclowndu93150.thaumcraft.api.items.IRevealer;
import com.leclowndu93150.thaumcraft.api.items.IVisDiscountGear;
import com.leclowndu93150.thaumcraft.api.items.IWarpingGear;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class VoidRobeArmorItem extends ArmorItem
        implements IVisDiscountGear, IWarpingGear, IGoggles, IRevealer {
    private static final int VIS_DISCOUNT = 5;
    private static final int WARP = 3;

    public VoidRobeArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Properties properties) {
        super(material, type, properties);
    }

    private boolean isHelm(ItemStack stack) {
        return getType() == ArmorItem.Type.HELMET;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (!level.isClientSide()) {
            VoidGearItem.selfRepairTick(stack, entity);
        }
    }

    @Override
    public int getVisDiscount(ItemStack stack) {
        return VIS_DISCOUNT;
    }

    @Override
    public int getWarp(ItemStack stack, LivingEntity wearer) {
        return WARP;
    }

    @Override
    public boolean showIngamePopups(ItemStack stack, LivingEntity wearer) {
        return isHelm(stack);
    }

    @Override
    public boolean showNodes(ItemStack stack, LivingEntity wearer) {
        return isHelm(stack);
    }
}
