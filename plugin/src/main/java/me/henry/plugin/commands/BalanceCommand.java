package me.henry.plugin.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.henry.plugin.Plugin;

public class BalanceCommand implements CommandExecutor {
    private final Plugin plugin;

    public BalanceCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        double bal = plugin.getEconomyManager().getBalance(player);
        player.sendMessage("Your balance is $" + bal);
        return true;
    }
}
