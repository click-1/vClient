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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class MMDetector extends Module {
    private boolean search;
    final private Set<Item> weapons = new HashSet<>(Arrays.asList(Items.iron_sword, Items.stone_sword, Items.iron_shovel,
            Items.stick, Items.wooden_axe, Items.wooden_sword, Items.stone_shovel, Items.blaze_rod, Items.diamond_shovel,
            Items.feather, Items.pumpkin_pie, Items.golden_pickaxe, Items.carrot_on_a_stick, Items.bone, Items.carrot,
            Items.golden_carrot, Items.cookie, Items.diamond_axe, Items.golden_sword, Items.diamond_sword, Items.diamond_hoe,
            Items.shears, Items.fish, Items.name_tag, Items.apple, Items.boat, Items.dye, Items.spawn_egg, Items.prismarine_shard,
            Items.cooked_beef, Items.record_blocks, Items.quartz, Items.netherbrick, Items.cooked_chicken, Items.golden_hoe,
            Items.speckled_melon, Items.book));
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
        return itemStack.getDisplayName().contains("Knife") || weapons.contains(itemStack.getItem());
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
