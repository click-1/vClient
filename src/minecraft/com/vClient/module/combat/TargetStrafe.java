package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.Event3D;
import com.vClient.event.events.EventMove;
import com.vClient.event.events.EventPreMotionUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.ColorUtil;
import com.vClient.util.MovementUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class TargetStrafe extends Module{
    private int direction = 1;
    private KillAura killAura;

    public TargetStrafe() {
        super("TargetStrafe", Keyboard.CHAR_NONE, Category.COMBAT, "Automatically strafe around KillAura target.");
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Radius", this, 2.5, 0.1, 5.0, false));
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (mc.thePlayer.isCollidedHorizontally)
            this.direction = -this.direction;
        if (mc.gameSettings.keyBindLeft.pressed)
            this.direction = 1;
        if (mc.gameSettings.keyBindRight.pressed)
            this.direction = -1;
    }

    private boolean canStrafe() {
        return killAura.isToggled() && killAura.active_target != null && !mc.thePlayer.isSneaking() && mc.gameSettings.keyBindJump.isKeyDown();
    }

    @EventTarget
    public void onMove(EventMove event) {
        killAura = (KillAura) vClient.instance.moduleManager.getModulebyName("KillAura");
        if (canStrafe()) {
            strafe(event, MovementUtil.getSpeed(event.getX(), event.getZ()));
            if (checkVoid())
                event.setSafeWalk(true);
        }
    }

    private void strafe(EventMove event, double moveSpeed) {
        if (killAura.active_target == null)
            return;
        double radius = vClient.instance.settingsManager.getSettingByName("Radius").getValDouble();
        double forward = mc.thePlayer.getDistanceToEntity(killAura.active_target) <= radius ? 0.0 : 1.0;
        MovementUtil.setSpeed(event, moveSpeed, killAura.targetRotations[0], (double) direction, forward);
    }

    private boolean checkVoid() {
        for (int x = -1; x <= 0; x++) {
            for (int z = -1; z <= 0; z++) {
                if (isVoid(x, z))
                    return true;
            }
        }
        return false;
    }

    private boolean isVoid(int x, int z) {
        if (mc.thePlayer.posY < 0.0) {
            return true;
        }
        int off = 0;
        while (off < (int)(mc.thePlayer.posY + 2)) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset((double)x, (double)-off, (double)z);
            if (mc.theWorld != null && mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                off += 2;
                continue;
            }
            return false;
        }
        return true;
    }

    @EventTarget
    public void on3D(Event3D event) {
        EntityLivingBase target = killAura.active_target;
        double radius = vClient.instance.settingsManager.getSettingByName("Radius").getValDouble();
        if (canStrafe()) {
            GL11.glPushMatrix();
            GL11.glTranslated(
                    target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX,
                    target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY,
                    target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ
            );
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glRotatef(90F, 1F, 0F, 0F);


            GL11.glLineWidth(2F);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            float[] chroma = ColorUtil.four_color(ColorUtil.getBlueandPinkRainbow(8,0));
            GlStateManager.color(chroma[0]/255f,chroma[1]/255f,chroma[2]/255f,chroma[3]/255f);
            for (int i = 0; i <= 360; i += 5) // You can change circle accuracy  i += _
                GL11.glVertex2f((float)(Math.cos(i * Math.PI / 180.0) * radius), (float)(Math.sin(i * Math.PI / 180.0) * radius));
            GL11.glEnd();


            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glPopMatrix();
            GlStateManager.resetColor();
            GL11.glColor4f(1F, 1F, 1F, 1F);
        }
    }
}
