package com.vClient.ui;

import com.vClient.ui.login.GuiAltLogin;
import com.vClient.vClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MainMenu extends GuiScreen {
    public MainMenu() {

    }
    public void initGui() {

    }
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/rtx_revamped.jpg"));
        this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        this.drawGradientRect(0, height - 100, width, height, 0x00000000, 0xff000000);

        String[] buttons = {"Singleplayer", "Multiplayer", "Settings", "Login", "Language", "Quit"};
        int count = 0;
        for (String s : buttons) {
            float x = (width/buttons.length)*count + (width/ buttons.length)/2f + 8 - mc.fontRendererObj.getStringWidth(s)/2f;
            float y = height - 20;
            boolean hovering = mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(s) && mouseY < y + mc.fontRendererObj.FONT_HEIGHT;
            this.drawCenteredString(mc.fontRendererObj, s, (width/buttons.length) * count + (width/ buttons.length)/2f + 8, height - 20, hovering ? 0xff0090ff : -1);
            count++;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(width/2f, height/2f, 0);
        GlStateManager.scale(3, 3, 1);
        GlStateManager.translate(-(width/2f), -(height/2f), 0);
        //this.drawCenteredString(mc.fontRendererObj, vClient.instance.name, width/2f, height/2f - mc.fontRendererObj.FONT_HEIGHT/2f - 5, 0xffff7000);
        GlStateManager.popMatrix();
    }
    public void mouseClicked(int mouseX, int mouseY, int button) {
        String[] buttons = {"Singleplayer", "Multiplayer", "Settings", "Login", "Language", "Quit"};
        int count = 0;
        for (String s : buttons) {
            float x = (width/buttons.length)*count + (width/ buttons.length)/2f + 8 - mc.fontRendererObj.getStringWidth(s)/2f;
            float y = height - 20;
            if (mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth(s) && mouseY < y + mc.fontRendererObj.FONT_HEIGHT) {
                switch (s) {
                    case "Singleplayer":
                        mc.displayGuiScreen(new GuiSelectWorld(this));
                        break;
                    case "Multiplayer":
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;
                    case "Settings":
                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        break;
                    case "Login":
                        mc.displayGuiScreen(new GuiAltLogin(this));
                        break;
                    case "Language":
                        mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
                        break;
                    case "Quit":
                        mc.shutdown();
                        break;
                }
            }

            count++;
        }
    }
    public void onGuiClosed() {

    }

}
