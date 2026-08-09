package com.leclowndu93150.thaumcraft.compat.jade;

import com.leclowndu93150.thaumcraft.api.aspect.AspectComponents;
import com.leclowndu93150.thaumcraft.api.aspect.AspectInstance;
import com.leclowndu93150.thaumcraft.api.aspect.AspectList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

final class JadeComponents {
    private JadeComponents() {}

    static Component aspectLine(String key, AspectList aspects) {
        MutableComponent list = Component.empty();
        boolean first = true;
        for (AspectInstance entry : aspects.entries()) {
            if (!first) {
                list.append(Component.translatable("jade.thaumcraft.aspect_separator"));
            }
            list.append(Component.translatable("jade.thaumcraft.aspect_amount",
                    AspectComponents.name(entry.aspect()), entry.amount()));
            first = false;
        }
        return Component.translatable(key, list);
    }
}
