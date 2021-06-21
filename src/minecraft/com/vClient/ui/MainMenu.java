package com.vClient.ui;

import com.vClient.ui.login.GuiAltLogin;
import net.minecraft.client.gui.*;
//import net.minecraft.client.renderer.GlStateManager;
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
        /**
        GlStateManager.pushMatrix();
        GlStateManager.translate(width/2f, height/2f, 0);
        GlStateManager.scale(3, 3, 1);
        GlStateManager.translate(-(width/2f), -(height/2f), 0);
        this.drawCenteredString(mc.fontRendererObj, vClient.instance.name, width/2f, height/2f - mc.fontRendererObj.FONT_HEIGHT/2f - 5, 0xffff7000);
        GlStateManager.popMatrix();
         **/
    }
    public void mouseClicked(int mouseX, int mouseY, int button) {
        float x1 = (width/6)*0 + (width/6)/2f + 8 - mc.fontRendererObj.getStringWidth("Singleplayer")/2f;
        float x2 = (width/6)*1 + (width/6)/2f + 8 - mc.fontRendererObj.getStringWidth("Multiplayer")/2f;
        float x3 = (width/6)*2 + (width/6)/2f + 8 - mc.fontRendererObj.getStringWidth("Settings")/2f;
        float x4 = (width/6)*3 + (width/6)/2f + 8 - mc.fontRendererObj.getStringWidth("Login")/2f;
        float x5 = (width/6)*4 + (width/6)/2f + 8 - mc.fontRendererObj.getStringWidth("Language")/2f;
        float x6 = (width/6)*5 + (width/6)/2f + 8 - mc.fontRendererObj.getStringWidth("Quit")/2f;
        float y = height - 20;
        float ymax = y + mc.fontRendererObj.FONT_HEIGHT;
        if (mouseX >= x1 && mouseY >= y && mouseX < x1 + mc.fontRendererObj.getStringWidth("Singleplayer") && mouseY < ymax) {
            mc.displayGuiScreen(new GuiSelectWorld(this));
            return;
        } else if (mouseX >= x2 && mouseY >= y && mouseX < x2 + mc.fontRendererObj.getStringWidth("Multiplayer") && mouseY < ymax) {
            mc.displayGuiScreen(new GuiMultiplayer(this));
            return;
        } else if (mouseX >= x3 && mouseY >= y && mouseX < x3 + mc.fontRendererObj.getStringWidth("Settings") && mouseY < ymax) {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
            return;
        } else if (mouseX >= x4 && mouseY >= y && mouseX < x4 + mc.fontRendererObj.getStringWidth("Login") && mouseY < ymax) {
            mc.displayGuiScreen(new GuiAltLogin(this));
            return;
        } else if (mouseX >= x5 && mouseY >= y && mouseX < x5 + mc.fontRendererObj.getStringWidth("Language") && mouseY < ymax) {
            mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
            return;
        } else if (mouseX >= x6 && mouseY >= y && mouseX < x6 + mc.fontRendererObj.getStringWidth("Quit") && mouseY < ymax) {
            mc.shutdown();
            return;
        } else {
            return;
        }
    }
    public void onGuiClosed() {

    }
}
