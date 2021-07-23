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
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Comparator;

public class TargetStrafe extends Module {
    private double normalX;
    private double normalZ;
    private EntityLivingBase target;
    public TargetStrafe() {
        super("TargetStrafe", Keyboard.CHAR_NONE, Category.COMBAT, "Automatically strafe around KillAura target. ");
    }
    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Radius", this, 2.68, 0.5, 8, false));
        vClient.instance.settingsManager.rSetting(new Setting("Speed", this, 0.28, 0.01, 1, false));
    }
    @Override
    public void onEnable() {
        normalX = mc.thePlayer.motionX;
        normalZ = mc.thePlayer.motionZ;
        super.onEnable();
    }
    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (!vClient.instance.moduleManager.getModulebyName("Killaura").isToggled())
            return;
        double radius = vClient.instance.settingsManager.getSettingByName("Radius").getValDouble();
        target = Killaura.getClosest(radius);
        if (target == null)
            return;
        if (mc.gameSettings.keyBindForward.pressed && !mc.gameSettings.keyBindSneak.pressed && mc.thePlayer.moveStrafing == 0F) {
            if (Math.sqrt(Math.pow(mc.thePlayer.posX - target.posX, 2) + Math.pow(mc.thePlayer.posZ - target.posZ, 2)) != 0) {
                double c1 = (mc.thePlayer.posX - target.posX) / (Math.sqrt(Math.pow(mc.thePlayer.posX - target.posX, 2) + Math.pow(mc.thePlayer.posZ - target.posZ, 2)));
                double s1 = (mc.thePlayer.posZ - target.posZ) / (Math.sqrt(Math.pow(mc.thePlayer.posX - target.posX, 2) + Math.pow(mc.thePlayer.posZ - target.posZ, 2)));
                if(Math.sqrt(Math.pow(mc.thePlayer.posX - target.posX,2) + Math.pow(mc.thePlayer.posZ - target.posZ,2)) <= radius && !target.isDead) {
                    double speed = vClient.instance.settingsManager.getSettingByName("Speed").getValDouble();
                    if(mc.gameSettings.keyBindLeft.pressed) {
                        mc.thePlayer.motionX = -speed*s1 - 1.38*speed*c1;
                        mc.thePlayer.motionZ = speed*c1 - 1.38*speed*s1;
                    }else if (mc.gameSettings.keyBindRight.pressed){
                        mc.thePlayer.motionX = speed*s1 - 1.38*speed*c1;
                        mc.thePlayer.motionZ = -speed*c1 - 1.38*speed*s1;
                    }
                }
            }
        }
    }
    @EventTarget
    public void onPost(EventPostMotionUpdate event) {
        if (target == null)
            return;
        mc.thePlayer.motionX = normalX;
        mc.thePlayer.motionZ = normalZ;
    }
}
