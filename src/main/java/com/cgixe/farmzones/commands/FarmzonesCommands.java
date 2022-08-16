package com.cgixe.farmzones.commands;

import com.cgixe.farmzones.handlers.AddZoneHandler;
import com.cgixe.farmzones.handlers.CreateFarmHandler;
import com.cgixe.farmzones.types.CropType;
import com.cgixe.farmzones.types.FzLocation;
import com.cgixe.farmzones.types.FzZone;
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
            "    &7Replant a FarmZones farm",
            "&9/farmzones list",
            "    &7Lists all owned FarmZones farms",
            "&9/farmzones detail [farm name]",
            "    &7Lists details of a FarmZones farm"
    };
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int argLength = args.length;
        if (argLength == 0) {
            for (String s : HELP_LINES) {
                SendColoredMessage(sender, s);
            }
            return true; // return immediately upon help message
        }
        if (sender instanceof Player player) {
            switch (args[0].toLowerCase()) {
                case "create":
                    if (argLength == 2) {
                        String createFarmName = args[1];
                        if (CreateFarmHandler.createFarm(createFarmName, player.getName()) != null) {
                            SendColoredMessage(player, "&aFarm " + createFarmName + " created!");
                        } else {
                            SendErrorMessage(player, "Farm " + createFarmName + " already exists.");
                        }
                    } else {
                        SendErrorMessage(sender, "Expected farm name to create.");
                        SendErrorMessage(sender, "Usage: /farmzones create [farm name]");
                    }
                    break;
                case "add":
                    if (argLength == 5) {
                        String farmName = args[1];
                        String zoneName = args[2];
                        boolean isPosOne = args[3].equals("pos1"); // else "pos2"
                        CropType type;
                        try {
                            type = CropType.valueOf(args[4].toUpperCase());
                        } catch (Exception ex) {
                            SendErrorMessage(player, "Crop type \"" + args[4] + "\" is invalid. Expected \"wheat\" \"carrot\" \"potato\" \"beetroot\" \"netherwart\"");
                            SendErrorMessage(sender, "Usage: /farmzones add [farm name] [zone name] [pos1|pos2] [wheat|carrot|potato|beetroot|netherwart]");
                            return true;
                        }
                        FzLocation playerLocation = new FzLocation(player.getLocation());

                        FzZone newZone = AddZoneHandler.addZone(player.getName(), farmName, zoneName, type, isPosOne, playerLocation);
                        if (newZone != null) {
                            SendColoredMessage(player, "&aAdded position + " + (isPosOne ? '1' : '2') + " to zone " + zoneName + " in farm " + farmName + ".");
                            if (newZone.isComplete()) {
                                SendColoredMessage(player, "&aZone is now complete!");
                            }
                        } else {
                            SendErrorMessage(player, "Farm " + farmName + " does not exist!");
                        }
                    } else {
                        SendErrorMessage(sender, "Expected farm name, zone name, position number, and crop type.");
                        SendErrorMessage(sender, "Usage: /farmzones add [farm name] [zone name] [pos1|pos2] [wheat|carrot|potato|beetroot|netherwart]");
                    }
                    break;
                case "delete":
                    if (argLength > 1) {
                        boolean isDeletingFarm = args[1].equals("farm"); // else zone
                        if (isDeletingFarm) {
                            if (argLength == 3) {
                                String deleteFarm = args[2];
                                SendColoredMessage(player, "&aFarm " + deleteFarm + " deleted!");
                            } else {
                                SendErrorMessage(sender, "Expected farm name to delete.");
                                SendErrorMessage(sender, "Usage: /farmzones delete farm [farm name]");
                            }
                        } else {
                            if (argLength == 4) {
                                String deleteFromFarm = args[2];
                                String deleteZone = args[3];
                                SendColoredMessage(player, "&aZone " + deleteZone + " from farm " + deleteFromFarm + " deleted!");
                            } else {
                                SendErrorMessage(sender, "Expected farm name and zone name.");
                                SendErrorMessage(sender, "Usage: /farmzones delete zone [farm name] [zone name]");
                            }
                        }
                    } else {
                        SendErrorMessage(sender, "Expected \"delete farm\" or \"delete zone\".");
                        SendErrorMessage(sender, "Usage: /farmzones delete [farm|zone] [farm name] [zone name]");
                    }
                    break;
                case "harvest":
                    if (argLength == 2) {
                        String farmToHarvest = args[1];
                        SendColoredMessage(player, "&aFarm " + farmToHarvest + " harvested!");
                    } else {
                        SendErrorMessage(sender, "Expected farm to harvest.");
                        SendErrorMessage(sender, "Usage: /farmzones harvest [farm name]");
                    }
                    break;
                case "replant":
                    if (argLength == 2) {
                        String farmToReplant = args[1];
                        SendColoredMessage(player, "&aFarm " + farmToReplant + " replanted!");
                    } else {
                        SendErrorMessage(sender, "Expected farm to replant.");
                        SendErrorMessage(sender, "Usage: /farmzones replant [farm name]");
                    }
                    break;
                case "list":
                    // TODO: make this print prettier
                    SendColoredMessage(player, manager.getPlayer(player.getName()).getFarms().toString());
                case "detail":
                    if (argLength == 2) {
                        // TODO: make this print prettier
                        // TODO: add check for nonexistent farm
                        String farmName = args[1];
                        SendColoredMessage(player, manager.getPlayer(player.getName()).getFarm(farmName).toString());
                    }
                default:
                    Bukkit.dispatchCommand(sender, "farmzones");
                    SendErrorMessage(sender, "FarmZones: Unknown command.");
                    break;
            }
        } else {
            // command requires player to run it
            SendErrorMessage(sender, "This command must be run by a player.");
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
