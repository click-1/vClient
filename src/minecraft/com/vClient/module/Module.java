package com.vClient.module;

import com.vClient.vClient;
import net.minecraft.client.Minecraft;

public class Module {
    protected static Minecraft mc = Minecraft.getMinecraft();
    private String name, displayName, description;
    private int key;
    private Category category;
    private boolean toggled;

    public Module(String name, int key, Category category, String description) {
        this.name = name;
        this.key = key;
        this.category = category;
        this.description = description;
        toggled = false;
        setup();
    }
    public void setup() { }
    public void onEnable() {
        vClient.instance.eventManager.register(this);
    }
    public void onDisable() {
        vClient.instance.eventManager.unregister(this);
    }
    public void onToggle() {
        if(vClient.instance.settingsManager.getSettingByName("Sound").getValBoolean())
            mc.thePlayer.playSound("random.click", 0.8f, 0.8f);
    }
    public void toggle() {
        toggled = !toggled;
        onToggle();
        if (toggled)
            onEnable();
        else
            onDisable();
    }
    public void silentToggle() {
        toggled = !toggled;
        if (toggled)
            onEnable();
        else
            onDisable();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
    }
    public Category getCategory() {
        return this.category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public boolean isToggled() {
        return toggled;
    }
    public String getDisplayName() {
        return displayName == null ? name : displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
