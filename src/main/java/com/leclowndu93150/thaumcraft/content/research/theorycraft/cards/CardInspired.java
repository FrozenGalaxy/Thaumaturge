package com.leclowndu93150.thaumcraft.content.research.theorycraft.cards;

import com.leclowndu93150.thaumcraft.serialization.TCNbt;
import com.leclowndu93150.thaumcraft.api.research.CategoryComponents;
import com.leclowndu93150.thaumcraft.api.research.IResearchCategory;
import com.leclowndu93150.thaumcraft.api.research.theorycraft.IResearchTableData;
import com.leclowndu93150.thaumcraft.api.research.theorycraft.TheorycraftCard;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

public final class CardInspired extends TheorycraftCard {
    private @Nullable ResourceKey<IResearchCategory> category;
    private int amount;

    @Override
    public int inspirationCost() {
        return 2;
    }

    @Override
    public @Nullable ResourceKey<IResearchCategory> category() {
        return category;
    }

    @Override
    public Component name() {
        return Component.translatable("card.thaumcraft.inspired.name");
    }

    @Override
    public Component description() {
        return Component.translatable(
                "card.thaumcraft.inspired.text",
                amount,
                category == null ? Component.empty() : CategoryComponents.emphasised(category));
    }

    @Override
    public boolean initialize(ServerPlayer player, IResearchTableData data) {
        if (data.categoriesWithTotal().isEmpty()) return false;
        int highest = 0;
        ResourceKey<IResearchCategory> highestKey = null;
        for (ResourceKey<IResearchCategory> key : data.categoriesWithTotal()) {
            int value = data.categoryTotal(key);
            if (value > highest) {
                highest = value;
                highestKey = key;
            }
        }
        if (highestKey == null) return false;
        category = highestKey;
        amount = 10 + highest / 2;
        return true;
    }

    @Override
    public boolean activate(ServerPlayer player, IResearchTableData data) {
        if (category == null) return false;
        data.addCategoryTotal(category, amount);
        return true;
    }

    @Override
    public void write(CompoundTag output, HolderLookup.Provider registries) {
        super.write(output, registries);
        if (category != null) {
            TCNbt.store(output, "category", ResourceKey.codec(IResearchCategory.REGISTRY_KEY), registries, category);
        }
        output.putInt("amount", amount);
    }

    @Override
    public void read(CompoundTag input, HolderLookup.Provider registries) {
        super.read(input, registries);
        category = TCNbt.read(input, "category", ResourceKey.codec(IResearchCategory.REGISTRY_KEY), registries).orElse(null);
        amount = input.getInt("amount");
    }
}
