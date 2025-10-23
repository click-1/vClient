package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.*;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.ColorUtil;
import com.vClient.util.MathUtil;
import com.vClient.util.RenderUtil;
import com.vClient.util.TargetUtil;
import com.vClient.util.custom_font.CustomFontUtil;
import com.vClient.util.custom_font.MinecraftFontRenderer;
import com.vClient.vClient;
import de.Hero.clickgui.util.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import java.awt.*;

public class TargetHUD extends Module {
    private int x1, y1, x2, y2;
    private MinecraftFontRenderer fr1 = CustomFontUtil.hud;
    private MinecraftFontRenderer fr2 = CustomFontUtil.arial;
    private EntityLivingBase regularTarget;
    private int regularTargetXP;
    private int ticksleft = 50;            // how long until hud fades

    public TargetHUD() {
        super("TargetHUD", Keyboard.CHAR_NONE, Category.COMBAT, "Display current target info on screen.");
    }

    @EventTarget
    public void onAttack(EventAttack event) {
        if (event.getTargetEntity() instanceof EntityLivingBase) {
            regularTarget = (EntityLivingBase) event.getTargetEntity();
            ticksleft = 50;
        }
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (ticksleft > 0)
            ticksleft--;
    }

    @EventTarget
    public void on2D(Event2D event) {
        EntityLivingBase KAtarget = ((KillAura) vClient.instance.moduleManager.getModulebyName("KillAura")).active_target;
        if (KAtarget != null) {
            display(KAtarget);
            return;
        }
        if (regularTarget != null ) {
            if (regularTarget.isEntityAlive() && ticksleft > 0) {
                display(regularTarget);
            } else if (!regularTarget.isEntityAlive()) {
                regularTarget = null;
                regularTargetXP = 0;
            }
        }
    }

    private void display(EntityLivingBase target) {
        GlStateManager.translate(55,55,0);
        GlStateManager.scale(0.8,0.8,1);

        ScaledResolution sr = new ScaledResolution(mc);
        x1 = sr.getScaledWidth() / 2;
        y1 = sr.getScaledHeight() / 4 + 35;
        x2 = x1 + 110;
        y2 = y1 + 40;
        Gui.drawRect(x1, y1, x2, y2, new Color(0, 0, 0, 170).getRGB());
        RenderUtil.drawHorizontalGradient(x1,y1-2,x2,y1, new Color(255,122,122).getRGB(), new Color(122,255,255).getRGB());
        RenderUtil.drawVerticalGradient(x1-2,y1-2,x1,y2+5,new Color(255,122,122).getRGB(), new Color(122,255,255).getRGB());
        RenderUtil.drawVerticalGradient(x2,y1-2,x2+2,y2+5, new Color(122,255,255).getRGB(),new Color(255,122,122).getRGB());
        Gui.drawRect(x1, y2, (int)(x1+barlength(target,x1,x2)), y2+5, getHPcolor(target));      // HP bar
        Gui.drawRect((int)(x1+barlength(target,x1,x2)), y2, x2, y2+5, new Color(0 ,0, 0, 170).getRGB());        // remaining gray bar

        fr1.drawString(target.getDisplayName().getFormattedText(), x1 + 30, y1 + 4, -1);  // name
        mc.fontRendererObj.drawString(regularTarget instanceof EntityPlayer ? "" + regularTargetXP : "none", x1 + 85, y1 + 19, 8453920);  // exp level
        GlStateManager.scale(1.6, 1.6, 1);
        FontUtil.drawString(MathUtil.round(target.getHealth(), 1) + "\u2764", (x1+30)/1.6, (y1+fr1.getHeight())/1.6 + 5, getHPcolor(target));        // HP + heart symbol
        GlStateManager.scale(.625,.625,1);
        fr2.drawString("blocking: " + (target instanceof EntityPlayer ? ((EntityPlayer) target).isBlocking() ? "true" : "false" : ""), x1 + 30, y1 + 22 + FontUtil.getFontHeight(), ColorUtil.baseColorInt);

        GuiInventory.drawEntityOnScreen(x1 + 14, y2 - 4, 16, -90, -30, target);
        
        GlStateManager.scale(1.25,1.25,1);
        GlStateManager.translate(-55,-55,0);
    }

    private double barlength(EntityLivingBase target, int a, int b) {
        double diff = b - a;
        double ratio = target.getHealth() / target.getMaxHealth();
        return ratio * diff;
    }

    private int getHPcolor(EntityLivingBase target) {
        double ratio = target.getHealth() / target.getMaxHealth();
        if (ratio >= 0.5) {
            ratio -= 0.5;
            return new Color(255 - (int)(ratio * 255 * 2), 255, 0).getRGB();
        } else if (ratio < 0.2) {
            return new Color(255, 0, 0).getRGB();
        } else {
            return new Color(255, (int)(570 * ratio - 30), 0).getRGB();
        }
    }
}
