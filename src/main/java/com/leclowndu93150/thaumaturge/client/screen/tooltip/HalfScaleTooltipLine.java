package com.leclowndu93150.thaumaturge.client.screen.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;

public final class HalfScaleTooltipLine implements ClientTooltipComponent {
    public static final String PREFIX = "@@";
    private static final float SCALE = 0.5F;
    private static final int LINE_HEIGHT = 7;
    private static final int FULL_BRIGHT = 0xF000F0;
    private static final int WHITE = -1;

    private final FormattedCharSequence text;

    public HalfScaleTooltipLine(FormattedCharSequence text) {
        this.text = text;
    }

    @Override
    public int getWidth(Font font) {
        return Math.round(font.width(text) * SCALE);
    }

    @Override
    public int getHeight() {
        return LINE_HEIGHT;
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        Matrix4f scaled = new Matrix4f(matrix).translate(x, y, 0.0F).scale(SCALE);
        font.drawInBatch(text, 0.0F, 0.0F, WHITE, true, scaled, bufferSource,
                Font.DisplayMode.NORMAL, 0, FULL_BRIGHT);
    }
}
