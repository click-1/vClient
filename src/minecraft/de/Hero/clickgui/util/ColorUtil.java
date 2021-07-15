package de.Hero.clickgui.util;

import java.awt.Color;
import com.vClient.vClient;

/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ColorUtil {
	
	public static Color getClickGUIColor(){
		return new Color((int) vClient.instance.settingsManager.getSettingByName("Red").getValDouble(), (int)vClient.instance.settingsManager.getSettingByName("Green").getValDouble(), (int)vClient.instance.settingsManager.getSettingByName("Blue").getValDouble());
	}
	public static int getRainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int)(seconds * 1000)) / (float)(seconds * 1000);
		return Color.HSBtoRGB(hue, saturation, brightness);
	}
	public static int getControlledRainbow(float seconds, float saturation, float brightness) {
		float hue = ((System.currentTimeMillis() % (int)(seconds * 100)) + (int)(0.2 * seconds * 100)) / (float)(seconds * 1000);
		return Color.HSBtoRGB(hue, saturation, brightness);
	}
}
