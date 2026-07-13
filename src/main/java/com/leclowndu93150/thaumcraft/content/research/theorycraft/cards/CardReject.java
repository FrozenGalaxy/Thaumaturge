package com.leclowndu93150.thaumcraft.content.research.theorycraft.cards;

import com.leclowndu93150.thaumcraft.serialization.TCNbt;
import com.leclowndu93150.thaumcraft.api.research.CategoryComponents;
import com.leclowndu93150.thaumcraft.api.research.IResearchCategory;
import com.leclowndu93150.thaumcraft.api.research.TCResearchCategories;
import com.leclowndu93150.thaumcraft.api.research.theorycraft.IResearchTableData;
import com.leclowndu93150.thaumcraft.api.research.theorycraft.TheorycraftCard;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

public final class CardReject extends TheorycraftCard {
    private @Nullable ResourceKey<IResearchCategory> target;

    @Override
    public int inspirationCost() {
        return 0;
    }

    @Override
    public Component name() {
        return Component.translatable(
                "card.thaumcraft.reject.name",
                target == null ? Component.empty() : CategoryComponents.emphasised(target));
    }

    @Override
    public Component description() {
        return Component.translatable(
                "card.thaumcraft.reject.text",
                target == null ? Component.empty() : CategoryComponents.emphasised(target));
    }

    @Override
    public boolean initialize(ServerPlayer player, IResearchTableData data) {
        Random rng = new Random(seed());
        List<ResourceKey<IResearchCategory>> options = new ArrayList<>();
        for (ResourceKey<IResearchCategory> key : data.categoriesWithTotal()) {
            if (!data.isCategoryBlocked(key)) options.add(key);
        }
        if (options.isEmpty()) return false;
        target = options.get(rng.nextInt(options.size()));
        return target != null;
    }

    @Override
    public boolean activate(ServerPlayer player, IResearchTableData data) {
        if (target == null) return false;
        data.addCategoryTotal(TCResearchCategories.BASICS, 5);
        data.blockCategory(target);
        return true;
    }

    @Override
    public void write(CompoundTag output, HolderLookup.Provider registries) {
        super.write(output, registries);
        if (target != null) {
            TCNbt.store(output, "target", ResourceKey.codec(IResearchCategory.REGISTRY_KEY), registries, target);
        }
    }

    @Override
    public void read(CompoundTag input, HolderLookup.Provider registries) {
        super.read(input, registries);
        target = TCNbt.read(input, "target", ResourceKey.codec(IResearchCategory.REGISTRY_KEY), registries).orElse(null);
    }
}
