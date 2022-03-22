package com.vClient.ui;

import com.vClient.module.Module;
import com.vClient.util.MathUtil;
import com.vClient.util.MovementUtil;
import com.vClient.util.custom_font.CustomFontUtil;
import com.vClient.util.custom_font.MinecraftFontRenderer;
import com.vClient.vClient;
import com.vClient.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ArrayListHUD {
    MinecraftFontRenderer cfr = CustomFontUtil.arial;
    MinecraftFontRenderer big = CustomFontUtil.big;
    private Minecraft mc = Minecraft.getMinecraft();

    public void draw() {
        int width = new ScaledResolution(mc).getScaledWidth();
        int height = new ScaledResolution(mc).getScaledHeight();
        int count = 0;
        boolean tails = vClient.instance.settingsManager.getSettingByName("tails").getValBoolean();
        boolean chrome =vClient.instance.settingsManager.getSettingByName("chroma").getValBoolean();
        boolean boxes = vClient.instance.settingsManager.getSettingByName("boxes").getValBoolean();
        int color = ColorUtil.getClickGUIColor().getRGB();
        int chroma = ColorUtil.getBlueandPinkRainbow(4f, 0);

        for (Module m : getSortedList()) {
            if (!m.isToggled())
                continue;
            float offset = count * (cfr.getHeight() + 6);
            double startPos = m.getDisplayMode() != null ? cfr.getStringWidth(m.getName() + " " + m.getDisplayMode()) : cfr.getStringWidth(m.getName());
            if (boxes)
                Gui.drawRect(width - startPos - 7, offset, width, offset + cfr.getHeight() + 6, new Color(184, 184, 184, 52).getRGB());

            drawHorizontalGradient((int)(width - startPos - 35), (int)offset, (int)(width - startPos - 7), (int)(offset + cfr.getHeight() + 6), new Color(255, 255, 255, 3).getRGB(), new Color(21, 21, 21, 91).getRGB());
            if (tails)
                Gui.drawRect(width - startPos - 9, offset, width - startPos - 7, offset + cfr.getHeight() + 6, chrome ? chroma: color);

            if (m.getDisplayMode() != null) {
                cfr.drawString(EnumChatFormatting.BOLD  + m.getName(), width - startPos - 4, 3 + offset, ColorUtil.getBlueandPinkRainbow(2f, -(int)offset*12));
                cfr.drawString(m.getDisplayMode(), width - startPos - 4 + cfr.getStringWidth(m.getName() + " "), 3 + offset, 0x00AAAAAA);
            } else {
                cfr.drawString(EnumChatFormatting.BOLD  + m.getName(), width - startPos - 4, 3 + offset, ColorUtil.getBlueandPinkRainbow(2f, -(int)offset*12));
            }
            count++;
        }

        if (vClient.instance.settingsManager.getSettingByName("BPS").getValBoolean())
            cfr.drawString("blocks/s: " + MathUtil.round(MovementUtil.getSpeed() * 20.0f, 2), width *.01f, height *.95f, chrome ? chroma : color);

        big.drawSmoothString(EnumChatFormatting.BOLD + "vCLIENT", width *.01f, height *.04f, ColorUtil.getaqua());
    }

    private ArrayList<Module> getSortedList() {
        ArrayList<Module> res = vClient.instance.moduleManager.getModules();
        res.sort(Comparator.comparingDouble(m -> cfr.getStringWidth(((Module) m).getFullDisplayName())).reversed());
        return res;
    }

    private void drawHorizontalGradient(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double)left, (double)top, (double)mc.ingameGUI.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)bottom, (double)mc.ingameGUI.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)right, (double)bottom, (double)mc.ingameGUI.zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double)right, (double)top, (double)mc.ingameGUI.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
