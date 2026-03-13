package me.henry.plugin.commands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import me.henry.plugin.utils.CustomItemUtil;

public class Give3x3Pick implements CommandExecutor {

    private final JavaPlugin plugin;
    private final NamespacedKey key;

    public Give3x3Pick(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "3x3_pickaxe");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        // Create pickaxe
        ItemStack pick = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = pick.getItemMeta();
        meta.setDisplayName("§k§13x3 Pickaxe");

        CustomItemUtil.markAsCustomWithKey(meta, plugin, this.key);
        
        //? meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1); // old method of marking
        
        // meta.setLore(java.util.Arrays.asList("§7A special pickaxe that mines a 3x3 area."));

        meta.addEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED, 5, true); // efficiency 5
        meta.addEnchant(org.bukkit.enchantments.Enchantment.LOOT_BONUS_BLOCKS,3, true); // fortune 3
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 15, true); // unbreaking 15

        pick.setItemMeta(meta);
        if (!player.isOp()) {
            player.sendMessage("§cYou must be OP to use this command!");
            return true;
        }
        player.getInventory().addItem(pick);
        player.sendMessage("§aYou received a 3x3 Pickaxe!");

        return true;
    }
}
