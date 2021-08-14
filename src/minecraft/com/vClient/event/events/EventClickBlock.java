package com.vClient.event.events;

import com.vClient.event.Event;
import net.minecraft.util.BlockPos;

public class EventClickBlock extends Event {
    private BlockPos blockPos;

    public EventClickBlock(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
