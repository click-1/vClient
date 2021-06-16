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
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public class Killaura extends Module {
    private EntityLivingBase target;
    private long current, last;
    private int delay = 8;
    private float yaw, pitch;

    public Killaura() {
        super("Killaura", Keyboard.CHAR_NONE, Category.COMBAT);
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Crack Size", this, 5, 0, 15, true));
        vClient.instance.settingsManager.rSetting(new Setting("Existed", this, 30, 0, 500, true));
        vClient.instance.settingsManager.rSetting(new Setting("FOV", this, 360, 0, 360, true));
        vClient.instance.settingsManager.rSetting(new Setting("Range", this, 3.0, 3.0, 6.0, false));
        vClient.instance.settingsManager.rSetting(new Setting("AutoBlock", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("Invisibles", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Players", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("Animals", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Monsters", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Villagers", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Teams", this, true));
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        target = getClosest(vClient.instance.settingsManager.getSettingByName("Range").getValDouble());
        if (target == null) {
            return;
        }
        updateTime();
        yaw = mc.thePlayer.rotationYaw;
        pitch = mc.thePlayer.rotationPitch;
        boolean block = target != null && vClient.instance.settingsManager.getSettingByName("AutoBlock").getValBoolean() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
        if(block && mc.thePlayer.getDistanceToEntity(target) < 8F)
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
        if (current - last > 1000 / delay) {
            attack(target);
            resetTime();
        }
    }

    @EventTarget
    public void onPost(EventPostMotionUpdate event) {
        if (target == null) {
            return;
        }
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
    }

    private void attack(Entity entity) {
        for (int i = 0; i < vClient.instance.settingsManager.getSettingByName("Crack Size").getValDouble(); i++) {
            mc.thePlayer.onCriticalHit(entity);
        }
        mc.thePlayer.swingItem();
        mc.playerController.attackEntity(mc.thePlayer, entity);
    }

    private void updateTime() {
        current = System.nanoTime() / 1000000L;
    }

    private void resetTime() {
        last = System.nanoTime() / 1000000L;
    }

    private EntityLivingBase getClosest(double range) {
        double dist = range;
        EntityLivingBase the_target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (mc.thePlayer.getDistanceToEntity(player) <= dist && canAttack(player)) {
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

    private boolean canAttack(EntityLivingBase player) {
        boolean conditions = player != mc.thePlayer && player.isEntityAlive() && player.ticksExisted > vClient.instance.settingsManager.getSettingByName("Existed").getValDouble();
        if (!conditions)
            return false;
        if (player instanceof EntityPlayer && !vClient.instance.settingsManager.getSettingByName("Players").getValBoolean()) {
            return false;
        }
        if (player instanceof EntityAnimal && !vClient.instance.settingsManager.getSettingByName("Animals").getValBoolean()) {
            return false;
        }
        if (player instanceof EntityMob && !vClient.instance.settingsManager.getSettingByName("Mobs").getValBoolean()) {
            return false;
        }
        if (player instanceof EntityVillager && !vClient.instance.settingsManager.getSettingByName("Villagers").getValBoolean()) {
            return false;
        }
        if (player.isOnSameTeam(mc.thePlayer) && vClient.instance.settingsManager.getSettingByName("Teams").getValBoolean()) {
            return false;
        }
        if (player.isInvisible() && !vClient.instance.settingsManager.getSettingByName("Invisibles").getValBoolean())
            return false;
        if (!isInFOV(player, vClient.instance.settingsManager.getSettingByName("FOV").getValDouble())) {
            return false;
        }
        return true;
    }

    private boolean isInFOV(EntityLivingBase entity, double angle) {
        angle *= .5D;
        double angleDiff = getAngleDifference(mc.thePlayer.rotationYaw, getRotations(entity.posX, entity.posY, entity.posZ)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }

    private float getAngleDifference(float dir, float yaw) {
        float f = Math.abs(yaw - dir) % 360F;
        float dist = f > 180F ? 360F - f : f;
        return dist;
    }

    private float[] getRotations(double x, double y, double z) {
        double diffX = x + .5D - mc.thePlayer.posX;
        double diffY = (y + .5D) / 2D - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double diffZ = z + .5D - mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180D / Math.PI) - 90F;
        float pitch = (float)-(Math.atan2(diffY, dist) * 180D / Math.PI);
        return new float[] { yaw, pitch };
    }
}
