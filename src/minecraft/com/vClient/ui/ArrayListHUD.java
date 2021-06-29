package com.vClient.ui;

import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.clickgui.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Comparator;

public class ArrayListHUD {
    public Minecraft mc = Minecraft.getMinecraft();
    private int chroma = ColorUtil.getRainbow(5, 0.8f, 1);
    
    public void draw() {
        ScaledResolution sr = new ScaledResolution(mc);
        FontRenderer fr = mc.fontRendererObj;
        vClient.instance.moduleManager.getModules().sort(Comparator.comparingInt(m ->
                fr.getStringWidth(((Module) m).getName())).reversed());

        int count = 0;
        boolean tails = vClient.instance.settingsManager.getSettingByName("tails").getValBoolean();
        boolean chrome =vClient.instance.settingsManager.getSettingByName("chroma").getValBoolean();
        boolean boxes = vClient.instance.settingsManager.getSettingByName("boxes").getValBoolean();
        /**
         * match tail color with ClickGUI RGB. default orange
         * int r = (int) vClient.instance.settingsManager.getSettingByName("guired").getValDouble();
        int g = (int) vClient.instance.settingsManager.getSettingByName("guigreen").getValDouble();
        int b = (int) vClient.instance.settingsManager.getSettingByName("guiblue").getValDouble();
        String hex = String.format("#%02x%02x%02x", r, g, b);
        **/
        int color = ColorUtil.getClickGUIColor().getRGB();
        for (Module m : vClient.instance.moduleManager.getModules()) {
            if (!m.isToggled())
                continue;
            double offset = count * (fr.FONT_HEIGHT + 6);
            if (boxes)
                Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 8, offset, sr.getScaledWidth(), 6 + fr.FONT_HEIGHT + offset, 0x90000000);
            if (tails)
                if (chrome)
                    Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 10, offset, sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 8, 6 + fr.FONT_HEIGHT + offset, chroma);
                else
                    Gui.drawRect(sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 10, offset, sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 8, 6 + fr.FONT_HEIGHT + offset, color);
            fr.drawString(m.getName(), sr.getScaledWidth() - fr.getStringWidth(m.getName()) - 4, 4 + offset, -1);
            count++;
        }
    }
}
