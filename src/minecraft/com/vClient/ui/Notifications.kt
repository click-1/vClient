package com.vClient.ui

import com.vClient.module.Module
import com.vClient.util.ColorUtil.getaqua
import com.vClient.util.custom_font.CustomFontUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture
import net.minecraft.client.gui.Gui.drawRect
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ResourceLocation
import java.awt.Color
import java.util.*
import kotlin.math.sqrt

class Notifications {
    private val mc = Minecraft.getMinecraft()
    private val fr = CustomFontUtil.notif
    private var displayList: LinkedList<NotifQuery> = LinkedList()

    class NotifQuery(m: Module, timeStamp: Long, status: Boolean, animate: Boolean) {
        val module = m
        var timeStamp = timeStamp
        val status = status
        var animate = animate
    }

    fun addNotif(module: Module) {
        displayList.addFirst(NotifQuery(module, System.currentTimeMillis(), module.isToggled, true))
    }

    fun handleNotifs() {
        if (displayList.isEmpty())
            return
        if (System.currentTimeMillis() - displayList.last.timeStamp > 5000)
            displayList.removeLast()
        val width = ScaledResolution(mc).scaledWidth
        val height = ScaledResolution(mc).scaledHeight
        for (i in 0 until displayList.size) {
            if (System.currentTimeMillis()-displayList[i].timeStamp > 600)
                displayList[i].animate = false
            draw(displayList[i], i, width, height)
        }
    }

    private fun draw(query: NotifQuery, j: Int, width: Int, height: Int) {
        val x: Double
        if (query.animate)
            x = -width * 0.18 + width*0.2* 24.5*sqrt(System.currentTimeMillis().toDouble()-query.timeStamp) / 600
        else
            x = width * 0.02
        val y = height * (0.68-0.12*j)
        drawRect(x, y, x + 70, y + 26, Color(45, 45, 45, 95).rgb)
        fr.drawString(""+EnumChatFormatting.BOLD+query.module.name, x + 30, y.toFloat() + 5f, getaqua())
        fr.drawString(if (query.status) "Enabled" else "Disabled", x + 30, y.toFloat() + 16f, if (query.status) Color(85,255,85).rgb else Color(255,85,85).rgb)
        val text = if (query.status) ResourceLocation("pictures/check.png") else ResourceLocation("pictures/quit.png")
        mc.textureManager.bindTexture(text)
        drawModalRectWithCustomSizedTexture(x.toInt()+5, y.toInt()+4, 0f, 0f, 20, 20, 20f, 20f)
    }
}
