package com.leclowndu93150.thaumcraft.content.device;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.api.essentia.EssentiaCapabilities;
import com.leclowndu93150.thaumcraft.registry.TCBlockEntities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

@EventBusSubscriber(modid = TCIds.MODID)
public final class DeviceCapabilities {
    private DeviceCapabilities() {}

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                EssentiaCapabilities.TRANSPORT,
                TCBlockEntities.LAMP_GROWTH.get(),
                (be, side) -> be
        );
        event.registerBlockEntity(
                EssentiaCapabilities.TRANSPORT,
                TCBlockEntities.LAMP_FERTILITY.get(),
                (be, side) -> be
        );
        event.registerBlockEntity(
                EssentiaCapabilities.TRANSPORT,
                TCBlockEntities.CENTRIFUGE.get(),
                (be, side) -> be
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TCBlockEntities.HUNGRY_CHEST.get(),
                (be, side) -> new InvWrapper(be)
        );
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                TCBlockEntities.EVERFULL_URN.get(),
                (be, side) -> be.getTank()
        );
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                TCBlockEntities.VIS_GENERATOR.get(),
                (be, side) -> side == be.outputFace() ? be : null
        );
        event.registerBlockEntity(
                EssentiaCapabilities.TRANSPORT,
                TCBlockEntities.ESSENTIA_PORT.get(),
                (be, side) -> be
        );
        event.registerBlockEntity(
                EssentiaCapabilities.TRANSPORT,
                TCBlockEntities.CONDENSER.get(),
                (be, side) -> be
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TCBlockEntities.VOID_SIPHON.get(),
                (be, side) -> be.output()
        );
        event.registerBlockEntity(
                EssentiaCapabilities.TRANSPORT,
                TCBlockEntities.THAUMATORIUM.get(),
                (be, side) -> be
        );
        event.registerBlockEntity(
                EssentiaCapabilities.TRANSPORT,
                TCBlockEntities.THAUMATORIUM_TOP.get(),
                (be, side) -> be
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                TCBlockEntities.THAUMATORIUM.get(),
                (be, side) -> be.catalyst()
        );
    }
}
