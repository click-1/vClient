package com.vClient.command;

import com.vClient.command.commands.*;
import com.vClient.event.events.EventMessage;
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
        commands.add(new Info());
        commands.add(new Panic());
    }

    public void handleMessage(EventMessage event) {
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
                    c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length));
                    foundCommand = true;
                    break;
                }
            }
        }
        if (!foundCommand)
            vClient.addChatMessage("Couldn't find command.");
    }

    public Command getCommandByName(String name) {
        return commands.stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst().get();
    }
}
