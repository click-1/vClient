package com.vClient.module;

import com.vClient.vClient;
import net.minecraft.client.Minecraft;

public class Module {
    protected static Minecraft mc = Minecraft.getMinecraft();
    private String name, displayMode, fullDisplayName,description;
    private int key;
    private Category category;
    private boolean toggled;
    protected boolean displayNotif = false;

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
        if (vClient.instance.settingsManager.getSettingByName("Sound").getValBoolean())
            mc.thePlayer.playSound("random.click", 0.8f, 0.8f);
        if (displayNotif)
            vClient.instance.notifications.addNotif(this);
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
    public String getDisplayMode() {
        return displayMode;
    }
    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getFullDisplayName() {
        return displayMode == null ? name : fullDisplayName;
    }
    public void setFullDisplayName(String fullDisplayName) {
        this.fullDisplayName = fullDisplayName;
    }
    public boolean getDisplayNotif() {
        return this.displayNotif;
    }
    public void setDisplayNotif(boolean displayNotif) {
        this.displayNotif = displayNotif;
    }
}
