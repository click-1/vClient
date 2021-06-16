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
		return new Color((int) vClient.instance.settingsManager.getSettingByName("GuiRed").getValDouble(), (int)vClient.instance.settingsManager.getSettingByName("GuiGreen").getValDouble(), (int)vClient.instance.settingsManager.getSettingByName("GuiBlue").getValDouble());
	}
}
