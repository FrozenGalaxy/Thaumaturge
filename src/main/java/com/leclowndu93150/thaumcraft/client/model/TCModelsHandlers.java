package com.leclowndu93150.thaumcraft.client.model;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.client.model.obj.TCObjGeometry;
import com.leclowndu93150.thaumcraft.client.render.crystal.CrystalUnbakedModel;
import com.leclowndu93150.thaumcraft.content.item.CelestialBody;
import com.leclowndu93150.thaumcraft.content.wands.WandVisHelper;
import com.leclowndu93150.thaumcraft.registry.TCDataComponents;
import com.leclowndu93150.thaumcraft.registry.TCItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = TCIds.MODID, value = Dist.CLIENT)
public final class TCModelsHandlers {
    public static final ResourceLocation WAND_IS_STAFF_PROPERTY_ID = TCIds.rl("wand_is_staff");
    public static final ResourceLocation LINKED_PROPERTY_ID = TCIds.rl("linked");
    public static final ResourceLocation LOADED_PROPERTY_ID = TCIds.rl("loaded");
    public static final ResourceLocation NOTE_COMPLETE_PROPERTY_ID = TCIds.rl("note_complete");
    public static final ResourceLocation FILLED_PROPERTY_ID = TCIds.rl("filled");
    public static final ResourceLocation VERDANT_TYPE_PROPERTY_ID = TCIds.rl("verdant_type");
    public static final ResourceLocation CELESTIAL_BODY_PROPERTY_ID = TCIds.rl("celestial_body");
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
        event.enqueueWork(() -> {
            ItemProperties.register(TCItems.WAND.get(), WAND_IS_STAFF_PROPERTY_ID,
                    (stack, level, entity, seed) -> WandVisHelper.getParts(stack).rod().staff() ? 1.0F : 0.0F);
            registerComponentFlag(TCItems.MIRROR.get(), LINKED_PROPERTY_ID, TCDataComponents.MIRROR_LINK.get());
            registerComponentFlag(TCItems.MIRROR_ESSENTIA.get(), LINKED_PROPERTY_ID, TCDataComponents.MIRROR_LINK.get());
            registerComponentFlag(TCItems.GRAPPLE_GUN.get(), LOADED_PROPERTY_ID, TCDataComponents.GRAPPLE_LOADED.get());
            registerComponentFlag(TCItems.RESEARCH_NOTE.get(), NOTE_COMPLETE_PROPERTY_ID, TCDataComponents.NOTE_COMPLETE.get());
            registerComponentFlag(TCItems.PHIAL.get(), FILLED_PROPERTY_ID, TCDataComponents.ASPECTS.get());
            ItemProperties.register(TCItems.VERDANT_CHARM.get(), VERDANT_TYPE_PROPERTY_ID,
                    (stack, level, entity, seed) -> stack.getOrDefault(TCDataComponents.VERDANT_TYPE.get(), 0));
            ItemProperties.register(TCItems.CELESTIAL_NOTES.get(), CELESTIAL_BODY_PROPERTY_ID,
                    (stack, level, entity, seed) -> {
                        CelestialBody body = stack.get(TCDataComponents.CELESTIAL_BODY.get());
                        return body == null ? 0.0F : body.ordinal();
                    });
        });
    }

    private static void registerComponentFlag(Item item, ResourceLocation id, DataComponentType<?> component) {
        ItemProperties.register(item, id, (stack, level, entity, seed) -> stack.has(component) ? 1.0F : 0.0F);
    }
}
