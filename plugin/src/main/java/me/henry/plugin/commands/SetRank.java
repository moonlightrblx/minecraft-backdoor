package me.henry.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.henry.plugin.Plugin;
import me.henry.plugin.utils.Messages;
import me.henry.plugin.utils.Rank;

public class SetRank implements CommandExecutor {
	private final Plugin plugin;

	public SetRank(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
            if (sender != null){
                sender.sendMessage(Messages.formatErr("Only players can use this command!"));
        }
            return true;
        }

		Player player = (Player) sender;

		Rank rank = plugin.getRankManager().getRank(player);
		if (!player.isOp() && (rank == null || !rank.isAtLeast(Rank.ADMIN))) {
			player.sendMessage(Messages.formatErr("You do not have permission to use this command!"));
			return true;
		}

		if (args.length < 2) {
			sender.sendMessage(ChatColor.RED + "Usage: /setrank <player> <rank>");
			return true;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(ChatColor.RED + "Player not found!");
			return true;
		}

		try {
			Rank rank1 = Rank.valueOf(args[1].toUpperCase());
			if (rank1 == Rank.OWNER && !player.isOp()) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to set rank to OWNER!");
				return true;
			}
			
			if (rank1.compareTo(rank) >= 0 && !player.isOp()) {
				sender.sendMessage(ChatColor.RED + "You can only set ranks lower than your own!");
				return true;
			}

			plugin.getRankManager().setRank(target, rank1);
			sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + " to " + rank1.name());
		} catch (IllegalArgumentException e) {
			sender.sendMessage(ChatColor.RED + "Invalid rank! Options: OWNER, ADMIN, MOD, MEMBER");
		}

		return true;
	}
}
