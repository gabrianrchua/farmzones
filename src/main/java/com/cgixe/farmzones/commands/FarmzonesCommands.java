package com.cgixe.farmzones.commands;

import com.cgixe.farmzones.handlers.*;
import com.cgixe.farmzones.types.CropType;
import com.cgixe.farmzones.types.FzFarm;
import com.cgixe.farmzones.types.FzLocation;
import com.cgixe.farmzones.types.FzZone;
import com.cgixe.farmzones.utils.Message;
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
            "&9/farmzones add [farm name] [zone name] [pos1|pos2] [wheat|carrot|potato|beetroot|netherwart]",
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
                Message.SendColoredMessage(sender, s);
            }
            return true; // return immediately upon help message
        }
        if (sender instanceof Player player) {
            switch (args[0].toLowerCase()) {
                case "create":
                    if (argLength == 2) {
                        String createFarmName = args[1];
                        if (CreateFarmHandler.createFarm(createFarmName, player.getName()) != null) {
                            Message.SendColoredMessage(player, "&aFarm " + createFarmName + " created!");
                        } else {
                            Message.SendErrorMessage(player, "Farm " + createFarmName + " already exists.");
                        }
                    } else {
                        Message.SendErrorMessage(sender, "Expected farm name to create.");
                        Message.SendErrorMessage(sender, "Usage: /farmzones create [farm name]");
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
                            Message.SendErrorMessage(player, "Crop type \"" + args[4] + "\" is invalid. Expected \"wheat\" \"carrot\" \"potato\" \"beetroot\" \"netherwart\"");
                            Message.SendErrorMessage(sender, "Usage: /farmzones add [farm name] [zone name] [pos1|pos2] [wheat|carrot|potato|beetroot|netherwart]");
                            return true;
                        }
                        FzLocation playerLocation = new FzLocation(player.getLocation());

                        FzZone newZone = AddZoneHandler.addZone(player.getName(), farmName, zoneName, type, isPosOne, playerLocation);
                        if (newZone != null) {
                            Message.SendColoredMessage(player, "&aAdded position " + (isPosOne ? '1' : '2') + " to zone " + zoneName + " in farm " + farmName + ".");
                            if (newZone.isComplete()) {
                                Message.SendColoredMessage(player, "&aZone is now complete!");
                            }
                        } else {
                            Message.SendErrorMessage(player, "Farm " + farmName + " does not exist!");
                        }
                    } else {
                        Message.SendErrorMessage(sender, "Expected farm name, zone name, position number, and crop type.");
                        Message.SendErrorMessage(sender, "Usage: /farmzones add [farm name] [zone name] [pos1|pos2] [wheat|carrot|potato|beetroot|netherwart]");
                    }
                    break;
                case "delete":
                    if (argLength > 1) {
                        boolean isDeletingFarm = args[1].equals("farm"); // else zone
                        if (isDeletingFarm) {
                            if (argLength == 3) {
                                String deleteFarm = args[2];
                                if (DeleteHandler.DeleteFarm(player.getName(), deleteFarm)) {
                                    Message.SendColoredMessage(player, "&aFarm " + deleteFarm + " deleted!");
                                } else {
                                    Message.SendErrorMessage(player, "Farm " + deleteFarm + " does not exist!");
                                }
                            } else {
                                Message.SendErrorMessage(sender, "Expected farm name to delete.");
                                Message.SendErrorMessage(sender, "Usage: /farmzones delete farm [farm name]");
                            }
                        } else {
                            if (argLength == 4) {
                                String deleteFromFarm = args[2];
                                String deleteZone = args[3];
                                if (DeleteHandler.DeleteZone(player.getName(), deleteFromFarm, deleteZone)) {
                                    Message.SendColoredMessage(player, "&aZone " + deleteZone + " from farm " + deleteFromFarm + " deleted!");
                                } else {
                                    Message.SendErrorMessage(player, "Either farm " + deleteFromFarm + " or zone " + deleteZone + " does not exist!");
                                }
                            } else {
                                Message.SendErrorMessage(sender, "Expected farm name and zone name.");
                                Message.SendErrorMessage(sender, "Usage: /farmzones delete zone [farm name] [zone name]");
                            }
                        }
                    } else {
                        Message.SendErrorMessage(sender, "Expected \"delete farm\" or \"delete zone\".");
                        Message.SendErrorMessage(sender, "Usage: /farmzones delete [farm|zone] [farm name] [zone name]");
                    }
                    break;
                case "harvest":
                    if (argLength == 2) {
                        String farmToHarvest = args[1];
                        if (HarvestFarmHandler.HarvestFarm(player, farmToHarvest)) {
                            Message.SendColoredMessage(player, "&aFarm " + farmToHarvest + " harvested!");
                        } else {
                            Message.SendErrorMessage(sender, "Farm " + farmToHarvest + " doesn't exist!");
                        }
                    } else {
                        Message.SendErrorMessage(sender, "Expected farm to harvest.");
                        Message.SendErrorMessage(sender, "Usage: /farmzones harvest [farm name]");
                    }
                    break;
                case "replant":
                    if (argLength == 2) {
                        String farmToReplant = args[1];
                        int replantResult = ReplantFarmHandler.ReplantFarm(player, farmToReplant);
                        if (replantResult == 0) {
                            Message.SendColoredMessage(player, "&aFarm " + farmToReplant + " replanted!");
                        } else if (replantResult == -999) {
                            Message.SendErrorMessage(sender, "Farm " + farmToReplant + " does not exist!");
                        } else {
                            Message.SendColoredMessage(player, "&6Farm " + farmToReplant + " partially replanted. " + replantResult + " crops couldn't be replanted (wrong soil type underneath, missing seeds in inventory, crop space wasn't empty).");
                        }
                    } else {
                        Message.SendErrorMessage(sender, "Expected farm to replant.");
                        Message.SendErrorMessage(sender, "Usage: /farmzones replant [farm name]");
                    }
                    break;
                case "list":
                    Message.SendColoredMessage(player, "&dYour FarmZones farms:");
                    Message.SendColoredMessage(player, manager.getPlayer(player.getName()).getFarmListString());
                    break;
                case "detail":
                    if (argLength == 2) {
                        String farmName = args[1];
                        FzFarm farm = manager.getPlayer(player.getName()).getFarm(farmName);
                        if (farm != null) {
                            Message.SendColoredMessage(player, farm.toString());
                        } else {
                            Message.SendErrorMessage(player, "Farm " + farmName + " does not exist!");
                        }
                    } else {
                        Message.SendErrorMessage(player, "Expected farm name.");
                        Message.SendErrorMessage(player, "Usage: /farmzones detail [farm name].");
                    }
                    break;
                case "help":
                    Bukkit.dispatchCommand(sender, "farmzones");
                    break;
                default:
                    Bukkit.dispatchCommand(sender, "farmzones");
                    Message.SendErrorMessage(sender, "FarmZones: Unknown command.");
                    break;
            }
        } else {
            // command requires player to run it
            Message.SendErrorMessage(sender, "This command must be run by a player.");
        }
        return true;
    }
}
