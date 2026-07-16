package com.leclowndu93150.thaumcraft.client.color;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.registry.TCItems;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = TCIds.MODID, value = Dist.CLIENT)
public final class TCColorHandlers {
    private TCColorHandlers() {}

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(new CrystalAspectTint(), TCItems.ESSENTIA_CRYSTAL.get());
        event.register(new AspectColorTint(), TCItems.PHIAL.get());
        event.register(new NoteColorTint(), TCItems.RESEARCH_NOTE.get());
        event.register(new GolemMaterialTint(), TCItems.GOLEM_PLACER.get());
        FocusColorTint focusColor = new FocusColorTint();
        event.register(focusColor, TCItems.FOCUS_1.get());
        event.register(focusColor, TCItems.FOCUS_2.get());
        event.register(focusColor, TCItems.FOCUS_3.get());
        for (DyeColor dye : DyeColor.values()) {
            event.register(new AspectFilterTint(dye.getMapColor().col), TCItems.BANNERS.get(dye).get());
        }
    }
}
