package com.leclowndu93150.thaumcraft.compat.jade;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.api.aspect.AspectComponents;
import com.leclowndu93150.thaumcraft.api.aspect.AspectInstance;
import com.leclowndu93150.thaumcraft.api.aspect.AspectList;
import com.leclowndu93150.thaumcraft.api.aspect.IAspectContainer;
import com.leclowndu93150.thaumcraft.content.aura.node.BlockEntityNode;
import com.leclowndu93150.thaumcraft.content.essentia.jar.BlockEntityJar;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EssentiaComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = TCIds.rl("essentia");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof BlockEntityJar jar) {
            AspectList contents = jar.getContents(accessor.getLevel().registryAccess());
            if (contents.isEmpty()) {
                tooltip.add(Component.translatable("jade.thaumcraft.essentia.empty"));
                return;
            }
            for (AspectInstance entry : contents.entries()) {
                tooltip.add(Component.translatable("jade.thaumcraft.essentia.fill",
                        AspectComponents.name(entry.aspect()), entry.amount(), jar.capacity()));
            }
            return;
        }
        if (accessor.getBlockEntity() instanceof BlockEntityNode) {
            return;
        }
        if (accessor.getBlockEntity() instanceof IAspectContainer container) {
            AspectList aspects = container.getAspects();
            if (!aspects.isEmpty()) {
                tooltip.add(JadeComponents.aspectLine("jade.thaumcraft.essentia.contents", aspects));
            }
        }
    }
}
