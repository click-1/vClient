package com.vClient.module.world;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.event.events.EventUpdate;
import com.vClient.event.events.EventWorld;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class StaffAlert extends Module {
    private final ArrayList<int[]> possiblePositions = new ArrayList<>();
    private final ArrayList<Entity> possibleStaff = new ArrayList<>();
    private final ArrayList<Entity> possiblePlayers = new ArrayList<>();
    private int prev_num = 0;

    public StaffAlert() {
        super("StaffAlert", Keyboard.CHAR_NONE, Category.WORLD, "Alert if possible staff online.");
    }

    @Override
    public void onDisable() {
        clearAll();
        super.onDisable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer == null || mc.theWorld == null || mc.thePlayer.ticksExisted < 20)
            return;
        if (mc.thePlayer.ticksExisted % 50 == 0)
            possiblePositions.clear();

        possiblePositions.add(new int[] {(int)mc.thePlayer.posX, (int)mc.thePlayer.posZ});

        if (possibleStaff.size() != prev_num && possibleStaff.size() > 0) {
            vClient.addChatMessage(possibleStaff.size() + " staff members in current lobby.");
            prev_num = possibleStaff.size();
        }
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (mc.thePlayer == null || mc.theWorld == null)
            return;
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S14PacketEntity) {
            final S14PacketEntity packetEntity = (S14PacketEntity) event.getPacket();
            Entity entity = packetEntity.getEntity(mc.theWorld);

            if (packetEntity.getEntity(mc.theWorld) == null && (!possiblePlayers.contains(entity) && !possibleStaff.contains(entity))) {
                possibleStaff.add(entity);
                return;
            }

            if (!possiblePlayers.contains(entity) && !possibleStaff.contains(entity)) {
                // position check, most important part

                if (entity.isInvisible() && !possiblePlayers.contains(entity)) // check if entity never existed in the world before
                {
                    possibleStaff.add(entity);
                    return;
                }

                for (int[] positions : possiblePositions) {
                    if ((positions[0] == (int) entity.prevPosX && positions[1] == (int) entity.prevPosZ)
                            || (positions[0] == (int) entity.posX && positions[1] == (int) entity.posZ)) {
                        possibleStaff.add(entity);
                        return;
                    }
                }
            }
            possiblePlayers.add(entity);
        }
    }

    @EventTarget
    public void onWorld(EventWorld event) {
        clearAll();
        prev_num = 0;
    }

    private void clearAll() {
        possibleStaff.clear();
        possiblePlayers.clear();
        possiblePositions.clear();
    }
}
