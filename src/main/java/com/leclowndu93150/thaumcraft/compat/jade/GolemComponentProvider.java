package com.leclowndu93150.thaumcraft.compat.jade;

import com.leclowndu93150.thaumcraft.TCIds;
import com.leclowndu93150.thaumcraft.content.golem.EntityThaumcraftGolem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum GolemComponentProvider implements IEntityComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = TCIds.rl("golem");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        if (!(accessor.getEntity() instanceof EntityThaumcraftGolem golem)) {
            return;
        }
        tooltip.add(Component.translatable("jade.thaumcraft.golem.rank",
                golem.getProperties().getRank(), golem.getRankXp()));
    }
}
