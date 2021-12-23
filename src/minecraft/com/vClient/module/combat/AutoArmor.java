package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.ClockUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

public class AutoArmor extends Module {
    private int[] bestDmgRedux = new int[4];
    private int[] bestArmorSlot = new int[4];
    private ClockUtil clock = new ClockUtil();
    public AutoArmor() {
        super("AutoArmor", Keyboard.CHAR_NONE, Category.COMBAT, "Actively wear best available armor.");
    }

    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("Delay", this, 150, 0, 1000, true));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        clock.updateTime();
        findBestArmor();

        for (int i = 0; i < 4; i++) {
            if (bestArmorSlot[i] != -1 && clock.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").getValDouble()) {
                int bestSlot = bestArmorSlot[i];
                ItemStack oldArmor = mc.thePlayer.inventory.getStackInSlot(i);
                if (mc.thePlayer.inventory.mainInventory.length >= 36)
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 35, 0, 4, mc.thePlayer);
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 8-i, 0, 1, mc.thePlayer);
                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestSlot < 9 ? bestSlot + 36 : bestSlot, 0, 1, mc.thePlayer);
                clock.resetTime();
            }
        }
    }

    private void findBestArmor() {
        Arrays.fill(bestDmgRedux, -1);
        Arrays.fill(bestArmorSlot, -1);

        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.armorItemInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof ItemArmor)
                bestDmgRedux[i] = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        }

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || !(itemStack.getItem() instanceof ItemArmor))
                continue;

            final ItemArmor armor = (ItemArmor) itemStack.getItem();
            int index = 3 - armor.armorType;
            if (bestDmgRedux[index] < armor.damageReduceAmount) {
                bestDmgRedux[index] = armor.damageReduceAmount;
                bestArmorSlot[index] = i;
            }
        }
    }
}
