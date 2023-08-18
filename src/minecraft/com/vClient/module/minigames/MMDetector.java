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
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;

public class MMDetector extends Module {
    private boolean search;
    final public LinkedList<Integer> murderers = new LinkedList<>();
    final public LinkedList<Integer> detectives = new LinkedList<>();

    public MMDetector() {
        super("MMDetector", Keyboard.CHAR_NONE, Category.MINIGAMES, "Detect who the murderer(s) are.");
    }

    @EventTarget
    public void onWorld(EventWorld event) {
        search = false;
        murderers.clear();
        detectives.clear();
    }

    @EventTarget
    public void onChat(EventChat event) {
        if (event.getIncomingChat().contains("received their sword"))
            search = true;
    }

    @EventTarget
    public void on2D(Event2D event) {
        if (!murderers.isEmpty() && !isDeadSpectator(mc.thePlayer)) {
            ScaledResolution sr = new ScaledResolution(mc);
            final int x1 = sr.getScaledWidth() / 10;
            final int y1 = 2 * sr.getScaledHeight() / 3;
            for (int i = 0; i < murderers.size(); i++) {
                if (mc.theWorld.getEntityByID(murderers.get(i)) != null)
                    GuiInventory.drawEntityOnScreen(x1 + 30*i, y1, 20, -90, -30, (EntityLivingBase) mc.theWorld.getEntityByID(murderers.get(i)));
            }
        }
    }

    @EventTarget
    public void onTick(EventTick event) {
        if (!search) return;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityLivingBase && TargetUtil.isPlayer(entity) && isPlayerInTabList(((EntityPlayer) entity).getGameProfile()) && !((EntityPlayer) entity).isPlayerSleeping() && !isDeadSpectator((EntityPlayer) entity)) {
                final EntityPlayer player = (EntityPlayer) entity;
                if (player.getCurrentEquippedItem() != null) {
                    if (isMurderWeapon(player.getCurrentEquippedItem()) && !murderers.contains(player.getEntityId())) {
                        murderers.addFirst(player.getEntityId());
                        String name = player.getName();
                        vClient.addChatMessage("\247c" + name + "\2477 is a murderer.");
                    } else if (isDetectiveWeapon(player.getCurrentEquippedItem()) && !detectives.contains(player.getEntityId())) {
                        detectives.addFirst(player.getEntityId());
                        String name = player.getName();
                        vClient.addChatMessage("\247b" + name + "\2477 is a detective.");
                    }
                }
            }
        }
    }

    private boolean isMurderWeapon(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return itemStack.getDisplayName().contains("Knife") || item == Items.iron_sword || item == Items.stone_sword
                || item == Items.iron_shovel || item == Items.stick || item == Items.wooden_axe || item == Items.wooden_sword
                || item == Items.stone_shovel || item == Items.blaze_rod || item == Items.diamond_shovel || item == Items.feather
                || item == Items.pumpkin_pie || item == Items.golden_pickaxe || item == Items.carrot_on_a_stick || item == Items.bone
                || item == Items.carrot || item == Items.golden_carrot || item == Items.cookie || item == Items.diamond_axe
                || item == Items.golden_sword || item == Items.diamond_sword || item == Items.diamond_hoe || item == Items.shears
                || item == Items.fish || item == Items.name_tag || item == Items.apple || item == Items.boat || item == Items.dye
                || item == Items.spawn_egg || item == Items.prismarine_shard || item == Items.cooked_beef || item == Items.record_blocks
                || item == Items.quartz || item == Items.netherbrick || item == Items.cooked_chicken || item == Items.golden_hoe
                || item == Items.speckled_melon || item == Items.book;
    }

    private boolean isDetectiveWeapon(ItemStack itemStack) {
        return itemStack.getDisplayName().contains("Bow") || itemStack.getItem() == Items.bow;
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
        search = false;
        murderers.clear();
        detectives.clear();
        super.onDisable();
    }
}
