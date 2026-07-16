package com.leclowndu93150.thaumcraft.client.screen.widget;

import com.leclowndu93150.thaumcraft.client.render.GuiBlend;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

final class WidgetRender {
    private WidgetRender() {}

    static void blitTinted(GuiGraphics graphics, ResourceLocation texture, int x, int y,
                           float u, float v, int width, int height, int textureWidth, int textureHeight, int argb) {
        GuiBlend.blitTinted(graphics, texture, x, y, u, v, width, height, textureWidth, textureHeight, argb);
    }
}
