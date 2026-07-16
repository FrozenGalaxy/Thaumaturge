package com.leclowndu93150.thaumcraft.client.color;

import com.leclowndu93150.thaumcraft.content.casters.ItemFocus;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public final class FocusColorTint implements ItemColor {
    private static final int OPAQUE = 0xFF000000;

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        return OPAQUE | ItemFocus.getFocusColor(stack);
    }
}
