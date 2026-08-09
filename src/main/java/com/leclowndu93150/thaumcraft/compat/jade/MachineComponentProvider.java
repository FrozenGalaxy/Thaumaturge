package com.leclowndu93150.thaumcraft.compat.jade;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.content.aura.node.BlockEntityNodeTransducer;
import com.leclowndu93150.thaumcraft.content.aura.relay.BlockEntityVisRelay;
import com.leclowndu93150.thaumcraft.content.essentia.smeltery.BlockEntitySmelter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum MachineComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = TCIds.rl("machine");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof BlockEntityVisRelay relay) {
            if (!relay.isLinked()) {
                tooltip.add(Component.translatable("jade.thaumcraft.relay.unlinked"));
            } else if (relay.depth() == 1) {
                tooltip.add(Component.translatable("jade.thaumcraft.relay.linked_node"));
            } else {
                tooltip.add(Component.translatable("jade.thaumcraft.relay.linked_relay", relay.depth() - 1));
            }
            return;
        }
        if (accessor.getBlockEntity() instanceof BlockEntityNodeTransducer transducer) {
            tooltip.add(Component.translatable("jade.thaumcraft.transducer.status." + transducer.getStatus()));
            if (transducer.getStatus() != 0) {
                tooltip.add(Component.translatable("jade.thaumcraft.transducer.charge",
                        transducer.getCount() * 100 / BlockEntityNodeTransducer.CHARGE_TARGET));
            }
            return;
        }
        if (!(accessor.getBlockEntity() instanceof BlockEntitySmelter)) {
            return;
        }
        CompoundTag data = accessor.getServerData();
        int progress = data.getInt("SmeltProgress");
        int burn = data.getInt("BurnRemaining");
        if (progress > 0) {
            tooltip.add(Component.translatable("jade.thaumcraft.machine.progress", progress));
        }
        if (burn > 0) {
            tooltip.add(Component.translatable("jade.thaumcraft.machine.heat", burn));
        }
    }
}
