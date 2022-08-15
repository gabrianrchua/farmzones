package com.cgixe.farmzones.commands;

import com.cgixe.farmzones.handlers.CreateFarmHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static com.cgixe.farmzones.Farmzones.manager;

public class FarmzonesCommands implements CommandExecutor {

    private final String[] HELP_LINES = {
            "=== FarmZones Help ===",
            "&9/farmzones",
            "    &7Print this help message",
            "&9farmzones create [name]",
            "    &7Create a new FarmZones farm",
            "&9/farmzones add [farm name] [zone name] [pos1|pos2]",
            "    &7Add zones to a FarmZones farm",
            "&9/farmzones delete farm [farm name]",
            "    &7Delete FarmZones farm and all its zones",
            "&9/farmzones delete zone [farm name] [zone name]",
            "    &7Delete FarmZones zone from a farm",
            "&9/farmzones harvest [farm name]",
            "    &7Harvest a FarmZones farm",
            "&9/farmzones replant [farm name]",
            "    &7Replant a FarmZones farm"
    };
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int argLength = args.length;
        if (argLength == 0) {
            for (String s : HELP_LINES) {
                SendColoredMessage(sender, s);
            }
            return true; // return immediately
        }
        // must have >1 arg
        if (argLength == 1) {
            Bukkit.dispatchCommand(sender, "farmzones");
            SendErrorMessage(sender, "Missing arguments to command.");
            return true;
        }
        if (sender instanceof Player player) {
            switch (args[0].toLowerCase()) {
                case "create":
                    String createFarmName = args[1];
                    CreateFarmHandler.createFarm(createFarmName, player.getName());
                    SendColoredMessage(player, "&aFarm " + createFarmName + " created!");
                    break;
                case "add":
                    if (argLength == 5) {
                        String farmName = args[1];
                        String zoneName = args[2];
                        boolean isPosOne = args[3].equals("pos1"); // else "pos2"
                        String type = args[4];
                        Location playerLocation = player.getLocation();
                        // DEBUG \/ MSG
                        player.sendMessage("Adding to zone " + zoneName + " in farm " + farmName + " type=" + type);
                        player.sendMessage((isPosOne ? "position 1" : "position 2") + " at " + playerLocation.getX() + "," + playerLocation.getY() + "," + playerLocation.getZ());
                    } else {
                        Bukkit.dispatchCommand(sender, "farmzones");
                        SendErrorMessage(sender, "Expected farm name, zone name, position number, and crop type.");
                        SendErrorMessage(sender, "Usage: /farmzones add [farm name] [zone name] [pos1|pos2] [wheat|carrot|potato|beetroot|netherwart]");
                    }
                    break;
                case "delete":
                    boolean isDeletingFarm = args[1].equals("farm"); // else zone
                    if (isDeletingFarm) {
                        if (argLength == 3) {
                            String deleteFarm = args[2];
                            SendColoredMessage(player, "&aFarm " + deleteFarm + " deleted!");
                        } else {
                            Bukkit.dispatchCommand(sender, "farmzones");
                            SendErrorMessage(sender, "Expected farm name to delete.");
                            SendErrorMessage(sender, "Usage: /farmzones delete farm [farm name]");
                        }
                    } else {
                        if (argLength == 4) {
                            String deleteFromFarm = args[2];
                            String deleteZone = args[3];
                            SendColoredMessage(player, "&aZone " + deleteZone + " from farm " + deleteFromFarm + " deleted!");
                        } else {
                            Bukkit.dispatchCommand(sender, "farmzones");
                            SendErrorMessage(sender, "Expected farm name and zone name.");
                            SendErrorMessage(sender, "Usage: /farmzones delete zone [farm name] [zone name]");
                        }
                    }
                    break;
                case "harvest":
                    String farmToHarvest = args[1];
                    SendColoredMessage(player, "&aFarm " + farmToHarvest + " harvested!");
                    break;
                case "replant":
                    String farmToReplant = args[1];
                    SendColoredMessage(player, "&aFarm " + farmToReplant + " replanted!");
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
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cFarmZones: ") + message);
    }
    private void SendColoredMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
