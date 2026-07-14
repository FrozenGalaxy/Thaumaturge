package com.leclowndu93150.thaumcraft.content.equipment;

import com.leclowndu93150.thaumcraft.api.items.IVisDiscountGear;
import com.leclowndu93150.thaumcraft.api.items.IWarpingGear;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public final class CultistRobeItem extends ArmorItem implements IVisDiscountGear, IWarpingGear {
    private static final int VIS_DISCOUNT = 1;
    private static final int WARP = 1;

    public CultistRobeItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public int getVisDiscount(ItemStack stack) {
        return VIS_DISCOUNT;
    }

    @Override
    public int getWarp(ItemStack stack, LivingEntity wearer) {
        return WARP;
    }
}
