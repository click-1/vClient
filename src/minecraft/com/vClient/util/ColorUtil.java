package com.vClient.util;

import java.awt.Color;
import com.vClient.vClient;

import static java.lang.Math.abs;

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
	public static float[] four_color(int rgb) {
		float[] res = new float[4];
		Color c = new Color(rgb);
		res[0] = c.getRed();
		res[1] = c.getGreen();
		res[2] = c.getBlue();
		res[3] = c.getAlpha();
		return res;
	}
	public static int getaqua() {
		return new Color(107, 230, 255, 255).getRGB();
	}
}
