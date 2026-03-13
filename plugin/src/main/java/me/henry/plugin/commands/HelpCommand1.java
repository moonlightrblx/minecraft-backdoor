package me.henry.plugin.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.henry.plugin.utils.Messages;

public class HelpCommand1 implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // sender.sendMessage(Messages.format("&eAvailable commands:"));
        // for (Command command : sender.getServer().getCommandMap().getKnownCommands().values()) {
        //     if (!command.getName().startsWith("henry")) {
        //     sender.sendMessage(Messages.format("&7/" + command.getName() + " - " + command.getDescription()));
        //     }
        // }

        
        sender.sendMessage(Messages.formatErr("The help command is disabled on this server."));
        //  lol

        return true;
    }
}