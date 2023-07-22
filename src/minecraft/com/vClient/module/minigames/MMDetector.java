package com.vClient.module.minigames;

import com.mojang.authlib.GameProfile;
import com.vClient.event.EventTarget;
import com.vClient.event.events.Event2D;
import com.vClient.event.events.EventChat;
import com.vClient.event.events.EventTick;
import com.vClient.event.events.EventWorld;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.TargetUtil;
import com.vClient.vClient;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class MMDetector extends Module {
    private boolean search = false;
    public ArrayList<Integer> murderers = new ArrayList<>();

    public MMDetector() {
        super("MMDetector", Keyboard.CHAR_NONE, Category.MINIGAMES, "Detect who the murderer(s) are.");
    }

    @EventTarget
    public void onWorld(EventWorld event) {
        search = false;
        murderers.clear();
    }

    @EventTarget
    public void onChat(EventChat event) {
        if (event.getIncomingChat().contains("received their sword"))
            search = true;
    }

    @EventTarget
    public void on2D(Event2D event) {
        if (!murderers.isEmpty() && !isDeadSpectator(mc.thePlayer) && mc.theWorld.getEntityByID(murderers.get(0)) != null) {
            ScaledResolution sr = new ScaledResolution(mc);
            int x1 = sr.getScaledWidth() / 10;
            int y1 = 2 * sr.getScaledHeight() / 3;
            GuiInventory.drawEntityOnScreen(x1, y1, 20, -90, -30, (EntityLivingBase) mc.theWorld.getEntityByID(murderers.get(0)));
        }
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (!search) return;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase && TargetUtil.isPlayer(entity) && isPlayerInTabList(((EntityPlayer) entity).getGameProfile()) && !((EntityPlayer) entity).isPlayerSleeping() && !isDeadSpectator((EntityPlayer) entity)) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getCurrentEquippedItem() != null) {
                    if (player.getCurrentEquippedItem().getDisplayName().contains("Knife")) {
                        vClient.addChatMessage("\247c" +player.getName() + "\2477 is the murderer.");
                        murderers.add(player.getEntityId());
                        search = false;
                        return;
                    }
                }
            }
        }
    }

    private static boolean isPlayerInTabList(GameProfile profile) {
        // ids of players are sometimes different from this player list but using names works
        if (mc.thePlayer != null) {
            String name = profile.getName();
            for (NetworkPlayerInfo entry : mc.getNetHandler().getPlayerInfoMap()){
                if (entry.getGameProfile().getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDeadSpectator(EntityPlayer player) {
        PotionEffect activeInvisibilityEffect = player.getActivePotionEffect(Potion.invisibility);
        return player.capabilities.allowFlying || player.capabilities.isFlying || (activeInvisibilityEffect != null && activeInvisibilityEffect.getDuration() > 30000);
    }

    @Override
    public void onDisable() {
        murderers.clear();
        super.onDisable();
    }
}
