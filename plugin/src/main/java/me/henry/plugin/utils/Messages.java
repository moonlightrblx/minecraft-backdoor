package me.henry.plugin.utils;

import org.bukkit.ChatColor;

import me.henry.plugin.Plugin;

public class Messages{
    public static String format(String message){
        return ChatColor.DARK_PURPLE + "[advantage] " + message;
    }
    public static String formatSuccess(String message){
        return ChatColor.DARK_PURPLE + "[advantage] " + ChatColor.GREEN + message;
    }
    public static String formatErr(String message){
        return ChatColor.DARK_PURPLE + "[advantage] " + ChatColor.RED + message;
    }
    public static String formatPrompt(String message){
        return ChatColor.DARK_PURPLE + "[advantage] " + ChatColor.YELLOW + message;
    }

    public static String formatChatPlus(String message){
        return ChatColor.ITALIC + "["+ ChatColor.GREEN+ "+" + ChatColor.RESET + "] " + ChatColor.GRAY + message;
    }
    public static String formatChatMinus(String message){
        return ChatColor.ITALIC + "["+ ChatColor.RED+ "-" + ChatColor.RESET + "] " + ChatColor.GRAY + message;
    }

    public static void sendAlert(org.bukkit.entity.Player player, String message) {
        for (org.bukkit.entity.Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
            if (p.isOp() || p.hasPermission("advantage.alerts") || Plugin.getInstance().getRankManager().getRank(p).isAtLeast(me.henry.plugin.utils.Rank.ADMIN)) {
                p.sendMessage(formatErr("Alert: " + player.getName() + " - " + message));
            }
        }
       org.bukkit.Bukkit.getLogger().info("[advantage] Alert: " + player.getName() + " - " + message);
    }
}