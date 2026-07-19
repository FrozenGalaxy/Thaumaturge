package com.leclowndu93150.thaumcraft.client.screen.tooltip;

import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class DeferredTooltip {
    private static @Nullable List<Component> lines;
    private static ItemStack stack = ItemStack.EMPTY;
    private static int x;
    private static int y;

    private DeferredTooltip() {}

    public static void set(List<Component> tooltipLines, int mouseX, int mouseY) {
        lines = tooltipLines;
        stack = ItemStack.EMPTY;
        x = mouseX;
        y = mouseY;
    }

    public static void set(Component line, int mouseX, int mouseY) {
        set(List.of(line), mouseX, mouseY);
    }

    public static void setItem(ItemStack itemStack, int mouseX, int mouseY) {
        stack = itemStack;
        lines = null;
        x = mouseX;
        y = mouseY;
    }

    public static void render(GuiGraphics graphics, Font font) {
        if (!stack.isEmpty()) {
            graphics.renderTooltip(font, stack, x, y);
        } else if (lines != null && !lines.isEmpty()) {
            graphics.renderComponentTooltip(font, lines, x, y);
        }
        lines = null;
        stack = ItemStack.EMPTY;
    }
}
