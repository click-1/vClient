package com.vClient.ui;

import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import com.vClient.util.custom_font.CustomFontUtil;
import com.vClient.util.custom_font.MinecraftFontRenderer;
import com.vClient.vClient;
import com.vClient.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.Comparator;

public class ArrayListHUD {
    Minecraft mc = Minecraft.getMinecraft();
    MinecraftFontRenderer cfr = CustomFontUtil.arial;
    MinecraftFontRenderer big = CustomFontUtil.big;
    public void draw() {
        ScaledResolution sr = new ScaledResolution(mc);

        vClient.instance.moduleManager.getModules().sort(Comparator.comparingDouble(m ->
                cfr.getStringWidth(((Module)m).getName())).reversed());

        int count = 0;
        boolean tails = vClient.instance.settingsManager.getSettingByName("tails").getValBoolean();
        boolean chrome =vClient.instance.settingsManager.getSettingByName("chroma").getValBoolean();
        boolean boxes = vClient.instance.settingsManager.getSettingByName("boxes").getValBoolean();
        int color = ColorUtil.getClickGUIColor().getRGB();
        int chroma = ColorUtil.getBlueandPinkRainbow(4f, 0);

        for (Module m : vClient.instance.moduleManager.getModules()) {
            if (!m.isToggled())
                continue;
            float offset = count * (cfr.getHeight() + 6);
            if (boxes)
                Gui.drawRect(sr.getScaledWidth() - cfr.getStringWidth(m.getName()) - 7, offset, sr.getScaledWidth(), offset + CustomFontUtil.arial.getHeight() + 6, 0x90000000);
            if (tails)
                Gui.drawRect(sr.getScaledWidth() - cfr.getStringWidth(m.getName()) - 9, offset, sr.getScaledWidth() - cfr.getStringWidth(m.getName()) - 7, 6 + cfr.getHeight() + offset, chrome ? chroma: color);

            cfr.drawString(m.getName(), sr.getScaledWidth() - CustomFontUtil.arial.getStringWidth(m.getName()) - 4, 3 + offset, ColorUtil.getBlueandPinkRainbow(2f, -(int)offset*12));
            cfr.drawString("", sr.getScaledWidth(), sr.getScaledHeight(), -1);
            count++;
        }

        if (vClient.instance.settingsManager.getSettingByName("BPS").getValBoolean())
            cfr.drawString("blocks/s: " + round(MovementUtil.getSpeed() * 20.0f, 2), sr.getScaledWidth() *.01f,sr.getScaledHeight() *.95f, chrome ? chroma : color);

        big.drawSmoothString(EnumChatFormatting.BOLD + "vCLIENT", sr.getScaledWidth() *.01f, sr.getScaledHeight() *.04f, new Color(158, 255, 255, 255).getRGB());
    }

    private double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        double ans = (double) Math.round(value * scale) / scale;
        return ans > 0.0 ? ans : 0.1;
    }
}
