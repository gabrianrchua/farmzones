package com.cgixe.farmzones.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FarmzonesCommands implements CommandExecutor {

    private final String[] HELP_LINES = {
            "=== FarmZones Help ===",
            "/farmzones",
            "    Print this help message",
            "farmzones create [name]",
            "    Create a new FarmZones farm",
            "/farmzones add [farm name] [zone name] [pos1|pos2]",
            "    Add zones to a FarmZones farm",
            "/farmzones delete farm [farm name]",
            "    Delete FarmZones farm and all its zones",
            "/farmzones delete zone [farm name] [zone name]",
            "    Delete FarmZones zone from a farm",
            "/farmzones harvest [farm name]",
            "    Harvest a FarmZones farm",
            "/farmzones replant [farm name]",
            "    Replant a FarmZones farm"
    };
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int argLength = args.length;
        if (argLength == 0) {
            for (String s : HELP_LINES) {
                sender.sendMessage(s);
            }
            return true; // return immediately
        }
        if (argLength == 1) {
            Bukkit.dispatchCommand(sender, "farmzones");
            SendErrorMessage(sender, "FarmZones: Missing arguments to command.");
            return true;
        }
        if (sender instanceof Player player) {
            // must have >1 arg
            switch (args[0].toLowerCase()) {
                case "create":
                    String createFarmName = args[1];
                    player.sendMessage("creating farm " + createFarmName);
                    break;
                case "add":
                    if (argLength == 4) {
                        String farmName = args[1];
                        String zoneName = args[2];
                        boolean isPosOne = args[3].equals("pos1"); // else pos2
                        Location playerLocation = player.getLocation();
                        player.sendMessage("Adding to zone " + zoneName + " in farm " + farmName);
                        player.sendMessage(isPosOne ? "position 1" : "position 2" + " at " + playerLocation.getX() + "," + playerLocation.getY() + "," + playerLocation.getZ());
                    } else {
                        Bukkit.dispatchCommand(sender, "farmzones");
                        SendErrorMessage(sender, "FarmZones: usage: /farmzones add [farm name] [zone name] [pos1|pos2]");
                    }
                    break;
                case "delete":
                    boolean isDeletingFarm = args[1].equals("farm"); // else zone
                    if (isDeletingFarm) {
                        if (argLength == 3) {
                            String deleteFarm = args[2];
                            player.sendMessage("Deleting farm " + deleteFarm);
                        } else {
                            Bukkit.dispatchCommand(sender, "farmzones");
                            SendErrorMessage(sender, "FarmZones: expected farm name to delete.");
                        }
                    } else {
                        if (argLength == 4) {
                            String deleteFromFarm = args[2];
                            String deleteZone = args[3];
                            player.sendMessage("Deleting zone " + deleteZone + " from farm " + deleteFromFarm);
                        } else {
                            Bukkit.dispatchCommand(sender, "farmzones");
                            SendErrorMessage(sender, "FarmZones: expected farm name and zone name.");
                        }
                    }
                    break;
                case "harvest":
                    String farmToHarvest = args[1];
                    player.sendMessage("Harvesting farm " + farmToHarvest);
                    break;
                case "replant":
                    String farmToReplant = args[1];
                    player.sendMessage("Replanting farm " + farmToReplant);
                    break;
                default:
                    Bukkit.dispatchCommand(sender, "farmzones");
                    SendErrorMessage(sender, "FarmZones: Unknown command.");
                    break;
            }
        } else {
            // command requires player to run it
            sender.sendMessage("This command must be run by a player.");
        }
        return true;
    }

    private void SendErrorMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c") + message);
    }
}
