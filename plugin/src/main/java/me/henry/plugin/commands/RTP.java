package me.henry.plugin.commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.henry.plugin.Plugin;
import me.henry.plugin.utils.Messages;
import me.henry.plugin.utils.Rank;

public class RTP implements CommandExecutor {
    private final Plugin plugin;
    private final Random random = new Random();

    public RTP(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            if (sender != null) {
                sender.sendMessage(Messages.formatErr("Only players can use this command!"));
            }
            return true;
        }

        Player player = (Player) sender;
        player.sendMessage(ChatColor.GREEN + "Attempting to teleport you...");
        int radius = 5000;
        Location randomLocation = getRandomSafeLocation(player.getWorld(), radius);

        if (randomLocation != null) {
            player.teleport(randomLocation);
            player.sendMessage(ChatColor.GREEN + "You have been randomly teleported!");
        } else {
            player.sendMessage(Messages.formatErr("Could not find a safe location. Try again!"));
        }

        return true;
    }

    private Location getRandomSafeLocation(World world, int radius) {
        for (int i = 0; i < 20; i++) { // 20 tries to find a safe spot
            int x = random.nextInt(radius * 2) - radius;
            int z = random.nextInt(radius * 2) - radius;
            int y = world.getHighestBlockYAt(x, z);

            Location loc = new Location(world, x + 0.5, y, z + 2);

            Material block = world.getBlockAt(x, y - 1, z).getType();
            if (block.isSolid() && block != Material.LAVA && block != Material.WATER) {
                return loc;
            }
        }
        return null;
    }
}
