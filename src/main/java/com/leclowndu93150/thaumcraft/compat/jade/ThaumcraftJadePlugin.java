package com.leclowndu93150.thaumcraft.compat.jade;

import com.leclowndu93150.thaumcraft.content.golem.EntityThaumcraftGolem;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public final class ThaumcraftJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(MachineDataProvider.INSTANCE, Block.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(NodeComponentProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(EssentiaComponentProvider.INSTANCE, Block.class);
        registration.registerBlockComponent(MachineComponentProvider.INSTANCE, Block.class);
        registration.registerEntityComponent(GolemComponentProvider.INSTANCE, EntityThaumcraftGolem.class);
    }
}
