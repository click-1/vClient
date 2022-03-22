package com.vClient.util;

import java.awt.Color;
import com.vClient.vClient;

import static java.lang.Math.abs;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ColorUtil {
	public static Color baseColor = getClickGUIColor();
	public static int baseColorint = baseColor.getRGB();
	public static Color getClickGUIColor(){
		return new Color((int)vClient.instance.settingsManager.getSettingByName("Red").getValDouble(), (int)vClient.instance.settingsManager.getSettingByName("Green").getValDouble(), (int)vClient.instance.settingsManager.getSettingByName("Blue").getValDouble());
	}
	public static int getRainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (float)(seconds * 1000);
		return Color.HSBtoRGB(hue, saturation, brightness);
	}
	public static int getControlledRainbow(float seconds, float saturation, float brightness) {
		float hue = ((System.currentTimeMillis() % (int)(seconds * 100)) + (int)(0.2 * seconds * 100)) / (float)(seconds * 1000);
		return Color.HSBtoRGB(hue, saturation, brightness);
	}
	public static int getBlueandPinkRainbow(float seconds, int offset) {
		float hue = (((int)(abs((seconds*445)-((System.currentTimeMillis() + offset) % (int)(seconds*890)))) / (int)(seconds)) + 486) / 1000f;
		return Color.HSBtoRGB(hue, 0.5f, 1f);
	}
	public static int getaqua() {
		return new Color(107, 230, 255, 255).getRGB();
	}
}
