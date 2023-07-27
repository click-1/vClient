package com.vClient.util;

import com.vClient.module.combat.AntiBot;
import com.vClient.vClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static void drawImage2(ResourceLocation image, float x, float y, int width, int height) {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glTranslatef(x, y, x);
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);
        glTranslatef(-x, -y, -x);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public static void drawHorizontalGradient(double left, double top, double right, double bottom, int startColor, int endColor) {
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

    public static void drawVerticalGradient(double left, double top, double right, double bottom, int startColor, int endColor) {
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

    public static void renderCustomAddress(int x, int y) {
        FontRenderer fr = mc.fontRendererObj;
        char[] chars = "best.vclient.gg".toCharArray();
        int offset = 0;
        for (char c : chars) {
            fr.drawStringWithShadow(String.valueOf(c), x, y, ColorUtil.getBlueandPinkRainbow(6f, offset, 1f, 255));
            x += fr.getCharWidth(c);
            offset -= 150;
        }
    }

    public static boolean canDisplay(EntityLivingBase entity) {
        AntiBot antiBot = (AntiBot) vClient.instance.moduleManager.getModulebyName("AntiBot");
        if (entity == null)
            return false;
        if (TargetUtil.isPlayer(entity) && !vClient.instance.moduleManager.getModulebyName("Player").isToggled())
            return false;
        if (antiBot.isToggled() && antiBot.isBot(entity))
            return false;
        if (TargetUtil.isAnimal(entity) && !vClient.instance.moduleManager.getModulebyName("Animal").isToggled())
            return false;
        if (TargetUtil.isMob(entity) && !vClient.instance.moduleManager.getModulebyName("Mob").isToggled())
            return false;
        if (entity instanceof EntityVillager && !vClient.instance.moduleManager.getModulebyName("Villager").isToggled())
            return false;
        if (entity.isInvisible() && !vClient.instance.moduleManager.getModulebyName("Invisible").isToggled())
            return false;
        if (!entity.isEntityAlive() && !vClient.instance.moduleManager.getModulebyName("Dead").isToggled())
            return false;
        if (TargetUtil.checkIfSameTeam(entity) && !vClient.instance.moduleManager.getModulebyName("Teammate").isToggled())
            return false;
        return true;
    }
}
