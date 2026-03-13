package me.henry.plugin.commands;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import me.henry.plugin.Plugin;
import me.henry.plugin.utils.CustomItemUtil;

public class CreateVillager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (!player.isOp()) {
            player.sendMessage("§cYou must be OP to use this command!");
            return true;
        }

        Villager villager = player.getWorld().spawn(player.getLocation(), Villager.class);
        villager.setCustomName("§6§lCPVP §7§lShop");
        villager.setCustomNameVisible(true);
        villager.setAdult();
        villager.setInvulnerable(true);
        villager.setCollidable(false);
        villager.setAI(false);
        villager.setProfession(Villager.Profession.NONE);

        List<MerchantRecipe> trades = new ArrayList<>();

        // Maxed Sword: 15 emeralds + 2 emerald blocks
        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setDisplayName("§1Void Sword");
        sword.setItemMeta(swordMeta);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
        sword.addEnchantment(Enchantment.KNOCKBACK, 2);
        sword.addEnchantment(Enchantment.DURABILITY, 3);
        
        MerchantRecipe swordTrade = new MerchantRecipe(sword, 999);
        swordTrade.addIngredient(new ItemStack(Material.EMERALD, 15));
        swordTrade.addIngredient(new ItemStack(Material.EMERALD_BLOCK, 2));
        trades.add(swordTrade);

        // Maxed Armor (emeralds + emerald blocks)
        trades.add(createArmorTrade(Material.NETHERITE_HELMET, 10, 2));
        trades.add(createArmorTrade(Material.NETHERITE_CHESTPLATE, 15, 3));
        trades.add(createArmorTrade(Material.NETHERITE_LEGGINGS, 15, 3));
        trades.add(createArmorTrade(Material.NETHERITE_BOOTS, 10, 2));

        // 3x3 Pickaxe
     
        
        ItemStack pick = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = pick.getItemMeta();
        meta.setDisplayName("§1Void Pickaxe");
        
        NamespacedKey key = new NamespacedKey(Plugin.getInstance(), "3x3_pickaxe");

        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1); // mark as special
        meta.setLore(java.util.Arrays.asList("§1Void Pickaxe"));
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DIG_SPEED, 5, true); // efficiency 5
        meta.addEnchant(org.bukkit.enchantments.Enchantment.LOOT_BONUS_BLOCKS,3, true); // fortune 3
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 15, true); // unbreaking 15
        pick.setItemMeta(meta);

        CustomItemUtil.markAsCustom(pick, Plugin.getInstance());
        MerchantRecipe pickTrade = new MerchantRecipe(pick, 999);
        pickTrade.addIngredient(new ItemStack(Material.EMERALD, 20));
        pickTrade.addIngredient(new ItemStack(Material.EMERALD_BLOCK, 3));

        trades.add(pickTrade);

        // PvP Essentials
        trades.add(createPvPItem(Material.END_CRYSTAL, 5, 1, 64));
        trades.add(createPvPItem(Material.RESPAWN_ANCHOR, 5, 1, 64));
        trades.add(createPvPItem(Material.GLOWSTONE, 10, 2, 64));
        trades.add(createPvPItem(Material.OBSIDIAN, 10, 2, 64));
        trades.add(createPvPItem(Material.ENDER_PEARL, 5, 0, 16));
        villager.setRecipes(trades);
        player.sendMessage("§aCrystal PvP villager spawned!");

        return true;
    }

    private MerchantRecipe createArmorTrade(Material type, int emeralds, int emeraldBlocks) {
        ItemStack armor = new ItemStack(type);
        ItemMeta swordMeta = armor.getItemMeta();
        swordMeta.setDisplayName("§1 Void Armor");
        armor.setItemMeta(swordMeta);
        armor.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        armor.addEnchantment(Enchantment.DURABILITY, 3);

        MerchantRecipe trade = new MerchantRecipe(armor, 999);
        trade.addIngredient(new ItemStack(Material.EMERALD, emeralds));
        trade.addIngredient(new ItemStack(Material.EMERALD_BLOCK, emeraldBlocks));

        return trade;
    }

    private MerchantRecipe createPvPItem(Material material, int emeralds, int emeraldBlocks, int amount) {
        ItemStack item = new ItemStack(material, amount);
        MerchantRecipe trade = new MerchantRecipe(item, 999);
        trade.addIngredient(new ItemStack(Material.EMERALD, emeralds));
        trade.addIngredient(new ItemStack(Material.EMERALD_BLOCK, emeraldBlocks));
        return trade;
    }
}
