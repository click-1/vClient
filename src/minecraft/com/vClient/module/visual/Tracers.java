package com.vClient.module.visual;

import com.vClient.event.EventTarget;
import com.vClient.event.events.Event3D;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.ColorUtil;
import com.vClient.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
    public Tracers() {
        super("Tracers", Keyboard.CHAR_NONE, Category.VISUAL, "Draw lines to targets.");
    }

    @EventTarget
    public void on3D(Event3D event) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBegin(GL11.GL_LINES);

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase && entity != mc.thePlayer && RenderUtil.canDisplay((EntityLivingBase) entity)) {
                drawTraces(entity, ColorUtil.getaqua());
            }
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.resetColor();
    }

    private void drawTraces(Entity entity, int color) {
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks
                - mc.getRenderManager().renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks
                - mc.getRenderManager().renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks
                - mc.getRenderManager().renderPosZ;

        final float alpha = (color >> 24 & 0xFF) / 255F;
        final float red = (color >> 16 & 0xFF) / 255F;
        final float green = (color >> 8 & 0xFF) / 255F;
        final float blue = (color & 0xFF) / 255F;
        GlStateManager.color(red, green, blue, alpha);

        GL11.glVertex3d(0, 1, 0);
        GL11.glVertex3d(x, y, z);
    }
}
