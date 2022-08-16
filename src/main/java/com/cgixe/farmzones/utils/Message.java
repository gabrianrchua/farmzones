package com.cgixe.farmzones.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Message {
    public static void SendErrorMessage(CommandSender sender, String message) {
        sender.sendMessage(ColorizeMessage("&cFarmZones: " + message));
    }
    public static void SendColoredMessage(CommandSender sender, String message) {
        String[] split = message.split("\n");
        for (String s : split) {
            sender.sendMessage(ColorizeMessage(s));
        }
    }
    public static String ColorizeMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
