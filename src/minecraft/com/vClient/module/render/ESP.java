package com.vClient.module.render;

import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.TargetUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class ESP extends Module {
    private Map<Integer, Boolean> glCapMap = new HashMap<>();
    private boolean outline, box3d;
    public ESP() {
        super("ESP", Keyboard.CHAR_NONE, Category.RENDER, "Extrasensory perception. ");
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Outline", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Box3D", this, true));
    }

    @Override
    public void onEnable() {
        outline = vClient.instance.settingsManager.getSettingByName("Outline").getValBoolean();
        box3d = vClient.instance.settingsManager.getSettingByName("Box3D").getValBoolean();
        super.onEnable();
    }

    public void draw() {
        if (!this.isToggled())
            return;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase && entity != mc.thePlayer && canDisplay((EntityLivingBase) entity) && mc.thePlayer.getDistanceToEntity(entity) >= 1F) {
                final EntityLivingBase entityLiving = (EntityLivingBase) entity;
                Color color = getColor(entityLiving);

                final RenderManager renderManager = mc.getRenderManager();
                final Timer timer = mc.timer;

                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                setGlCap(GL_BLEND, true);
                setGlCap(GL_TEXTURE_2D, false);
                setGlCap(GL_DEPTH_TEST, false);
                glDepthMask(false);

                final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks
                        - renderManager.renderPosX;
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks
                        - renderManager.renderPosY;
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks
                        - renderManager.renderPosZ;

                final AxisAlignedBB entityBox = entity.getEntityBoundingBox();
                final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                        entityBox.minX - entity.posX + x - 0.05D,
                        entityBox.minY - entity.posY + y,
                        entityBox.minZ - entity.posZ + z - 0.05D,
                        entityBox.maxX - entity.posX + x + 0.05D,
                        entityBox.maxY - entity.posY + y + 0.15D,
                        entityBox.maxZ - entity.posZ + z + 0.05D
                );

                if (outline) {
                    glLineWidth(2.5F - 2.5F / (float)Math.pow(mc.thePlayer.getDistanceToEntity(entityLiving), 2));
                    setGlCap(GL_LINE_SMOOTH, true);
                    GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 510F);
                    drawSelectionBoundingBox(axisAlignedBB);
                }
                if (box3d) {
                    GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 40 / 255F);
                    drawFilledBox(axisAlignedBB);
                }

                GlStateManager.resetColor();
                glDepthMask(true);
                glCapMap.forEach(ESP::setGlState);
            }
        }
    }

    public static void drawFilledBox(final AxisAlignedBB axisAlignedBB) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();

        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();

        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();

        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    public void setGlCap(final int cap, final boolean state) {
        glCapMap.put(cap, glGetBoolean(cap));
        setGlState(cap, state);
    }

    public static void setGlState(final int cap, final boolean state) {
        if (state)
            glEnable(cap);
        else
            glDisable(cap);
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        // Lower Rectangle
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();

        // Upper Rectangle
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();

        // Upper Rectangle
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();

        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();

        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();

        tessellator.draw();
    }

    public Color getColor(final EntityLivingBase entity) {
        if (entity.hurtTime > 0)
            return Color.RED;
        final char[] chars = entity.getDisplayName().getFormattedText().toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] != '§')
                continue;
            char colorkey = chars[i + 1];
            switch (colorkey) {
                case '0':   return new Color(0, 0, 0);

                case '1':   return new Color(0, 0, 170);

                case '2':   return new Color(0, 170, 0);

                case '3':   return new Color(0, 170, 170);

                case '4':   return new Color(170, 0, 0);

                case '5':   return new Color(170, 0 ,170);

                case '6':   return new Color(255, 170, 0);

                case '7':   return new Color(170, 170, 170);

                case '8':   return new Color(85, 85, 85);

                case '9':   return new Color(85, 85, 255);

                case 'a':   return new Color(85, 255, 85);

                case 'b':   return new Color(85, 255, 255);

                case 'c':   return new Color(255, 85, 85);

                case 'd':   return new Color(255, 85, 255);

                case 'e':   return new Color(255, 255, 85);
            }
            break;
        }
        return new Color(Integer.MAX_VALUE);
    }

    public boolean canDisplay(EntityLivingBase entity) {
        boolean conditions = entity != null && entity.isEntityAlive() && entity.ticksExisted > vClient.instance.settingsManager.getSettingByName("Existed").getValDouble();
        if (!conditions)
            return false;
        if (TargetUtil.isPlayer(entity) && !vClient.instance.settingsManager.getSettingByName("Players").getValBoolean())
            return false;
        if (TargetUtil.isAnimal(entity) && !vClient.instance.settingsManager.getSettingByName("Animals").getValBoolean())
            return false;
        if (TargetUtil.isMob(entity) && !vClient.instance.settingsManager.getSettingByName("Mobs").getValBoolean())
            return false;
        if (entity instanceof EntityVillager && !vClient.instance.settingsManager.getSettingByName("Villagers").getValBoolean())
            return false;
        if (entity.isInvisible() && !vClient.instance.settingsManager.getSettingByName("Invisible").getValBoolean())
            return false;
        if (!entity.isEntityAlive() && !vClient.instance.settingsManager.getSettingByName("Dead").getValBoolean())
            return false;
        return true;
    }
}
