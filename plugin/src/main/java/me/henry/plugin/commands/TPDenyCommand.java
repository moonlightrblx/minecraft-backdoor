package me.henry.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.henry.plugin.utils.Messages;
import me.henry.plugin.utils.TPA;

public class TPDenyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (sender != null){
                 sender.sendMessage(Messages.formatErr("Only players can use this command!"));
            }

            return true;
        }
        Player player = (Player) sender;
        TPA.denyTPA(player);
        return true;
    }
}
