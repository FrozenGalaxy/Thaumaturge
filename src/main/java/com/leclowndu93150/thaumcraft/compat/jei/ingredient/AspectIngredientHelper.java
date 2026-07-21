package com.leclowndu93150.thaumcraft.compat.jei.ingredient;

import com.leclowndu93150.thaumcraft.api.aspect.AspectComponents;
import com.leclowndu93150.thaumcraft.api.aspect.AspectInstance;
import com.leclowndu93150.thaumcraft.api.aspect.IAspect;
import com.leclowndu93150.thaumcraft.content.item.PhialItem;
import com.leclowndu93150.thaumcraft.content.research.pool.AspectDiscoveryView;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;

public final class AspectIngredientHelper implements IIngredientHelper<AspectInstance> {
    public static final AspectIngredientHelper INSTANCE = new AspectIngredientHelper();

    private static final ResourceLocation UNKNOWN = ResourceLocation.fromNamespaceAndPath("thaumcraft", "unknown");

    private AspectIngredientHelper() {}

    @Override
    public IIngredientType<AspectInstance> getIngredientType() {
        return AspectIngredientType.INSTANCE;
    }

    @Override
    public String getDisplayName(AspectInstance ingredient) {
        if (!AspectDiscoveryView.isDiscovered(ingredient.aspect())) {
            return Component.translatable("tc.aspect.unknown").getString();
        }
        return AspectComponents.name(ingredient.aspect()).getString();
    }

    @Override
    public String getUniqueId(AspectInstance ingredient, UidContext context) {
        return ingredient.aspect().unwrapKey()
                .map(ResourceKey::location)
                .orElse(UNKNOWN)
                .toString();
    }

    @Override
    public ResourceLocation getResourceLocation(AspectInstance ingredient) {
        return ingredient.aspect().unwrapKey()
                .map(ResourceKey::location)
                .orElse(UNKNOWN);
    }

    @Override
    public AspectInstance copyIngredient(AspectInstance ingredient) {
        return ingredient;
    }

    @Override
    public String getErrorInfo(AspectInstance ingredient) {
        if (ingredient == null) {
            return "null";
        }
        return ingredient.aspect().unwrapKey()
                .map(key -> key.location().toString())
                .orElse("unbound aspect holder");
    }

    @Override
    public boolean isValidIngredient(AspectInstance ingredient) {
        return ingredient != null && ingredient.aspect().isBound();
    }

    @Override
    public ItemStack getCheatItemStack(AspectInstance ingredient) {
        return PhialItem.makeFilled(ingredient.aspect());
    }
}
