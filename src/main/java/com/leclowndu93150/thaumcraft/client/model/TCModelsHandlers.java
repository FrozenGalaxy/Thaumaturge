package com.leclowndu93150.thaumcraft.client.model;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.model.obj.TCObjGeometry;
import com.leclowndu93150.thaumcraft.client.render.crystal.CrystalUnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = TCIds.MODID, value = Dist.CLIENT)
public final class TCModelsHandlers {
    public static final ResourceLocation JAR_MODEL_ID = TCIds.rl("jar");
    public static final ResourceLocation JAR_BRAIN_MODEL_ID = TCIds.rl("jar_brain");
    public static final ResourceLocation JAR_NODE_MODEL_ID = TCIds.rl("jar_node");
    public static final ResourceLocation CENTRIFUGE_MODEL_ID = TCIds.rl("centrifuge");
    public static final ResourceLocation GOLEM_BUILDER_MODEL_ID = TCIds.rl("golem_builder");
    public static final ResourceLocation DECON_TABLE_MODEL_ID = TCIds.rl("deconstruction_table");
    public static final ResourceLocation WAND_MODEL_ID = TCIds.rl("wand");
    public static final ResourceLocation NODE_STABILIZER_MODEL_ID = TCIds.rl("node_stabilizer");
    public static final ResourceLocation WAND_IS_STAFF_PROPERTY_ID = TCIds.rl("wand_is_staff");
    public static final ResourceLocation OBJ_LOADER_ID = TCIds.rl("obj");
    public static final ResourceLocation CRYSTAL_LOADER_ID = TCIds.rl("crystal");

    private TCModelsHandlers() {}

    @SubscribeEvent
    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(OBJ_LOADER_ID, TCObjGeometry.Loader.INSTANCE);
        event.register(CRYSTAL_LOADER_ID, CrystalUnbakedModel.Loader.INSTANCE);
    }
}
