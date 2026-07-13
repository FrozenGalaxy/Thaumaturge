package com.leclowndu93150.thaumcraft.client.model;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.model.obj.TCObjUnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

@EventBusSubscriber(modid = TCIds.MODID, value = Dist.CLIENT)
public class TCModelsHandlers {

    public static final ResourceLocation JAR_MODEL_ID =
            ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "jar");

    public static final ResourceLocation JAR_BRAIN_MODEL_ID =
            ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "jar_brain");

    public static final ResourceLocation CENTRIFUGE_MODEL_ID =
            ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "centrifuge");

    public static final ResourceLocation GOLEM_BUILDER_MODEL_ID =
            ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "golem_builder");

    public static final ResourceLocation OBJ_LOADER_ID =
            ResourceLocation.fromNamespaceAndPath(TCIds.MODID, "obj");

    @SubscribeEvent
    public static void onRegisterItemModels(RegisterSpecialModelRendererEvent event){
        event.register(JAR_MODEL_ID, JarItemSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(JAR_BRAIN_MODEL_ID, JarBrainItemSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(CENTRIFUGE_MODEL_ID, CentrifugeItemSpecialRenderer.Unbaked.MAP_CODEC);
        event.register(GOLEM_BUILDER_MODEL_ID, GolemBuilderItemSpecialRenderer.Unbaked.MAP_CODEC);
    }

    @SubscribeEvent
    public static void onRegisterLoaders(ModelEvent.RegisterLoaders event) {
        event.register(OBJ_LOADER_ID, TCObjUnbakedModel.Loader.INSTANCE);
    }

}
