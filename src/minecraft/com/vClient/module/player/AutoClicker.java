package com.vClient.module.player;

import com.vClient.event.EventTarget;
import com.vClient.event.events.Event3D;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MathUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class AutoClicker extends Module {
    private String mode;
    private long lastClick = 0;
    private int delay = 0;
    private int minCPS, maxCPS;
    public AutoClicker() {
        super("AutoClicker", Keyboard.CHAR_NONE, Category.PLAYER, "Repeated swinging while holding left mouse button.");
    }

    /** Include old autoclicker version as option for football
     */
    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Vanilla");
        options.add("Watchdog");
        vClient.instance.settingsManager.rSetting(new Setting("Click Mode", this, "Vanilla", options));
        vClient.instance.settingsManager.rSetting(new Setting("Min CPS", this, 5, 1, 20, true));
        vClient.instance.settingsManager.rSetting(new Setting("Max CPS", this, 8, 1, 20, true));
    }

    private boolean shouldAutoClick() {
        return mc.gameSettings.keyBindAttack.isKeyDown() && (mc.objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK || mc.thePlayer.capabilities.isCreativeMode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.equals("Vanilla") && mc.gameSettings.keyBindAttack.isKeyDown())
            mc.clickMouse();
    }

    @EventTarget
    public void on3D(Event3D event) {
        if (!mode.equals("Watchdog"))
            return;
        long timestamp = System.currentTimeMillis();
        if (shouldAutoClick() && timestamp - lastClick >= delay) {
            mc.clickMouse();
            lastClick = timestamp;
            delay = MathUtil.randomClickDelay(minCPS, maxCPS);
        }
    }

    @Override
    public void onEnable() {
        mode = vClient.instance.settingsManager.getSettingByName("Click Mode").getValString();
        minCPS = (int) vClient.instance.settingsManager.getSettingByName("Min CPS").getValDouble();
        maxCPS = (int) vClient.instance.settingsManager.getSettingByName("Max CPS").getValDouble();
        updateDisplay(mode.equals("Vanilla") ? mode : minCPS + " - " + maxCPS);
        super.onEnable();
    }
}
