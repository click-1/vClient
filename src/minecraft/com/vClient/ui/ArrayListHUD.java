package com.vClient.ui;

import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.clickgui.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import java.util.Comparator;

public class ArrayListHUD {
    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fr = mc.fontRendererObj;
    public void draw() {
        ScaledResolution sr = new ScaledResolution(mc);

        vClient.instance.moduleManager.getModules().sort(Comparator.comparingDouble(m ->
                fr.getPreciseStringWidth(((Module)m).getName())).reversed());

        int count = 0;
        boolean tails = vClient.instance.settingsManager.getSettingByName("tails").getValBoolean();
        boolean chrome =vClient.instance.settingsManager.getSettingByName("chroma").getValBoolean();
        boolean boxes = vClient.instance.settingsManager.getSettingByName("boxes").getValBoolean();
        int color = ColorUtil.getClickGUIColor().getRGB();
        int chroma = ColorUtil.getRainbow(5, 0.8f, 1);

        fr.setUnicodeFlag(true);
        for (Module m : vClient.instance.moduleManager.getModules()) {
            if (!m.isToggled())
                continue;
            double offset = count * (fr.FONT_HEIGHT + 6);
            if (boxes)
                Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 12, offset, sr.getScaledWidth(), 6 + fr.FONT_HEIGHT + offset, 0x90000000);
            if (tails)
                if (chrome)
                    Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 14, offset, sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 12, 6 + fr.FONT_HEIGHT + offset, chroma);
                else
                    Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 14, offset, sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 12, 6 + fr.FONT_HEIGHT + offset, color);
            //fr.drawString(m.getName(), sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 4, 4 + offset, -1);
            fr.drawString(EnumChatFormatting.getValueByName("BOLD") + m.getName(), sr.getScaledWidth() - 0.9*fr.getStringWidth(EnumChatFormatting.getValueByName("BOLD") + m.getName()) - 5, 4 + offset, -1);
            count++;
        }
        fr.setUnicodeFlag(false);
    }
}
