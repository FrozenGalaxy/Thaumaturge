package com.leclowndu93150.thaumcraft.content.equipment;

import com.leclowndu93150.thaumcraft.api.items.IVisDiscountGear;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public final class RobeArmorItem extends ArmorItem implements IVisDiscountGear {
    private final int visDiscount;

    public RobeArmorItem(int visDiscount, ArmorItem.Type type, Properties properties) {
        super(TCMaterials.ARMOR_ROBES, type, properties);
        this.visDiscount = visDiscount;
    }

    @Override
    public int getVisDiscount(ItemStack stack) {
        return visDiscount;
    }
}
