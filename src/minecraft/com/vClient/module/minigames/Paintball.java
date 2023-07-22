package com.vClient.module.minigames;

import com.vClient.event.EventTarget;
import com.vClient.event.events.Event2D;
import com.vClient.event.events.EventChat;
import com.vClient.event.events.EventUpdate;
import com.vClient.event.events.EventWorld;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.HashMap;

public class Paintball extends Module {
    private final HashMap<String, Integer> timermap = new HashMap<>();
    private final HashMap<String, Integer> idmap = new HashMap<>();
    private final HashMap<Integer, String> iconmap = new HashMap<>();
    private ArrayList<PotionEffect> activeks = new ArrayList();
    private final double endurance = 2.5;

    public Paintball() {
        super("Paintball", Keyboard.CHAR_NONE, Category.MINIGAMES, "Display Paintball killstreaks/notifications.");
        timermap.put("Triple Shot", (int)(30 * endurance * 20));
        timermap.put("Strong Arm", (int)(30 * endurance * 20));
        timermap.put("RAMBO", (int)(30 * endurance * 20));
        timermap.put("Quintuple Shot", (int)(20 * endurance * 20));
        timermap.put("Force Field", 26 * 20);
        timermap.put("Super Strong Arm", (int)(30 * endurance * 20));

        idmap.put("Triple Shot", 90);
        idmap.put("Strong Arm", 91);
        idmap.put("RAMBO", 92);
        idmap.put("Quintuple Shot", 93);
        idmap.put("Force Field", 94);
        idmap.put("Super Strong Arm", 95);

        iconmap.put(90, "diamond");
        iconmap.put(91, "snowball");
        iconmap.put(92, "tnt");
        iconmap.put(93, "diamond_block");
        iconmap.put(94, "obsidian");
        iconmap.put(95, "snowball");
    }

    @EventTarget
    public void onChat(EventChat event) {
        if (event.getIncomingChat().contains("click_1 activated")) {
            String ksname = event.getIncomingChat().substring(18);
            PotionEffect ks = new PotionEffect(idmap.get(ksname), timermap.get(ksname));
            activeks.add(ks);
        } else if (event.getIncomingChat().contains("You can now use Nuke")) {
            vClient.instance.notifications.addNotif(null, "Nuke");
        } else if (event.getIncomingChat().contains("You can now use Force Field")) {
            vClient.instance.notifications.addNotif(null, "Force Field");
        } else if (event.getIncomingChat().contains("You can now use Flashbang")) {
            vClient.instance.notifications.addNotif(null, "Flashbang");
        } else if (event.getIncomingChat().contains("You can now use Lightning")) {
            vClient.instance.notifications.addNotif(null, "Lightning");
        } else if (event.getIncomingChat().contains("You can now use Bomber Man")) {
            vClient.instance.notifications.addNotif(null, "Bomber Man");
        }
    }

    @EventTarget
    public void on2D(Event2D event) {
        if (activeks.isEmpty())
            return;
        for (int i = 0; i < activeks.size(); i++) {
            PotionEffect p = activeks.get(i);
            ItemStack icon = new ItemStack(Item.getByNameOrId(iconmap.get(p.getPotionID())));
            mc.getRenderItem().MYrenderItemIntoGUI(icon, 5, 80 + i*20);
            if (p.getPotionID() == 95)
                render_icon_subscript("2", 5, 80 + i*20);
            mc.fontRendererObj.drawString(StringUtils.ticksToElapsedTime(p.getDuration()), 25, 85 + i*20, 0x7de0ff);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (activeks.isEmpty())
            return;
        for (int i = 0; i < activeks.size(); i++) {
            if (activeks.get(i).getDuration() < 0)
                activeks.remove(i);
        }
        for (PotionEffect p : activeks)
            p.deincrementDuration();
    }

    @EventTarget
    public void onWorld(EventWorld event) {
        activeks = new ArrayList<>();
    }
    
    private void render_icon_subscript(String s, int xPos, int yPos) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();
        mc.fontRendererObj.drawStringWithShadow(s, (float) (xPos + 19 - 8), (float) (yPos + 6 + 3), 16777215);
        GlStateManager.enableDepth();
    }
}
