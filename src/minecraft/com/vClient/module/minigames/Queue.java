package com.vClient.module.minigames;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventChat;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.ClockUtil;
import com.vClient.vClient;
import de.Hero.settings.Setting;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;

public class Queue extends Module {
    private ClockUtil clock = new ClockUtil();
    private boolean flag;

    public Queue() {
        super("Queue", Keyboard.CHAR_NONE, Category.MINIGAMES, "Automatically queue into another game.");
    }

    @Override
    public void setup() {
        ArrayList<String> options1 = new ArrayList<>();
        ArrayList<String> options2 = new ArrayList<>();
        options1.add("Solo");
        options1.add("Teammate");
        options2.add("Insane");
        options2.add("Normal");
        vClient.instance.settingsManager.rSetting(new Setting("S/T", this, "Solo", options1));
        vClient.instance.settingsManager.rSetting(new Setting("N/I", this, "Insane", options2));
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (flag) {
            clock.updateTime();
            if (clock.elapsedTime() > 5000) {
                if (vClient.instance.settingsManager.getSettingByName("S/T").getValString().equalsIgnoreCase("solo")) {
                    if (vClient.instance.settingsManager.getSettingByName("N/I").getValString().equalsIgnoreCase("normal"))
                        mc.thePlayer.sendChatMessage("/play solo_normal");
                    else
                        mc.thePlayer.sendChatMessage("/play solo_insane");
                } else {
                    if (vClient.instance.settingsManager.getSettingByName("N/I").getValString().equalsIgnoreCase("normal"))
                        mc.thePlayer.sendChatMessage("/play teams_normal");
                    else
                        mc.thePlayer.sendChatMessage("/play teams_insane");
                }
                flag = false;
            }
        }
    }

    @EventTarget
    public void onChat(EventChat event) {
        if (event.getIncomingChat().contains("You died") || event.getIncomingChat().contains("You won")) {
            flag = true;
            vClient.addChatMessage("Joining new game in 5.0 sec...");
            clock.resetTime();
        }
    }
}