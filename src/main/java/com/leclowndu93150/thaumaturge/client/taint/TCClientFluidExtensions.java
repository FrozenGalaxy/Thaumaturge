package com.leclowndu93150.thaumaturge.client.taint;

import com.leclowndu93150.thaumaturge.TCIds;
import com.leclowndu93150.thaumaturge.registry.TCFluidTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = TCIds.MODID, value = Dist.CLIENT)
public final class TCClientFluidExtensions {
    private TCClientFluidExtensions() {}

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(new FluxGooClientExtensions(), TCFluidTypes.FLUX_GOO.get());
        event.registerFluidType(new PurifyingClientExtensions(), TCFluidTypes.PURIFYING.get());
        event.registerFluidType(new LiquidDeathClientExtensions(), TCFluidTypes.LIQUID_DEATH.get());
    }
}
