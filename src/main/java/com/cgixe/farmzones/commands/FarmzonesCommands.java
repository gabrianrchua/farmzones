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

import static com.cgixe.farmzones.Farmzones.config;
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
            "&9/farmzones harvest-all",
            "    &7Harvest all owned FarmZones farms",
            "&9/farmzones replant [farm name]",
            "    &7Replant a FarmZones farm",
            "&9/farmzones replant-all",
            "    &7Replant all owned FarmZones farms",
            "&9/farmzones harvest-replant [farm name]",
            "    &7Harvests and then replants a FarmZones farm",
            "&9/farmzones harvest-replant-all",
            "    &7Harvests and then replants all owned FarmZones farms",
            "&9/farmzones bonemeal [farm name]",
            "    &7Applies bonemeal to all crops in a FarmZones farm",
            "&9/farmzones bonemeal-all",
            "    &7Applies bonemeal to all crops in all FarmZones farms",
            "&9/farmzones list",
            "    &7List all owned FarmZones farms",
            "&9/farmzones detail [farm name]",
            "    &7List details of a FarmZones farm"
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
            String playerName = player.getName();
            switch (args[0].toLowerCase()) {
                case "create":
                    if (argLength == 2) {
                        String createFarmName = args[1];
                        switch (CreateFarmHandler.createFarm(createFarmName, playerName)) {
                            case SUCCESS ->
                                    Message.SendColoredMessage(player, "&aFarm " + createFarmName + " created!");
                            case ERROR_FARM_EXISTS ->
                                    Message.SendErrorMessage(player, "Farm " + createFarmName + " already exists.");
                            case ERROR_MAX_FARMS ->
                                    Message.SendErrorMessage(player, "You have created the maximum number of farms (" + config.getInt("max-num-farms") + ") allowed on this server!");
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
                        switch (AddZoneHandler.addZone(playerName, farmName, zoneName, type, isPosOne, playerLocation)) {
                            case SUCCESS -> {
                                Message.SendColoredMessage(player, "&aAdded position " + (isPosOne ? '1' : '2') + " to zone " + zoneName + " in farm " + farmName + ".");
                                FzFarm farm = manager.getPlayer(playerName).getFarm(farmName);
                                if (farm != null) {
                                    FzZone zone = farm.getZone(zoneName);
                                    if (zone != null && zone.isComplete()) {
                                        Message.SendColoredMessage(player, "&aZone is now complete!");
                                    }
                                }
                            }
                            case ERROR_MAX_NUM_ZONES ->
                                    Message.SendErrorMessage(player, "You have added the maximum number of zones (" + config.getInt("max-num-zones") + ") allowed on this server! Create a new farm to continue adding zones.");
                            case ERROR_FARM_NOT_EXIST ->
                                    Message.SendErrorMessage(player, "Farm " + farmName + " does not exist!");
                            case ERROR_MAX_ZONE_SIZE ->
                                    Message.SendErrorMessage(player, "This zone exceeds the maximum size (" + config.getInt("max-zone-size") + ") allowed by this server! Create a smaller zone instead.");
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
                                if (DeleteHandler.DeleteFarm(playerName, deleteFarm)) {
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
                                if (DeleteHandler.DeleteZone(playerName, deleteFromFarm, deleteZone)) {
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
                    Message.SendColoredMessage(player, manager.getPlayer(playerName).getFarmListString());
                    break;
                case "detail":
                    if (argLength == 2) {
                        String farmName = args[1];
                        FzFarm farm = manager.getPlayer(playerName).getFarm(farmName);
                        if (farm != null) {
                            Message.SendColoredMessage(player, farm.toString());
                        } else {
                            Message.SendErrorMessage(player, "Farm " + farmName + " does not exist!");
                        }
                    } else {
                        Message.SendErrorMessage(player, "Expected farm name.");
                        Message.SendErrorMessage(player, "Usage: /farmzones detail [farm name]");
                    }
                    break;
                case "help":
                    Bukkit.dispatchCommand(sender, "farmzones");
                    break;
                case "harvest-all":
                    int farmsHarvested = HarvestFarmHandler.HarvestAllFarms(player);
                    if (farmsHarvested == 0) {
                        Message.SendColoredMessage(player, "&6You did not have any farms to harvest.");
                    } else {
                        Message.SendColoredMessage(player, "&a" + farmsHarvested + " farms were successfully harvested!");
                    }
                    break;
                case "replant-all":
                    int failed = ReplantFarmHandler.ReplantAllFarms(player);
                    if (failed == 0) {
                        Message.SendColoredMessage(player, "&aAll farms replanted!");
                    } else {
                        Message.SendColoredMessage(player, "&6Farms partially replanted. " + failed + " crops couldn't be replanted (wrong soil type underneath, missing seeds in inventory, crop space wasn't empty).");
                    }
                    break;
                case "harvest-replant":
                    if (argLength == 2) {
                        String farm = args[1];
                        Bukkit.dispatchCommand(player, "farmzones harvest " + farm);
                        Bukkit.dispatchCommand(player, "farmzones replant " + farm);
                    } else {
                        Message.SendErrorMessage(player, "Expected farm name.");
                        Message.SendErrorMessage(player, "Usage: /farmzones harvest-replant [farm name]");
                    }
                    break;
                case "harvest-replant-all":
                    Bukkit.dispatchCommand(player, "farmzones harvest-all");
                    Bukkit.dispatchCommand(player, "farmzones replant-all");
                    break;
                case "bonemeal":
                    if (argLength == 2) {
                        String farm = args[1];
                        int failedBonemeal = BonemealFarmHandler.BonemealFarm(player, farm);
                        if (failedBonemeal == 0) {
                            Message.SendColoredMessage(player, "&aFarm " + farm + " bonemealed!");
                        } else if (failedBonemeal == -999) {
                            Message.SendErrorMessage(player, "Farm " + farm + " doesn't exist!");
                        } else {
                            Message.SendColoredMessage(player, "&6Farm " + farm + " partially bonemealed. " + failedBonemeal + " crops couldn't be bonemealed (not enough bonemeal in inventory).");
                        }
                    } else {
                        Message.SendErrorMessage(player, "Expected farm name.");
                        Message.SendErrorMessage(player, "Usage: /farmzones bonemeal [farm name]");
                    }
                    break;
                case "bonemeal-all":
                    int failedBonemeal = BonemealFarmHandler.BonemealAllFarms(player);
                    if (failedBonemeal == 0) {
                        Message.SendColoredMessage(player, "&aAll farms bonemealed!");
                    } else {
                        Message.SendColoredMessage(player, "&6Farms partially bonemealed. " + failedBonemeal + " crops couldn't be bonemealed (not enough bonemeal in inventory).");
                    }
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
