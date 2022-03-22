package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.Event2D;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.module.combat.KillAura;
import com.vClient.util.ColorUtil;
import com.vClient.util.MathUtil;
import com.vClient.util.custom_font.CustomFontUtil;
import com.vClient.util.custom_font.MinecraftFontRenderer;
import com.vClient.vClient;
import de.Hero.clickgui.util.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import java.awt.*;

public class TargetHUD extends Module {
    private int x1, y1, x2, y2;
    private MinecraftFontRenderer fr1 = CustomFontUtil.hud;
    private MinecraftFontRenderer fr2 = CustomFontUtil.arial;

    public TargetHUD() {
        super("TargetHUD", Keyboard.CHAR_NONE, Category.COMBAT, "Display current target info on screen.");
    }

    @EventTarget
    public void on2D(Event2D event) {
        EntityLivingBase target = ((KillAura) vClient.instance.moduleManager.getModulebyName("KillAura")).active_target;
        if (this.isToggled() && target != null)
            display(target);
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
        drawHorizontalGradient(x1,y1-2,x2,y1, new Color(255,122,122).getRGB(), new Color(122,255,255).getRGB());
        drawVerticalGradient(x1-2,y1-2,x1,y2+5,new Color(255,122,122).getRGB(), new Color(122,255,255).getRGB());
        drawVerticalGradient(x2,y1-2,x2+2,y2+5, new Color(122,255,255).getRGB(),new Color(255,122,122).getRGB());
        drawHorizontalGradient(x1,y2,(int)(x1+barlength(target,x1,x2)),y2+5, Color.HSBtoRGB(0,1,1), Color.HSBtoRGB(target.getHealth() / target.getMaxHealth() /3,1,1));
        Gui.drawRect((int)(x1+barlength(target,x1,x2)), y2, x2, y2+5, new Color(0 ,0, 0, 170).getRGB());

        fr2.drawString(MathUtil.round(mc.thePlayer.getDistanceToEntity(target), 2)+"", x1+78, y1+21, new Color(255, 111, 0).getRGB());
        fr1.drawString(target.getName(), x1 + 30, y1 + 4, ColorUtil.baseColorint);
        FontUtil.drawString("\u27B6", x1+98, y1+20, new Color(255, 111, 0).getRGB());
        GlStateManager.scale(1.6, 1.6, 1);
        FontUtil.drawString(MathUtil.round(target.getHealth(), 1) + "\u2764", (x1+30)/1.6, (y1+fr1.getHeight())/1.6 + 5, ColorUtil.baseColorint);
        GlStateManager.scale(.625,.625,1);
        fr2.drawString("blocking: " + (target instanceof EntityPlayer ? ((EntityPlayer) target).isBlocking() ? "true" : "false" : ""), x1 + 30, y1 + 22 + FontUtil.getFontHeight(), new Color(255, 111, 0).getRGB());

        GuiInventory.drawEntityOnScreen(x1 + 14, y2 - 4, 16, -90, -30, target);
        
        GlStateManager.scale(1.25,1.25,1);
        GlStateManager.translate(-55,-55,0);
    }

    private double barlength(EntityLivingBase target, int a, int b) {
        double diff = b - a;
        double ratio = target.getHealth() / target.getMaxHealth();
        return ratio * diff;
    }
    protected void drawHorizontalGradient(int left, int top, int right, int bottom, int startColor, int endColor)
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
    public void drawVerticalGradient(int left, int top, int right, int bottom, int startColor, int endColor)
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
        worldrenderer.pos((double)right, (double)top, (double)mc.ingameGUI.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)top, (double)mc.ingameGUI.zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)bottom, (double)mc.ingameGUI.zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double)right, (double)bottom, (double)mc.ingameGUI.zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
