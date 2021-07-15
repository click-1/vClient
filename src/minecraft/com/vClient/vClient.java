package com.vClient;

import com.vClient.command.CommandManager;
import com.vClient.event.EventManager;
import com.vClient.event.EventTarget;
import com.vClient.event.events.EventChat;
import com.vClient.event.events.EventKey;
import com.vClient.module.ModuleManager;
import com.vClient.ui.ArrayListHUD;
import de.Hero.clickgui.ClickGUI;
import de.Hero.clickgui.util.IconUtil;
import de.Hero.settings.SettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.GL_COLOR;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;

public class vClient {
    public String name = "vClient", version = "1", creator = "click_1";
    public static vClient instance = new vClient();
    public SettingsManager settingsManager;
    public EventManager eventManager;
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public ArrayListHUD arrayListHUD;
    public ClickGUI clickGui;

    public void startClient() {
        settingsManager = new SettingsManager();
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        commandManager = new CommandManager();
        arrayListHUD = new ArrayListHUD();
        clickGui = new ClickGUI();
        System.out.println("[" + name + "] Starting client, b" + version + ", created by " + creator);
        Display.setIcon(IconUtil.getLoadingIcon());
        Display.setTitle(name + " b"+version);
        eventManager.register(this);
    }

    public void stopClient() {
        eventManager.unregister(this);
    }

    public static void addChatMessage(String message) {
        message = "\2479" + "[" + instance.name + "]" + "\2477: " + message;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    @EventTarget
    public void onKey(EventKey event) {
        moduleManager.getModules().stream().filter(module -> module.getKey() == event.getKey()).forEach(module -> module.toggle());
    }

    public void onChat(EventChat event) {
        commandManager.handleChat(event);
    }
}
