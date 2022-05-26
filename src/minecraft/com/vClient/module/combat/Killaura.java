package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.*;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.*;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.Comparator;

public class KillAura extends Module {
    public ArrayList<EntityLivingBase> targets;
    public EntityLivingBase active_target;
    private int delay, num_attacks;
    private static double range;
    public boolean blockingStatus = false;
    private int right_click = mc.gameSettings.keyBindUseItem.getKeyCode();
    private ClockUtil clock = new ClockUtil();
    public float[] targetRotations;

    public KillAura() {
        super("KillAura", Keyboard.CHAR_NONE, Category.COMBAT, "Attack entities.");
        setDisplayNotif(true);
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Crack Size", this, 0, 0, 15, true));
        vClient.instance.settingsManager.rSetting(new Setting("Existed", this, 30, 0, 500, true));
        vClient.instance.settingsManager.rSetting(new Setting("FOV", this, 360, 0, 360, true));
        vClient.instance.settingsManager.rSetting(new Setting("Range", this, 4.25, 3.0, 6.0, false));
        vClient.instance.settingsManager.rSetting(new Setting("CPS", this, 15, 1, 25, true));
        vClient.instance.settingsManager.rSetting(new Setting("Multi", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("AutoBlock", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("Players", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("Animals", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Mobs", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Villagers", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Invisible", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Dead", this, false));
        vClient.instance.settingsManager.rSetting(new Setting("Teams", this, true));

        updateDisplay("Sort");
    }

    @Override
    public void onEnable() {
        delay = (int) vClient.instance.settingsManager.getSettingByName("CPS").getValDouble();
        range = vClient.instance.settingsManager.getSettingByName("Range").getValDouble();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        stopBlocking();
        active_target = null;
        targetRotations = null;
        super.onDisable();
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        clock.updateTime();
        num_attacks = vClient.instance.settingsManager.getSettingByName("Multi").getValBoolean() ? 3 : 1;
        updateDisplay(num_attacks > 1 ? "Multi" : "Sort");

        targets = getClosest(range);
        if (targets.size() == 0)
            return;

        if (clock.elapsedTime() > 1000 / delay) {
            boolean canBlock = vClient.instance.settingsManager.getSettingByName("AutoBlock").getValBoolean() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
            if (canBlock)
                stopBlocking();
            mc.thePlayer.swingItem();
            for (int i = 0; i < num_attacks; i++) {
                if (i < targets.size()) {
                    active_target = targets.get(i);
                    targetRotations = PlayerUtil.getRotations(active_target);
                    event.setYaw(targetRotations[0]);
                    event.setPitch(targetRotations[1]);
                    attack(active_target);
                }
            }
            if (!mc.thePlayer.isBlocking() && !blockingStatus && canBlock)
                startBlocking();
            clock.resetTime();
        }
    }

    private boolean no_longer_attacking() {
        for (int i = 0; i < num_attacks; i++) {
            if (canAttack(targets.get(i)))
                return false;
        }
        return true;
    }

    @EventTarget
    public void onPost(EventPostMotionUpdate event) {
        if (targets == null || targets.size() == 0 || no_longer_attacking()) {
            if (blockingStatus)
                stopBlocking();
            active_target = null;
            targetRotations = null;
        }
    }

    private void attack(Entity entity) {
        for (int i = 0; i < vClient.instance.settingsManager.getSettingByName("Crack Size").getValDouble(); i++)
            mc.thePlayer.onCriticalHit(entity);
        mc.playerController.attackEntity(mc.thePlayer, entity);
    }

    public ArrayList<EntityLivingBase> getClosest(double range) {
        ArrayList<EntityLivingBase> list = new ArrayList<>();
        for (Entity entity : mc.theWorld.loadedEntityList)
            if (mc.thePlayer.getDistanceToEntity(entity) <= range && entity instanceof EntityLivingBase && entity != mc.thePlayer && canAttack((EntityLivingBase) entity))
                list.add((EntityLivingBase) entity);
        list.sort(Comparator.comparingDouble(e -> mc.thePlayer.getDistanceToEntity(e)));
        return list;
    }

    private void startBlocking() {
        KeyBinding.setKeyBindState(right_click, true);
        blockingStatus = true;
    }

    private void stopBlocking() {
        KeyBinding.setKeyBindState(right_click, false);
        blockingStatus = false;
    }

    private boolean canAttack(EntityLivingBase entity) {
        boolean conditions = entity != null && entity.ticksExisted > vClient.instance.settingsManager.getSettingByName("Existed").getValDouble() && mc.thePlayer.getDistanceToEntity(entity) <= range;
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
        if (checkIfSameTeam(entity) && vClient.instance.settingsManager.getSettingByName("Teams").getValBoolean())
            return false;
        if (entity.isInvisible() && !vClient.instance.settingsManager.getSettingByName("Invisible").getValBoolean())
            return false;
        if (!entity.isEntityAlive() && !vClient.instance.settingsManager.getSettingByName("Dead").getValBoolean())
            return false;
        if (!isInFOV(entity, vClient.instance.settingsManager.getSettingByName("FOV").getValDouble()))
            return false;
        return true;
    }

    private boolean checkIfSameTeam(EntityLivingBase entity) {
        if (mc.thePlayer.getTeam() != null && entity.getTeam() != null &&
                mc.thePlayer.getTeam().isSameTeam(entity.getTeam()))
            return true;
        if (mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            String targetName = entity.getDisplayName().getFormattedText().replace("§r", "");
            String clientName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
            return targetName.startsWith("§" + clientName.charAt(1));
        }
        return false;
    }

    private boolean isInFOV(EntityLivingBase entity, double angle) {
        angle *= .5D;
        double angleDiff = MathUtil.getAngleDifference(mc.thePlayer.rotationYaw, PlayerUtil.getRotations(entity)[0]);
        return (angleDiff > 0 && angleDiff < angle) || (-angle < angleDiff && angleDiff < 0);
    }
}
