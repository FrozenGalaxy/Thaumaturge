package com.leclowndu93150.thaumcraft.client.color;

import com.leclowndu93150.thaumcraft.api.aspect.AspectInstance;
import com.leclowndu93150.thaumcraft.registry.TCDataComponents;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.item.ItemStack;

public final class CrystalAspectTint implements ItemColor {
    private static final int FALLBACK = 0xFFFFFF;

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        AspectInstance instance = stack.get(TCDataComponents.CRYSTAL_ASPECT.get());
        if (instance == null) {
            return ARGB32.opaque(FALLBACK);
        }
        return ARGB32.opaque(instance.aspect().value().color());
    }
}
