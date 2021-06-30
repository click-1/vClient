package com.vClient.command;

import com.vClient.command.commands.Bind;
import com.vClient.command.commands.Say;
import com.vClient.command.commands.Toggle;
import com.vClient.event.events.EventChat;
import com.vClient.vClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    private List<Command> commands = new ArrayList<>();
    private String prefix = ".";

    public CommandManager() {
        setup();
    }

    public void setup() {
        commands.add(new Toggle());
        commands.add(new Say());
        commands.add(new Bind());
    }

    public void handleChat(EventChat event) {
        String message = event.getMessage();
        if (!message.startsWith(prefix))
            return;
        event.setCancelled(true);
        message = message.substring(prefix.length());
        boolean foundCommand = false;
        if (message.split(" ").length > 0) {
            String commandName = message.split(" ")[0];
            for (Command c : commands) {
                if (c.getName().equalsIgnoreCase(commandName) || c.getAliases().contains(commandName)) {
                    c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
                    foundCommand = true;
                    break;
                }
            }
        }
        if (!foundCommand)
            vClient.addChatMessage("Couldn't find command.");
    }
}
