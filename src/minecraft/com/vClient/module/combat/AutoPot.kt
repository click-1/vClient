package com.vClient.module.combat

import com.vClient.event.EventTarget
import com.vClient.event.events.EventPostMotionUpdate
import com.vClient.event.events.EventPreMotionUpdate
import com.vClient.module.Category
import com.vClient.module.Module
import com.vClient.util.ClockUtil
import com.vClient.vClient
import de.Hero.settings.Setting
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemPotion
import net.minecraft.network.play.client.*
import net.minecraft.potion.Potion
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import org.lwjgl.input.Keyboard

class AutoPot : Module("AutoPot", Keyboard.CHAR_NONE, Category.COMBAT, "Automatically apply splash pots.") {
    private var throwing = false
    private var potIndex = -1
    private val throwTimer = ClockUtil()
    private val invTimer = ClockUtil()
    private val fallPeriod = ClockUtil()

    @Override
    override fun onEnable() {
        throwTimer.resetTime()
        invTimer.resetTime()
        fallPeriod.resetTime()
        super.onEnable()
    }

    @Override
    override fun setup() {
        vClient.instance.settingsManager.rSetting(Setting("InvSpoof", this, true))
    }

    @EventTarget
    fun onPre(event: EventPreMotionUpdate) {
        val killAura = vClient.instance.moduleManager.getModulebyName("KillAura")!! as KillAura
        if (throwing && mc.currentScreen !is GuiContainer && (!killAura.isToggled || killAura.active_target == null))
            event.pitch = 90f
    }

    @EventTarget
    fun onPost(event: EventPostMotionUpdate) {
        throwTimer.updateTime()
        invTimer.updateTime()
        fallPeriod.updateTime()
        if (fallPeriod.elapsedTime() < 250)
            return

        if (throwing && mc.thePlayer.onGround && mc.currentScreen !is GuiContainer && throwTimer.elapsedTime() > 250) {
            if (finalActiveCheck())
                return
            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(potIndex - 36))
            mc.netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(mc.thePlayer.heldItem))
            mc.netHandler.addToSendQueue(C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN))
            mc.netHandler.addToSendQueue(C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem))
            potIndex = -1
            throwing = false
            fallPeriod.resetTime()
            return
        }
        if (!throwing && mc.currentScreen !is GuiContainer) {
            val potionIndex = findPotion(36, 45)
            if (potionIndex != -1) {
                throwing = true
                potIndex = potionIndex
                throwTimer.resetTime()
            }
        }
        if (!throwing && vClient.instance.settingsManager.getSettingByName("InvSpoof").valBoolean && mc.currentScreen !is GuiContainer) {
            val invPotion = findPotion(9, 36)
            if (invPotion != -1 && invTimer.elapsedTime() > vClient.instance.settingsManager.getSettingByName("Delay").valDouble) {
                if (openHotBarSlot()) {
                    mc.netHandler.addToSendQueue(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
                    mc.playerController.windowClick(0, invPotion, 0, 1, mc.thePlayer)
                    mc.netHandler.addToSendQueue(C0DPacketCloseWindow())
                    invTimer.resetTime()
                } else {
                    for (i in 44 downTo 36) {
                        if (i == 36 || i == 38 || i == 39 || i == 44)
                            continue
                        val stack = mc.thePlayer.inventoryContainer.getSlot(i).stack
                        if (stack == null || stack.item is ItemPotion)
                            continue
                        mc.netHandler.addToSendQueue(C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT))
                        mc.playerController.windowClick(0, invPotion, 0, 0, mc.thePlayer)
                        mc.playerController.windowClick(0, i, 0, 0, mc.thePlayer)
                        mc.netHandler.addToSendQueue(C0DPacketCloseWindow())
                        invTimer.resetTime()
                        break
                    }
                }
            }
        }
    }

    private fun finalActiveCheck(): Boolean {
        if (potIndex == -1)
            return false
        val stack = mc.thePlayer.inventoryContainer.getSlot(potIndex).stack ?: return false
        val itemPotion = stack.item as ItemPotion
        val potID = itemPotion.getEffects(stack)[0].potionID
        if (mc.thePlayer.isPotionActive(potID)) {
            potIndex = -1
            throwing = false
            return true
        }
        return false
    }

    private fun findPotion(startSlot: Int, endSlot: Int): Int {
        for (i in startSlot until endSlot)
            if (findSinglePotion(i))
                return i
        return -1
    }

    private fun findSinglePotion(slot: Int): Boolean {
        val stack = mc.thePlayer.inventoryContainer.getSlot(slot).stack
        if (stack == null || stack.item !is ItemPotion || !ItemPotion.isSplash(stack.itemDamage))
            return false

        val itemPotion = stack.item as ItemPotion
        for (potionEffect in itemPotion.getEffects(stack)) {
            if (potionEffect.potionID == Potion.regeneration.id && !mc.thePlayer.isPotionActive(Potion.regeneration))
                return true
            else if (potionEffect.potionID == Potion.heal.id && mc.thePlayer.health / mc.thePlayer.maxHealth * 100F < 50f)
                return true
        }

        for (potionEffect in itemPotion.getEffects(stack))
            if (isUsefulPotion(potionEffect.potionID))
                return true
        return false
    }

    private fun isUsefulPotion(id: Int): Boolean {
        if (id == Potion.regeneration.id || id == Potion.heal.id || id == Potion.poison.id
                || id == Potion.blindness.id || id == Potion.harm.id || id == Potion.wither.id
                || id == Potion.digSlowdown.id || id == Potion.moveSlowdown.id || id == Potion.weakness.id) {
            return false
        }
        return !mc.thePlayer.isPotionActive(id)
    }

    fun openHotBarSlot(): Boolean {
        for (i in 36..44)
            mc.thePlayer.inventoryContainer.getSlot(i).stack ?: return true
        return false
    }
}
