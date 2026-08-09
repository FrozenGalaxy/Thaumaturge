package com.leclowndu93150.thaumaturge.client.render.research;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public final class RecipeDisplayCache {
    private RecipeDisplayCache() {}

    public static List<RecipeHolder<?>> get(ResourceLocation id) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return List.of();
        }
        return mc.level.getRecipeManager().byKey(id)
                .<List<RecipeHolder<?>>>map(List::of)
                .orElseGet(List::of);
    }

    public static void ensureRequested(ResourceLocation id) {
    }

    public static void clear() {
    }
}
