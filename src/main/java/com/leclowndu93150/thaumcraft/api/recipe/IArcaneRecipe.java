package com.leclowndu93150.thaumcraft.api.recipe;

import com.leclowndu93150.thaumcraft.api.aspect.AspectList;
import net.minecraft.world.item.crafting.Recipe;

/**
 *
 * @since 1.0.0
 */

public interface IArcaneRecipe extends ResearchGated, Recipe<IArcaneCraftingInput> {
    int getBaseVis();

    AspectList getCrystals();
}
