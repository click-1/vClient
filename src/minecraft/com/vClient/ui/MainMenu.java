package com.vClient.ui;

import com.vClient.discord.DiscordHandler;
import com.vClient.ui.login.GuiAltLogin;
import com.vClient.util.ColorUtil;
import com.vClient.util.RenderUtil;
import de.Hero.clickgui.util.FontUtil;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class MainMenu extends GuiScreen {
    public void initGui() {
        DiscordHandler.getInstance().getDiscordRP().update("Successfully injected vClient!", "Main Menu");
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/city.jpg"));
        this.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        this.drawGradientRect(0, 0, width, 75, 0xff000000, 0x00000000); //top
        this.drawGradientRect(0, height - 75, width, height, 0x00000000, 0xff000000); //bottom
        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/vclient_logo.png"));
        this.drawModalRectWithCustomSizedTexture((int)(.38*width), height/4, 0, 0, 125,125,125,125);
        RenderUtil.renderCustomAddress((int)(.835*width), (int)(.95*height));
        FontUtil.drawString("vClient b3", (int)(.015*width), (int)(.95*height), ColorUtil.getaqua());

        int side = 26, by = 35;
        for (int b = 0; b < 6; b++) {
            boolean hover = mouseX >= 15 && mouseX <= 40 && mouseY >= by && mouseY <= by + side;
            Gui.drawRect(15, by, 15 + side, by + side, hover ? new Color(0, 53, 101, 216).getRGB() : new Color(199, 199, 199, 106).getRGB());
            by += side + 5;
        }
        Gui.drawRect(0,0,0,0, new Color(255, 255, 255, 188).getRGB());
        GlStateManager.disableLighting();

        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/singleplayer.png"));
        this.drawModalRectWithCustomSizedTexture(18, 38, 0, 0, 20, 20, 20, 20);
        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/multiplayer.png"));
        this.drawModalRectWithCustomSizedTexture(18, 69, 0, 0, 20, 20, 20, 20);
        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/settings.png"));
        this.drawModalRectWithCustomSizedTexture(18, 100, 0, 0, 20, 20, 20, 20);
        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/login.png"));
        this.drawModalRectWithCustomSizedTexture(18, 131, 0, 0, 20, 20, 20, 20);
        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/language.png"));
        this.drawModalRectWithCustomSizedTexture(18, 162, 0, 0, 20, 20, 20, 20);
        mc.getTextureManager().bindTexture(new ResourceLocation("pictures/quit.png"));
        this.drawModalRectWithCustomSizedTexture(18, 193, 0, 0, 20, 20, 20, 20);
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        float y1 = 30, y2 = 61, y3 = 92, y4 = 123, y5 = 154, y6 = 185;
        float x1 = 15f;
        float x2 = 41f;
        if (mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y1 + 26) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            mc.displayGuiScreen(new GuiSelectWorld(this));
        } else if (mouseX >= x1 && mouseX <= x2 && mouseY >= y2 && mouseY <= y2 + 26) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (mouseX >= x1 && mouseX <= x2 && mouseY >= y3 && mouseY <= y3 + 26) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        } else if (mouseX >= x1 && mouseX <= x2 && mouseY >= y4 && mouseY <= y4 + 26) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            mc.displayGuiScreen(new GuiAltLogin(this));
        } else if (mouseX >= x1 && mouseX <= x2 && mouseY >= y5 && mouseY <= y5 + 26) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
        } else if (mouseX >= x1 && mouseX <= x2 && mouseY >= y6 && mouseY <= y6 + 26) {
            mc.shutdown();
        } else {
            return;
        }
    }

    public void onGuiClosed() {

    }
}
