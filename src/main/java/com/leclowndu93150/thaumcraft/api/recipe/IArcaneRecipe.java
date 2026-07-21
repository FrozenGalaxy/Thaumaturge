package com.leclowndu93150.thaumcraft.api.recipe;

import com.leclowndu93150.thaumcraft.api.aspect.AspectList;
import com.leclowndu93150.thaumcraft.content.recipe.workbench.ArcaneCraftingInput;
import net.minecraft.world.item.crafting.Recipe;

/**
 *
 * @since 1.0.0
 */

public interface IArcaneRecipe extends ResearchGated, Recipe<ArcaneCraftingInput> {
    int getBaseVis();

    AspectList getCrystals();
}
