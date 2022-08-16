package com.cgixe.farmzones.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class FarmzonesTabcomplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        int argLength = args.length;
        List<String> subCommands = new ArrayList<>();
        if (argLength == 1) {
            subCommands.addAll(List.of("create", "add", "delete", "harvest", "replant", "list", "detail"));
            return subCommands;
        } else if (argLength == 2) {
            if (args[0].equals("delete")) {
                subCommands.addAll(List.of("farm", "zone"));
                return subCommands;
            }
        } else if (argLength == 5) {
            if (args[0].equals("add")) {
                subCommands.addAll(List.of("pos1", "pos2"));
                return subCommands;
            }
        }

        return null;
    }
}
