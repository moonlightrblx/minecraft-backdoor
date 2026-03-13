package me.henry.plugin.listeners;
import java.util.Random;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.henry.plugin.utils.Messages;
import me.henry.plugin.utils.Rank;

public class MiscListener implements org.bukkit.event.Listener {
    private final me.henry.plugin.Plugin plugin; // me.henry.plugin.Plugin.getInstance();
    private final me.henry.plugin.utils.RankManager rankManager;
    private final Random random = new Random();

    public MiscListener(me.henry.plugin.Plugin plugin) {
        this.plugin = plugin;
        rankManager = plugin.rankManager;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (rankManager.getRank(p) == null) {
            rankManager.setRank(p, Rank.MEMBER); // default rank
        }
        rankManager.updateTablist(p);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (!player.hasPlayedBefore()) {
            event.setJoinMessage(Messages.formatChatPlus("Welcome " + player.getName() + " to the server!"));
            player.sendMessage(Messages.formatChatPlus("Welcome to the server!"));
            player.sendMessage(Messages.formatChatPlus("Heres some items to get you going!!"));
            player.sendMessage(Messages.formatChatPlus("You can run /dupe to dupe items!"));
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.WOODEN_SWORD));
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.WOODEN_PICKAXE));
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.BREAD,5));
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.TORCH,16));
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.CRAFTING_TABLE));
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.OAK_LOG,16));

        }
        else{
            event.setJoinMessage(Messages.formatChatPlus(player.getName()));
            player.sendMessage(Messages.formatChatPlus("Welcome back to the server!"));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(Messages.formatChatMinus(player.getName() + " has left the server."));
    }

     @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return; // no player killed it

        // 30% chance
        if (random.nextDouble() <= 0.3) {
            double reward = 0;

            if (event.getEntity() instanceof Animals) {
                reward = 10.0;
            } else if (event.getEntity() instanceof Monster) {
                reward = 15.0;
            }

            if (reward > 0) {
                plugin.getEconomyManager().deposit(killer, reward);
                killer.sendMessage("§aYou earned $" + reward + " for killing " 
                                   + event.getEntity().getName() + "!");
            }
    }
    }
}
