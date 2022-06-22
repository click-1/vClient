package com.vClient.util.custom_font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAtomicOperationOnVolatileField")
public class CustomFontUtil {
    public static volatile int completed;
    public static MinecraftFontRenderer arial;
    public static MinecraftFontRenderer comfortaa;
    public static MinecraftFontRenderer descriptions;
    public static MinecraftFontRenderer hud;
    public static MinecraftFontRenderer notif;
    public static MinecraftFontRenderer opensans;
    private static Font arial_;
    private static Font comfortaa_;
    private static Font descriptions_;
    private static Font hud_;
    private static Font notif_;
    private static Font opensans_;

    private static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font;

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("fonts/" + location)).getInputStream();
                font = Font.createFont(0, is);
                locationMap.put(location, font);
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, +10);
        }

        return font;
    }

    public static boolean hasLoaded() {
        return completed >= 3;
    }

    public static void bootstrap() {
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            arial_ = getFont(locationMap, "arial.ttf", 15);
            comfortaa_ = getFont(locationMap, "comfortaa.ttf", 20);
            descriptions_ = getFont(locationMap, "arialn.ttf", 15);
            hud_ = getFont(locationMap, "roboto.ttf", 20);
            notif_ = getFont(locationMap, "arial.ttf", 18);
            opensans_ = getFont(locationMap, "opensans.ttf", 18);
            completed++;
        }).start();
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();

        while (!hasLoaded()) {
            try {
                //noinspection BusyWait
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        arial = new MinecraftFontRenderer(arial_, true, true);
        comfortaa = new MinecraftFontRenderer(comfortaa_, true, true);
        descriptions = new MinecraftFontRenderer(descriptions_, true, true);
        hud = new MinecraftFontRenderer(hud_, true, true);
        notif = new MinecraftFontRenderer(notif_, true, true);
        opensans = new MinecraftFontRenderer(opensans_, true, true);
    }
}