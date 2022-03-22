package com.vClient.module.visual;

import com.vClient.event.EventTarget;
import com.vClient.event.events.Event2D;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import java.awt.*;
import java.util.ArrayList;

public class Effects extends Module {
    private ArrayList<PotionEffect> effects = new ArrayList<>();
    private final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");

    public Effects() {
        super("Effects", Keyboard.CHAR_NONE, Category.VISUAL, "Display active potion effects.");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        effects.clear();
        effects.addAll(mc.thePlayer.getActivePotionEffects());
    }

    @EventTarget
    public void on2D(Event2D event) {
        if (!effects.isEmpty()) {
            int x = (int) (.93 * new ScaledResolution(mc).getScaledWidth());
            int y = (int) (1.25 * new ScaledResolution(mc).getScaledHeight());
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            mc.fontRendererObj.drawStringWithShadow("", 0, 0, 16777215);

            GlStateManager.scale(3.0/4.0, 3.0/4.0, 1);
            for (PotionEffect effect : effects) {
                Potion potion = Potion.potionTypes[effect.getPotionID()];
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(inventoryBackground);

                if (potion.hasStatusIcon()) {
                    int i1 = potion.getStatusIconIndex();
                    drawTexturedModalRect(x, y, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                }
                mc.fontRendererObj.drawString(Potion.getDurationString(effect), x + 22, y + 5, new Color(125, 224, 255).getRGB());
                y -= 20;
            }
            GlStateManager.scale(4.0/3.0, 4.0/3.0, 1);
        }
    }

    private void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + height), mc.ingameGUI.zLevel).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + height), mc.ingameGUI.zLevel).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + width), (double)(y + 0), mc.ingameGUI.zLevel).tex((double)((float)(textureX + width) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), mc.ingameGUI.zLevel).tex((double)((float)(textureX + 0) * f), (double)((float)(textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }
}
