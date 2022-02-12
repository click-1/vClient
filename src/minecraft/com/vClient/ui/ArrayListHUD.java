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
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class ArrayListHUD {
    MinecraftFontRenderer cfr = CustomFontUtil.arial;
    MinecraftFontRenderer big = CustomFontUtil.big;

    public void draw() {
        int width = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
        int height = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
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
                Gui.drawRect(width - startPos - 7, offset, width, offset + CustomFontUtil.arial.getHeight() + 6, new Color(66, 66, 66, 102).getRGB());
            if (tails)
                Gui.drawRect(width - startPos - 9, offset, width - startPos - 7, 6 + cfr.getHeight() + offset, chrome ? chroma: color);

            if (m.getDisplayMode() != null) {
                cfr.drawString(m.getName(), width - startPos - 4, 3 + offset, ColorUtil.getBlueandPinkRainbow(2f, -(int)offset*12));
                cfr.drawString(m.getDisplayMode(), width - startPos - 4 + cfr.getStringWidth(m.getName() + " "), 3 + offset, 0x00AAAAAA);
            } else {
                cfr.drawString(m.getName(), width - startPos - 4, 3 + offset, ColorUtil.getBlueandPinkRainbow(2f, -(int)offset*12));
            }
            count++;
        }

        if (vClient.instance.settingsManager.getSettingByName("BPS").getValBoolean())
            cfr.drawString("blocks/s: " + MathUtil.round(MovementUtil.getSpeed() * 20.0f, 2), width *.01f, height *.95f, chrome ? chroma : color);

        big.drawSmoothString(EnumChatFormatting.BOLD + "vCLIENT", width *.01f, height *.04f, new Color(135, 255, 255, 255).getRGB());
    }

    private ArrayList<Module> getSortedList() {
        ArrayList<Module> res = vClient.instance.moduleManager.getModules();
        res.sort(Comparator.comparingDouble(m -> cfr.getStringWidth(((Module) m).getFullDisplayName())).reversed());
        return res;
    }
}
