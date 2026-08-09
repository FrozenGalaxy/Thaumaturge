package com.leclowndu93150.thaumaturge.api.recipe;

import com.leclowndu93150.thaumaturge.api.aspect.AspectList;
import net.minecraft.world.item.ItemStack;

/**
 * Contract for the arcane workbench inventory, exposing the crystal payment currently loaded in
 * its crystal slots.
 *
 * @since 1.0.0
 */
public interface IArcaneWorkbench {
    /**
     * The aspects currently available as essentia crystals loaded in the crystal slots, summed by
     * aspect. Each unit corresponds to one crystal that can pay part of a recipe's crystal cost.
     *
     * @return the available crystal aspects, or {@link AspectList#EMPTY} when no crystals are loaded
     */
    AspectList availableCrystals();

    /**
     * The wand currently loaded in the wand slot, or {@link ItemStack#EMPTY} when no wand is loaded.
     *
     * @return the loaded wand stack
     */
    ItemStack wandStack();
}
