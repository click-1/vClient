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
import net.minecraft.item.*;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class InvManager extends Module {
    private int[] bestArmorDmg = new int[4];
    private int[] bestArmorSlot = new int[4];
    private float bestSwordDmg;
    private int bestSwordSlot, fishingRodSlot, bestBowDmg, bestBowSlot, bestSharpness;
    private ClockUtil clock = new ClockUtil();
    public InvManager() {
        super("InvManager", Keyboard.CHAR_NONE, Category.PLAYER, "Maintain ideal inventory setup (i.e. armor, tools, etc).");
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Delay", this, 150, 0, 1000, true));
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
                ItemStack oldArmor = mc.thePlayer.inventory.getStackInSlot(i + 36);
                if (oldArmor != null && oldArmor.getItem() != null)
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 8-i, 0, 1, mc.thePlayer);
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSlot < 9 ? bestSlot + 36 : bestSlot, 0, 1, mc.thePlayer);
                clock.resetTime();
                return;
            }
        }

        if (bestSwordSlot != -1 && bestSwordSlot != 0 && clock.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").getValDouble()) {
            clearSpace();
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSwordSlot < 9 ? bestSwordSlot + 36 : bestSwordSlot, 0, 2, mc.thePlayer);
            clock.resetTime();
            return;
        }

        if (fishingRodSlot != -1 && fishingRodSlot != 3 && clock.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").getValDouble()) {
            clearSpace();
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, fishingRodSlot < 9 ? fishingRodSlot + 36 : fishingRodSlot, 3, 2, mc.thePlayer);
            clock.resetTime();
            return;
        }

        if (bestBowSlot != -1 && bestBowSlot != 2 && clock.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").getValDouble()) {
            clearSpace();
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestBowSlot < 9 ? bestBowSlot + 36 : bestBowSlot, 2, 2, mc.thePlayer);
            clock.resetTime();
            return;
        }

        removeTrash();
    }

    private void removeTrash() {
        for (int i = 9; i < 36; i++) {
            if (mc.thePlayer.inventory.getStackInSlot(i) == null || mc.thePlayer.inventory.getStackInSlot(i).stackSize == 0)
                continue;
            Item item = mc.thePlayer.inventory.getStackInSlot(i).getItem();
            if (item == null)
                continue;
            if (isTrash(item) && clock.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").getValDouble()) {
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 4, 4, mc.thePlayer);
                clock.resetTime();
                break;
            }
        }
    }

    private boolean isTrash(Item item) {
        return item instanceof ItemArmor || item instanceof ItemSword
                || item instanceof ItemBow || item instanceof ItemFishingRod;
    }

    private void clearSpace() {
        if (mc.thePlayer.inventory.mainInventory.length >= 36)
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 17, 4, 4, mc.thePlayer);
    }

    private void findBest() {
        Arrays.fill(bestArmorDmg, -1);
        Arrays.fill(bestArmorSlot, -1);
        bestSwordSlot = -1;
        bestSwordDmg = -1;
        fishingRodSlot = -1;
        bestBowDmg = -1;
        bestBowSlot = -1;
        bestSharpness = -1;

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
                if (bestSwordDmg <= sword.getDamageVsEntity()) {
                    if (bestSharpness < EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack)) {
                        bestSwordDmg = sword.getDamageVsEntity();
                        bestSharpness = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
                        bestSwordSlot = i;
                    }
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
