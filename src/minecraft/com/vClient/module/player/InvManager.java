package com.vClient.module.player;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.ClockUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemArmor;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class InvManager extends Module {
    private int[] bestArmorDmg = new int[4];
    private int[] bestArmorSlot = new int[4];
    private float bestSwordDmg;
    private int bestSwordSlot, fishingRodSlot, bestBowDmg, bestBowSlot;
    private ClockUtil clock = new ClockUtil();
    public InvManager() {
        super("InvManager", Keyboard.CHAR_NONE, Category.PLAYER, "Maintain ideal inventory setup (i.e. armor, tools, etc).");
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Delay", this, 200, 0, 1000, true));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        clock.updateTime();
        if (!(mc.currentScreen instanceof GuiInventory))
            return;

        findBest();
        for (int i = 0; i < 4; i++) {
            if (bestArmorSlot[i] != -1 && clock.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").getValDouble()) {
                clearSpace();
                int bestSlot = bestArmorSlot[i];
                ItemStack oldArmor = mc.thePlayer.inventory.getStackInSlot(i+36);
                if (oldArmor != null && oldArmor.getItem() != null)
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 8-i, 0, 1, mc.thePlayer);
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSlot < 9 ? bestSlot + 36 : bestSlot, 0, 1, mc.thePlayer);
                clock.resetTime();
            }
        }

        if (bestSwordSlot != -1 && bestSwordSlot != 0 && clock.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").getValDouble()) {
            clearSpace();
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSwordSlot < 9 ? bestSwordSlot + 36 : bestSwordSlot, 0, 2, mc.thePlayer);
            clock.resetTime();
        }

        if (fishingRodSlot != -1 && fishingRodSlot != 3 && clock.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").getValDouble()) {
            clearSpace();
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, fishingRodSlot < 9 ? fishingRodSlot + 36 : fishingRodSlot, 3, 2, mc.thePlayer);
            clock.resetTime();
        }

        if (bestBowSlot != -1 && bestBowSlot != 2 && clock.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").getValDouble()) {
            clearSpace();
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestBowSlot < 9 ? bestBowSlot + 36 : bestBowSlot, 2, 2, mc.thePlayer);
            clock.resetTime();
        }
    }

    private void clearSpace() {
        if (mc.thePlayer.inventory.mainInventory.length >= 36)
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 35, 0, 4, mc.thePlayer);
    }

    private void findBest() {
        Arrays.fill(bestArmorDmg, -1);
        Arrays.fill(bestArmorSlot, -1);
        bestSwordSlot = -1;
        bestSwordDmg = -1;
        fishingRodSlot = -1;
        bestBowDmg = -1;
        bestBowSlot = -1;

        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.armorItemInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof ItemArmor)
                bestArmorDmg[i] = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        }

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || (itemStack.getItem() == null))
                continue;

            if (itemStack.getItem() instanceof ItemArmor) {
                final ItemArmor armor = (ItemArmor) itemStack.getItem();
                int index = 3 - armor.armorType;
                if (bestArmorDmg[index] < armor.damageReduceAmount) {
                    bestArmorDmg[index] = armor.damageReduceAmount;
                    bestArmorSlot[index] = i;
                }
            }

            if (itemStack.getItem() instanceof ItemSword) {
                final ItemSword sword = (ItemSword) itemStack.getItem();
                if (bestSwordDmg < sword.getDamageVsEntity()) {
                    bestSwordDmg = sword.getDamageVsEntity();
                    bestSwordSlot = i;
                }
            }

            if (itemStack.getItem() instanceof ItemFishingRod) {
                final ItemFishingRod rod = (ItemFishingRod) itemStack.getItem();
                fishingRodSlot = i;
            }

            if (itemStack.getItem() instanceof ItemBow) {
                if (bestBowDmg < EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack)) {
                    bestBowDmg = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack);
                    bestBowSlot = i;
                }
            }
        }
    }
}
