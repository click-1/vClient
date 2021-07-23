package com.vClient.module.render;

import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.module.combat.Killaura;
import com.vClient.vClient;
import de.Hero.clickgui.util.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;
import java.awt.*;

public class TargetHUD extends Module {
    private int x1, y1, x2, y2;

    public TargetHUD() {
        super("TargetHUD", Keyboard.CHAR_NONE, Category.RENDER, "Display current target info on screen.");
    }

    public void draw() {
        if (!this.isToggled())
            return;
        /**
        if (!vClient.instance.moduleManager.getModulebyName("Killaura").isToggled())
            return;
        Killaura KAinstance = (Killaura) vClient.instance.moduleManager.getModulebyName("Killaura");
         */
        EntityLivingBase target = (EntityLivingBase) mc.pointedEntity;

        if (target != null && target.isEntityAlive())
            display(target);
    }

    private void display(EntityLivingBase target) {
        ScaledResolution sr = new ScaledResolution(mc);
        x1 = sr.getScaledWidth() / 2;
        y1 = sr.getScaledHeight() / 4 + 35;
        x2 = x1 + 100;
        y2 = y1 + 40;
        Gui.drawRect(x1, y1, x2, y2, new Color(0 ,0, 0, 170).getRGB());
        Gui.drawRect(x1 + 30, y1 + 9 + 18, x2 - 5, y2 - 4, new Color(145, 145, 145, 170).getRGB());
        Gui.drawRect(x1 + 30, y1 + 9 + 18, x1 + 30 + barlength(target), y2 - 4, new Color(0, 255, 0, 255).getRGB());

        FontUtil.drawStringWithShadow(target.getName(), x1 + 30, y1 + 5, -1);
        FontUtil.drawStringWithShadow(round(target.getHealth(), 1) + " \u2764", x1 + 30, y1 + 7 + FontUtil.getFontHeight(), -1);

        GuiInventory.drawEntityOnScreen(x1 + 13, y1 + 37, 16, -90, -30, target);
    }

    private double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        double ans = (double) Math.round(value * scale) / scale;
        return ans > 0.0 ? ans : 0.1;
    }
    private double barlength(EntityLivingBase target) {
        double diff = x2 - 35 - x1;
        double ratio = target.getHealth() / target.getMaxHealth();
        return ratio*diff;
    }
}
