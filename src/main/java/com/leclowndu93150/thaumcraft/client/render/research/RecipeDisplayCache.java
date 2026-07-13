package com.leclowndu93150.thaumcraft.client.render.research;

import com.leclowndu93150.thaumcraft.network.ServerboundRequestRecipeDisplayPayload;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

public final class RecipeDisplayCache {
    private static final Map<ResourceLocation, List<RecipeDisplay>> CACHE = new HashMap<>();
    private static final Set<ResourceLocation> REQUESTED = new HashSet<>();

    private RecipeDisplayCache() {}

    public static @Nullable List<RecipeDisplay> get(ResourceLocation id) {
        return CACHE.get(id);
    }

    public static void ensureRequested(ResourceLocation id) {
        if (CACHE.containsKey(id) || REQUESTED.contains(id)) return;
        REQUESTED.add(id);
        PacketDistributor.sendToServer(new ServerboundRequestRecipeDisplayPayload(id));
    }

    public static void put(ResourceLocation id, List<RecipeDisplay> displays) {
        CACHE.put(id, displays);
        REQUESTED.remove(id);
    }

    public static void clear() {
        CACHE.clear();
        REQUESTED.clear();
    }
}
