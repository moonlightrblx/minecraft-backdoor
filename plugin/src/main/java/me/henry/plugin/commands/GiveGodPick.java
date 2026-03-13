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

public class GiveGodPick implements CommandExecutor {

    private final JavaPlugin plugin;
    private final NamespacedKey key;

    public GiveGodPick(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "god_pickaxe");
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
        meta.setDisplayName("§1GOD Pickaxe");

        CustomItemUtil.markAsCustomWithKey(meta, plugin, this.key);
        // meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1); // the old method of marking
        meta.setUnbreakable(true);
        meta.addEnchant(org.bukkit.enchantments.Enchantment.SILK_TOUCH, 1, true);
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED, 5, true); // efficiency 5
        meta.addEnchant(org.bukkit.enchantments.Enchantment.LOOT_BONUS_BLOCKS,15, true); // fortune 3
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 15, true); // unbreaking 15
        
        pick.setItemMeta(meta);
        if (!player.isOp()) {
            player.sendMessage("§cYou must be OP to use this command!");
            return true;
        }
        
        player.getInventory().addItem(pick);
        player.sendMessage("§aYou received a GOD Pickaxe!");

        return true;
    }
}
