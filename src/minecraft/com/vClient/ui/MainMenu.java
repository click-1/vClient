package com.vClient.ui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class MainMenu extends GuiScreen {
    public MainMenu() {

    }
    public void initGui() {
        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/waterfall.jpg"));
        this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
    }
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }
    public void mouseClicked(int mouseX, int mouseY, int button) {

    }
    public void onGuiClosed() {

    }

}
