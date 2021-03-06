package de.Hero.clickgui.elements;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import com.vClient.module.Module;
import com.vClient.util.RenderUtil;
import com.vClient.util.custom_font.CustomFontUtil;
import com.vClient.util.custom_font.MinecraftFontRenderer;
import com.vClient.vClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import de.Hero.clickgui.Panel;
import de.Hero.clickgui.elements.menu.ElementCheckBox;
import de.Hero.clickgui.elements.menu.ElementComboBox;
import de.Hero.clickgui.elements.menu.ElementSlider;
import com.vClient.util.ColorUtil;
import de.Hero.clickgui.util.FontUtil;
import de.Hero.settings.Setting;


/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit me
 *
 *  @author HeroCode
 */
public class ModuleButton {
	public Module mod;
	public ArrayList<Element> menuelements;
	public Panel parent;
	public double x;
	public double y;
	public double width;
	public double height;
	public boolean extended = false;
	public boolean listening = false;
	private final MinecraftFontRenderer cfr = CustomFontUtil.arial;

	/*
	 * Konstrukor
	 */
	public ModuleButton(Module imod, Panel pl) {
		mod = imod;
		height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
		parent = pl;
		menuelements = new ArrayList<>();
		/*
		 * Settings wurden zuvor in eine ArrayList eingetragen
		 * dieses SettingSystem hat 3 Konstruktoren je nach
		 *  verwendetem Konstruktor ndert sich die Value
		 *  bei .isCheck() usw. so kann man ganz einfach ohne
		 *  irgendeinen Aufwand bestimmen welches Element
		 *  fr ein Setting bentigt wird :>
		 */
		if (vClient.instance.settingsManager.getSettingsByMod(imod) != null)
			for (Setting s : vClient.instance.settingsManager.getSettingsByMod(imod)) {
				if (s.isCheck()) {
					menuelements.add(new ElementCheckBox(this, s));
				} else if (s.isSlider()) {
					menuelements.add(new ElementSlider(this, s));
				} else if (s.isCombo()) {
					menuelements.add(new ElementComboBox(this, s));
				}
			}

	}

	/*
	 * Rendern des Elements 
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Color temp = ColorUtil.getClickGUIColor();
		//int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();
		
		/*
		 * Ist das Module an, wenn ja dann soll
		 *  #ein neues Rechteck in Gre des Buttons den Knopf als Toggled kennzeichnen
		 *  #sich der Text anders frben
		 */
		int textcolor = 0xffafafaf;
		if (mod.isToggled()) {
			//Gui.drawRect(x - 2, y, x + width + 2, y + height + 1, color);
			RenderUtil.drawHorizontalGradient(x - 2, y, x + width + 2, y + height + 1, ColorUtil.getBlueandPinkRainbow(6f, 0, 0.8f, 255), ColorUtil.getBlueandPinkRainbow(6f, -750, 0.8f, 255));
			textcolor = 0xffefefef;
		}
		
		/*
		 * Ist die Maus ber dem Element, wenn ja dann soll der Button sich anders frben
		 */
		if (isHovered(mouseX, mouseY)) {
			Gui.drawRect(x - 2, y, x + width + 2, y + height + 1, 0x55111111);
		}
		
		/*
		 * Den Namen des Modules in die Mitte (x und y) rendern
		 */
		FontUtil.drawStringWithShadow(mod.getName(), x + 2, y + 2, textcolor);
		if (mod.getKey() != 0 && mod.getName() != "ClickGUI")
			cfr.drawString(EnumChatFormatting.BOLD + Keyboard.getKeyName(mod.getKey()), x + width - cfr.getStringWidth(Keyboard.getKeyName(mod.getKey())) - 1.5, (float) (y + 5), new Color(255, 170, 0).getRGB());
	}

	/*
	 * 'true' oder 'false' bedeutet hat der Nutzer damit interagiert und
	 * sollen alle anderen Versuche der Interaktion abgebrochen werden?
	 */
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (!isHovered(mouseX, mouseY))
			return false;

		/*
		 * Rechtsklick, wenn ja dann Module togglen, 
		 */
		if (mouseButton == 0) {
			mod.toggle();

		} else if (mouseButton == 1) {
			/*
			 * Wenn ein Settingsmenu existiert dann sollen alle Settingsmenus 
			 * geschlossen werden und dieses geffnet/geschlossen werden
			 */
			if (menuelements != null && menuelements.size() > 0) {
				boolean b = !this.extended;
				vClient.instance.clickGui.closeAllSettings();
				this.extended = b;
				
				if(vClient.instance.settingsManager.getSettingByName("Sound").getValBoolean())
				if(extended)Minecraft.getMinecraft().thePlayer.playSound("tile.piston.out", 0.8f, 0.8f);else Minecraft.getMinecraft().thePlayer.playSound("tile.piston.in", 0.8f, 0.8f);
			}
		} else if (mouseButton == 2) {
			/*
			 * MidClick => Set keybind (wait for next key)
			 */
			listening = true;
		}
		return true;
	}

	public boolean keyTyped(char typedChar, int keyCode) throws IOException {
		/*
		 * Wenn listening, dann soll der nchster Key (abgesehen 'ESCAPE') als Keybind fr mod
		 * danach soll nicht mehr gewartet werden!
		 */
		if (listening) {
			if (keyCode != Keyboard.KEY_ESCAPE) {
				//Client.sendChatMessage("Bound '" + mod.getName() + "'" + " to '" + Keyboard.getKeyName(keyCode) + "'");
				mod.setKey(Keyboard.getKeyIndex(Keyboard.getKeyName(keyCode).toUpperCase()));
				vClient.addChatMessage(String.format("Bound \2473%s \2477to \2476%s", mod.getName(), Keyboard.getKeyName(mod.getKey()).toUpperCase()));
			}
			listening = false;
			return true;
		}
		return false;
	}

	public boolean isHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}

}
