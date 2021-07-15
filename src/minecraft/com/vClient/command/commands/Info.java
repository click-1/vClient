package com.vClient.command.commands;

import com.vClient.command.Command;
import com.vClient.vClient;

public class Info extends Command {
    public Info() {
        super("Info", "Display client and command information", ".info", "i");
    }

    @Override
    public void onCommand(String[] args) {
        vClient.addChatMessage("\2472" + vClient.instance.name + " " + vClient.instance.version + "\2477 " + "developed by " + "\2472" + vClient.instance.creator);
        vClient.addChatMessage("\2473.info");
        vClient.addChatMessage("\2473.bind");
        vClient.addChatMessage("\2473.toggle");
        vClient.addChatMessage("\2473.say");
        vClient.addChatMessage("\2473.panic");
    }
}
