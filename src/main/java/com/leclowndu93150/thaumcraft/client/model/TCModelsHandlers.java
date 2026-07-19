package com.leclowndu93150.thaumcraft.client.model;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.model.obj.TCObjGeometry;
import com.leclowndu93150.thaumcraft.client.render.crystal.CrystalUnbakedModel;
import com.leclowndu93150.thaumcraft.content.wands.WandVisHelper;
import com.leclowndu93150.thaumcraft.registry.TCItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = TCIds.MODID, value = Dist.CLIENT)
public final class TCModelsHandlers {
    public static final ResourceLocation WAND_IS_STAFF_PROPERTY_ID = TCIds.rl("wand_is_staff");
    public static final ResourceLocation OBJ_LOADER_ID = TCIds.rl("obj");
    public static final ResourceLocation CRYSTAL_LOADER_ID = TCIds.rl("crystal");

    private TCModelsHandlers() {}

    @SubscribeEvent
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(OBJ_LOADER_ID, TCObjGeometry.Loader.INSTANCE);
        event.register(CRYSTAL_LOADER_ID, CrystalUnbakedModel.Loader.INSTANCE);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(TCItems.WAND.get(), WAND_IS_STAFF_PROPERTY_ID,
                (stack, level, entity, seed) -> WandVisHelper.getParts(stack).rod().staff() ? 1.0F : 0.0F));
    }
}
