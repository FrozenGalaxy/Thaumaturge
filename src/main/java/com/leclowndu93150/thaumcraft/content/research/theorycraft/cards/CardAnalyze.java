package com.leclowndu93150.thaumcraft.content.research.theorycraft.cards;

import com.leclowndu93150.thaumcraft.serialization.TCNbt;
import com.leclowndu93150.thaumcraft.api.capability.IPlayerKnowledge;
import com.leclowndu93150.thaumcraft.api.capability.KnowledgeAccess;
import com.leclowndu93150.thaumcraft.api.capability.KnowledgeType;
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

public final class CardAnalyze extends TheorycraftCard {
    private @Nullable ResourceKey<IResearchCategory> category;

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
        return Component.translatable(
                "card.thaumcraft.analyze.name",
                category == null ? Component.empty() : CategoryComponents.emphasised(category));
    }

    @Override
    public Component description() {
        return Component.translatable(
                "card.thaumcraft.analyze.text",
                category == null ? Component.empty() : CategoryComponents.emphasised(category),
                CategoryComponents.emphasised(TCResearchCategories.BASICS));
    }

    @Override
    public boolean initialize(ServerPlayer player, IResearchTableData data) {
        Random rng = new Random(seed());
        List<ResourceKey<IResearchCategory>> all = data.availableCategories(player);
        IPlayerKnowledge knowledge = KnowledgeAccess.of(player);
        List<ResourceKey<IResearchCategory>> candidates = new ArrayList<>();
        for (ResourceKey<IResearchCategory> key : all) {
            if (key == TCResearchCategories.BASICS) continue;
            if (knowledge.knowledge(KnowledgeType.OBSERVATION, key) > 0) {
                candidates.add(key);
            }
        }
        if (candidates.isEmpty()) return false;
        category = candidates.get(rng.nextInt(candidates.size()));
        return true;
    }

    @Override
    public boolean activate(ServerPlayer player, IResearchTableData data) {
        if (category == null) return false;
        IPlayerKnowledge knowledge = KnowledgeAccess.of(player);
        if (knowledge.knowledge(KnowledgeType.OBSERVATION, category) < 1) return false;
        knowledge.addKnowledge(KnowledgeType.OBSERVATION, category, -KnowledgeType.OBSERVATION.progression());
        knowledge.sync(player);
        data.addCategoryTotal(TCResearchCategories.BASICS, 5);
        data.addCategoryTotal(category, 25 + player.getRandom().nextInt(26));
        return true;
    }

    @Override
    public void write(CompoundTag output, HolderLookup.Provider registries) {
        super.write(output, registries);
        if (category != null) {
            TCNbt.store(output, "category", ResourceKey.codec(IResearchCategory.REGISTRY_KEY), registries, category);
        }
    }

    @Override
    public void read(CompoundTag input, HolderLookup.Provider registries) {
        super.read(input, registries);
        category = TCNbt.read(input, "category", ResourceKey.codec(IResearchCategory.REGISTRY_KEY), registries).orElse(null);
    }
}
