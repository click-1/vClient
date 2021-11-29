package com.vClient.module.render;

import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

public class Camera extends Module {
    public Camera() {
        super("Camera", Keyboard.CHAR_NONE, Category.RENDER, "Fix third-person viewing distance.");
    }
    @Override
    public void setup() {
        vClient.instance.settingsManager.rSetting(new Setting("View Distance", this, 4, 1, 32, true));
    }
    @Override
    public void onEnable() {
        mc.entityRenderer.thirdPersonDistance = (float) vClient.instance.settingsManager.getSettingByName("View Distance").getValDouble();
        super.onEnable();
    }
    @Override
    public void onDisable() {
        mc.entityRenderer.thirdPersonDistance = 4.0F;
        super.onDisable();
    }
}
