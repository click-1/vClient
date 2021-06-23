package com.vClient.module.render;

import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.RENDER);
    }
    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Classic");
        options.add("Jelly");
        vClient.instance.settingsManager.rSetting(new Setting("Design", this, "Classic", options));
        vClient.instance.settingsManager.rSetting(new Setting("Sound", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("tails", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("boxes", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("blur", this, true));
        vClient.instance.settingsManager.rSetting(new Setting("Red", this, 100, 0, 255, true));
        vClient.instance.settingsManager.rSetting(new Setting("Green", this, 145, 0, 255, true));
        vClient.instance.settingsManager.rSetting(new Setting("Blue", this, 255, 0, 255, true));
    }
    @Override
    public void onEnable() {
        mc.displayGuiScreen(vClient.instance.clickGui);
        toggle();
        super.onEnable();
    }
}
