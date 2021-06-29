package com.vClient;

import com.vClient.event.EventManager;
import com.vClient.event.EventTarget;
import com.vClient.event.events.EventKey;
import com.vClient.module.ModuleManager;
import com.vClient.ui.ArrayListHUD;
import de.Hero.clickgui.ClickGUI;
import de.Hero.settings.SettingsManager;
import org.lwjgl.opengl.Display;

public class vClient {
    public String name = "vClient", version = "1.0", creator = "click_1";
    public static vClient instance = new vClient();
    public SettingsManager settingsManager;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public ArrayListHUD arrayListHUD;
    public ClickGUI clickGui;

    public void startClient() {
        settingsManager = new SettingsManager();
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        arrayListHUD = new ArrayListHUD();
        clickGui = new ClickGUI();
        System.out.println("["+name+"] Starting client, b"+version+", created by "+creator);
        Display.setTitle(name + " b"+version);
        eventManager.register(this);
    }
    public void stopClient() {
        eventManager.unregister(this);
    }
    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(module -> module.toggle());
    }
}
