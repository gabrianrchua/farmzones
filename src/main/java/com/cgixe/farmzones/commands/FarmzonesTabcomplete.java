package com.cgixe.farmzones.commands;

import com.cgixe.farmzones.types.FzFarm;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.cgixe.farmzones.Farmzones.manager;

public class FarmzonesTabcomplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        int argLength = args.length;
        List<String> subCommands = new ArrayList<>();
        if (argLength == 1) {
            subCommands.addAll(List.of("create", "add", "delete", "harvest", "replant", "list", "detail"));
            return subCommands;
        } else if (argLength == 2) {
            switch (args[0]) {
                case "delete":
                    subCommands.addAll(List.of("farm", "zone"));
                    return subCommands;
                case "create":
                    subCommands.add("[farm name]");
                    return subCommands;
                case "add":
                case "harvest":
                case "replant":
                case "detail":
                    // add player's farms
                    if (sender instanceof Player player) {
                        subCommands.addAll(manager.getPlayer(player.getName()).getFarmList());
                        return subCommands;
                    }
            }
        } else if (argLength == 3) {
            // delete farm and delete zone both work
            if (args[0].equals("delete")) {
                // add player's farms
                if (sender instanceof Player player) {
                    subCommands.addAll(manager.getPlayer(player.getName()).getFarmList());
                    return subCommands;
                }
            } else if (args[0].equals("add")) {
                // add player's zone names
                if (sender instanceof Player player) {
                    FzFarm farm = manager.getPlayer(player.getName()).getFarm(args[1]);
                    if (farm != null) {
                        subCommands.addAll(farm.getZoneList());
                        return subCommands;
                    }
                }
            }
        } else if (argLength == 4) {
            if (args[0].equals("delete") && args[1].equals("zone")) {
                // add player's zone names
                if (sender instanceof Player player) {
                    FzFarm farm = manager.getPlayer(player.getName()).getFarm(args[2]);
                    if (farm != null) {
                        subCommands.addAll(farm.getZoneList());
                        return subCommands;
                    }
                }
            } else if (args[0].equals("add")) {
                subCommands.addAll(List.of("pos1", "pos2"));
                return subCommands;
            }
        } else if (argLength == 5) {
            if (args[0].equals("add")) {
                subCommands.addAll(List.of("wheat", "carrot", "potato", "beetroot", "netherwart"));
                return subCommands;
            }
        }

        return null;
    }
}
