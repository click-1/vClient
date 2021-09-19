package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventTick;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

public class WTap extends Module {
    private int delay = 0;
    private int reset_delay;
    private int forward = mc.gameSettings.keyBindForward.getKeyCode();
    public WTap() {
        super("WTap",Keyboard.CHAR_NONE, Category.PLAYER, "W-tap at set interval when facing target. ");
    }
    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("WPS", this, 1, 1, 5, true));
    }
    @Override
    public void onEnable() {
        reset_delay = (int) (20.0 / vClient.instance.settingsManager.getSettingByName("WPS").getValDouble());
    }
    @EventTarget
    public void onTick(EventTick event) {
        delay--;
        if (mc.pointedEntity instanceof EntityPlayer) {
            if (delay <= 0) {
                KeyBinding.setKeyBindState(forward, false);
                KeyBinding.setKeyBindState(forward, true);
                KeyBinding.setKeyBindState(forward, false);
                delay = reset_delay;
            }
        }
    }
}
