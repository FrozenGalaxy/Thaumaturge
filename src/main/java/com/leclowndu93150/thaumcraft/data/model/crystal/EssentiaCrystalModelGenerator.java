package com.leclowndu93150.thaumcraft.data.model.crystal;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.color.CrystalAspectTint;
import com.leclowndu93150.thaumcraft.registry.TCItems;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public final class EssentiaCrystalModelGenerator {
    private static final ResourceLocation CRYSTAL_TEXTURE = ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "item/essentia_crystal");

    private EssentiaCrystalModelGenerator() {}

    public static void register(ItemModelGenerators itemModels) {
        ResourceLocation model = ModelLocationUtils.getModelLocation(TCItems.ESSENTIA_CRYSTAL.get());
        ModelTemplates.FLAT_ITEM.create(model, TextureMapping.layer0(new Material(CRYSTAL_TEXTURE)), itemModels.modelOutput);
        itemModels.itemModelOutput.accept(TCItems.ESSENTIA_CRYSTAL.get(),
                ItemModelUtils.tintedModel(model, new CrystalAspectTint(0xFFFFFF)));
    }
}
