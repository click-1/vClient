package com.vClient.command.commands;

import com.vClient.command.Command;
import com.vClient.vClient;

public class Info extends Command {
    public Info() {
        super("Info", "Display client and command info", ".info", "i");
    }

    @Override
    public void onCommand(String[] args) {
        vClient.addChatMessage("\2472" + vClient.instance.name + " " + vClient.instance.version + "\2477 " + "developed by " + "\2472" + vClient.instance.creator);
        vClient.addChatMessage("\2471.info");
        vClient.addChatMessage("\2471.bind");
        vClient.addChatMessage("\2471.toggle");
        vClient.addChatMessage("\2471.say");
    }
}
