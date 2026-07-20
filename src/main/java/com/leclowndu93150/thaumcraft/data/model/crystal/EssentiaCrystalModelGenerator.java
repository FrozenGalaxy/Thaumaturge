package com.leclowndu93150.thaumcraft.data.model.crystal;

import com.google.gson.JsonElement;
import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.registry.TCItems;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;

public final class EssentiaCrystalModelGenerator {
    private static final ResourceLocation CRYSTAL_TEXTURE = TCIds.rl("item/essentia_crystal");

    private EssentiaCrystalModelGenerator() {}

    public static void register(BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(TCItems.ESSENTIA_CRYSTAL.get()),
                TextureMapping.layer0(CRYSTAL_TEXTURE), modelOutput);
    }
}
