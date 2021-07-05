package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventPostMotionUpdate;
import com.vClient.event.events.EventPreMotionUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class TargetStrafe extends Module {
    private int direction = 1;
    public TargetStrafe() {
        super("TargetStrafe", Keyboard.CHAR_NONE, Category.COMBAT);
    }
    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Radius", this, 6, 0.5, 8, false));
        vClient.instance.settingsManager.rSetting(new Setting("Speed", this, 0.07, 0.01, 1, false));
        vClient.instance.settingsManager.rSetting(new Setting("Angle", this, 180, 30, 180, true));
    }
    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (!vClient.instance.moduleManager.getModulebyName("Killaura").isToggled())
            return;
        EntityLivingBase target = getClosest(3.0);
        if (target == null)
            return;
        if (mc.gameSettings.keyBindForward.pressed && !mc.gameSettings.keyBindSneak.pressed && mc.thePlayer.moveStrafing == 0F) {
            double distance = Math.sqrt(Math.pow(mc.thePlayer.posX - target.posX, 2) + Math.pow(mc.thePlayer.posZ - target.posZ, 2));
            double strafeYaw = Math.atan2(target.posZ - mc.thePlayer.posZ, target.posX - mc.thePlayer.posX);
            double yaw = strafeYaw - (0.5 * Math.PI);
            double[] predict = {target.posX + (2 * (target.posX - target.lastTickPosX)), target.posZ + (2 * (target.posZ - target.lastTickPosZ))};

            double radius = vClient.instance.settingsManager.getSettingByName("Radius").getValDouble();
            double speed = vClient.instance.settingsManager.getSettingByName("Speed").getValDouble();
            int angle = (int) vClient.instance.settingsManager.getSettingByName("Angle").getValDouble();

            if ((distance - speed) > radius || Math.abs(((((yaw * 180 / Math.PI - mc.thePlayer.rotationYaw) % 360) + 540) % 360) - 180) > angle || !isAboveGround(predict[0], target.posY, predict[1]))
                return;

            double encirclement = distance - radius < -speed ? -speed : distance - radius;
            double encirclementX = -Math.sin(yaw) * encirclement;
            double encirclementZ = Math.cos(yaw) * encirclement;
            double strafeX = -Math.sin(strafeYaw) * speed * direction;
            double strafeZ = Math.cos(strafeYaw) * speed * direction;

            if (mc.thePlayer.onGround && (!isAboveGround(mc.thePlayer.posX + encirclementX + (2 * strafeX), mc.thePlayer.posY, mc.thePlayer.posZ + encirclementZ + (2 * strafeZ)) || mc.thePlayer.isCollidedHorizontally)) {
                direction *= -1;
                strafeX *= -1;
                strafeZ *= -1;
            }
            mc.thePlayer.motionX += (encirclementX + strafeX);
            mc.thePlayer.motionZ += (encirclementZ + strafeZ);
        }


    }

    private boolean isAboveGround(double x, double y, double z) {
        for (int i = (int) Math.ceil(y); y - 5 < i; i--)
            if (!mc.theWorld.isAirBlock(new BlockPos(x, i, z)))
                return true;
        return false;
    }

    private EntityLivingBase getClosest(double range) {
        double dist = range;
        EntityLivingBase the_target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (mc.thePlayer.getDistanceToEntity(player) <= dist) {
                    double currDist = mc.thePlayer.getDistanceToEntity(player);
                    if (currDist <= dist) {
                        dist = currDist;
                        the_target = player;
                    }
                }
            }
        }
        return the_target;
    }
}
