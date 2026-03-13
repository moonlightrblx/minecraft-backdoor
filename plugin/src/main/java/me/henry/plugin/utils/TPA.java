package me.henry.plugin.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class TPA implements CommandExecutor {

    // store pending requests (target -> requester)
    private static final HashMap<UUID, UUID> requests = new HashMap<>();

    public static boolean SendTPA(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (sender != null){
                sender.sendMessage(Messages.formatErr("Only players can use this command!"));
        }
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Messages.formatErr("Usage: /tpa <player>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        // store the request
        requests.put(target.getUniqueId(), player.getUniqueId());

        player.sendMessage(ChatColor.GREEN + "Teleport request sent to " + target.getName());
        target.sendMessage(ChatColor.AQUA + player.getName() + " wants to teleport to you.");
        target.sendMessage(ChatColor.AQUA + "Type /tpaccept to accept or /tpdeny to deny.");
        return true;
    }

    // accept command
    public static boolean acceptTPA(Player player) {
        UUID requesterUUID = requests.get(player.getUniqueId());
        if (requesterUUID == null) {
            player.sendMessage(ChatColor.RED + "No teleport request found!");
            return false;
        }

        Player requester = Bukkit.getPlayer(requesterUUID);
        if (requester != null && requester.isOnline()) {
            requester.teleport(player.getLocation());
            requester.sendMessage(ChatColor.GREEN + "Teleport request accepted!");
            player.sendMessage(ChatColor.GREEN + "You accepted the teleport request.");
        }

        requests.remove(player.getUniqueId());
        return true;
    }

    // deny command
    public static boolean denyTPA(Player player) {
        UUID requesterUUID = requests.get(player.getUniqueId());
        if (requesterUUID == null) {
            player.sendMessage(ChatColor.RED + "No teleport request found!");
            return false;
        }

        Player requester = Bukkit.getPlayer(requesterUUID);
        if (requester != null && requester.isOnline()) {
            requester.sendMessage(ChatColor.RED + "Teleport request denied.");
            player.sendMessage(ChatColor.RED + "You denied the teleport request.");
        }

        requests.remove(player.getUniqueId());
        return true;
    }
}
