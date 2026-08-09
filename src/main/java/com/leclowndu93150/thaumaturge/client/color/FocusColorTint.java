package com.leclowndu93150.thaumaturge.client.color;

import com.leclowndu93150.thaumaturge.content.casters.ItemFocus;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public final class FocusColorTint implements ItemColor {
    private static final int OPAQUE = 0xFF000000;

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        return OPAQUE | ItemFocus.getFocusColor(stack);
    }
}
