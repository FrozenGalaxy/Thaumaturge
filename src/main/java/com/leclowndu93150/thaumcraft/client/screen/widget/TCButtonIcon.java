package com.leclowndu93150.thaumcraft.client.screen.widget;

import com.leclowndu93150.thaumcraft.api.aspect.IAspect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public sealed interface TCButtonIcon {
    void draw(GuiGraphics graphics, int x, int y, int size, int tintColor);

    record AspectIcon(Holder<IAspect> aspect) implements TCButtonIcon {
        @Override
        public void draw(GuiGraphics graphics, int x, int y, int size, int tintColor) {
            IAspect value = aspect.value();
            int color = (tintColor & 0xFF000000) | (value.color() & 0x00FFFFFF);
            WidgetRender.blitTinted(graphics, value.texture(), x, y, 0.0F, 0.0F, size, size, size, size, color);
        }
    }

    record TextureIcon(ResourceLocation texture, int textureWidth, int textureHeight) implements TCButtonIcon {
        public TextureIcon(ResourceLocation texture) {
            this(texture, 16, 16);
        }

        @Override
        public void draw(GuiGraphics graphics, int x, int y, int size, int tintColor) {
            WidgetRender.blitTinted(graphics, texture, x, y, 0.0F, 0.0F, size, size, textureWidth, textureHeight, tintColor);
        }
    }

    record StackIcon(ItemStack stack) implements TCButtonIcon {
        @Override
        public void draw(GuiGraphics graphics, int x, int y, int size, int tintColor) {
            graphics.renderItem(stack, x, y);
            graphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);
        }
    }
}
