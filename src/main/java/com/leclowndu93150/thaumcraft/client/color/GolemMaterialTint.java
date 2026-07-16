package com.leclowndu93150.thaumcraft.client.color;

import com.leclowndu93150.thaumcraft.content.golem.GolemProperties;
import com.leclowndu93150.thaumcraft.registry.TCDataComponents;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;

public final class GolemMaterialTint implements ItemColor {
    private static final int OPAQUE = 0xFF000000;
    private static final int DEFAULT = 0xFFFFFFFF;

    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        GolemProperties props = stack.get(TCDataComponents.GOLEM_PROPERTIES.get());
        if (props == null) {
            return DEFAULT;
        }
        return OPAQUE | props.getMaterial().itemColor();
    }
}
