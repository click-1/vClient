package com.vClient;

import com.vClient.command.CommandManager;
import com.vClient.event.EventManager;
import com.vClient.event.EventTarget;
import com.vClient.event.events.EventMessage;
import com.vClient.event.events.EventKey;
import com.vClient.module.ModuleManager;
import com.vClient.ui.ArrayListHUD;
import com.vClient.ui.Notifications;
import com.vClient.util.custom_font.CustomFontUtil;
import de.Hero.clickgui.ClickGUI;
import com.vClient.util.IconUtil;
import de.Hero.settings.SettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;

public class vClient {
    public String name = "vClient", version = "3", creator = "click_1";
    public static vClient instance = new vClient();
    public SettingsManager settingsManager;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public ArrayListHUD arrayListHUD;
    public Notifications notifications;
    public ClickGUI clickGui;

    public void startClient() {
        CustomFontUtil.bootstrap();
        settingsManager = new SettingsManager();
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        arrayListHUD = new ArrayListHUD();
        notifications = new Notifications();
        clickGui = new ClickGUI();
        System.out.println("[" + name + "] Starting client, b" + version + ", created by " + creator);
        Display.setIcon(IconUtil.getLoadingIcon());
        Display.setTitle(name + " b" + version);
        eventManager.register(this);
        prestartModules();
    }

    public void stopClient() {
        eventManager.unregister(this);
    }

    public static void addChatMessage(String message) {
        message = "\247b" + "[" + instance.name + "]" + "\2477 " + message;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(module -> module.toggle());
    }

    public void onMessage(EventMessage event) {
        commandManager.handleMessage(event);
    }

    private void prestartModules() {
        moduleManager.getModulebyName("Camera").silentToggle();
        moduleManager.getModulebyName("Effects").silentToggle();
        moduleManager.getModulebyName("Rotations").silentToggle();
        moduleManager.getModulebyName("Sprint").silentToggle();
        //TARGETS
        moduleManager.getModulebyName("Player").silentToggle();
        moduleManager.getModulebyName("Teammate").silentToggle();
    }
}
