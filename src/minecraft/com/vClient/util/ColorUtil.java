package com.vClient.util;

import java.awt.Color;
import com.vClient.vClient;
import kotlin.jvm.JvmStatic;

import java.util.regex.Pattern;

public class ColorUtil {
	public static Color baseColor = getClickGUIColor();
	public static int baseColorInt = baseColor.getRGB();
	private static Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

	@JvmStatic
	public static String stripColor(String input) {
		return COLOR_PATTERN.matcher(input).replaceAll("");
	}

	public static Color getClickGUIColor(){
		return new Color((int)vClient.instance.settingsManager.getSettingByName("Red").getValDouble(), (int)vClient.instance.settingsManager.getSettingByName("Green").getValDouble(), (int)vClient.instance.settingsManager.getSettingByName("Blue").getValDouble());
	}

	public static int getRainbow(float seconds, int offset, float saturation, float brightness, int alpha) {
		float hue = (System.currentTimeMillis() + (int)(seconds*offset)) % (int)(seconds*360) / (seconds*360);
		Color color = Color.getHSBColor(hue, saturation, brightness);
		Color adjusted = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		return adjusted.getRGB();
	}

	public static int getBlueandPinkRainbow(float seconds, int offset, float brightness, int alpha) {
		float hue = (((int)(Math.abs((seconds*445)-((System.currentTimeMillis() + offset) % (int)(seconds*890)))) / (int)(seconds)) + 486) / 1000f;
		Color color = Color.getHSBColor(hue, 0.5f, brightness);
		Color adjusted = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		return adjusted.getRGB();
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
