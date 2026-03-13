package me.henry.plugin.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import me.henry.plugin.utils.CustomItemUtil;

public class PickaxeListener implements Listener {

    private final JavaPlugin plugin;

    public PickaxeListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    private Boolean hasKey(ItemStack item, String key){
        if (item == null || item.getItemMeta() == null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(
                new org.bukkit.NamespacedKey(plugin, key),
                PersistentDataType.BYTE
        );
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;
       // if (item.getItemMeta().getPersistentDataContainer().has(
        //         new org.bukkit.NamespacedKey(plugin, "3x3_pickaxe"),
        //         PersistentDataType.BYTE
        // )) {
        if (CustomItemUtil.isCustom(item, "3x3_pickaxe")) { // 3x3 area
            Block block = event.getBlock();

            // 3x3 cube centered on the broken block
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Block target = block.getLocation().add(x, y, z).getBlock();
                        if (target.getType() != Material.AIR && target.getType() != Material.BEDROCK) {
                            target.breakNaturally(item);
                        }
                    }
                }
            }
        }

        if (CustomItemUtil.isCustom(item, "god_pickaxe")) { // auto smelts
            Block block = event.getBlock();
            if (block.getType() == Material.RAW_IRON || block.getType() == Material.RAW_COPPER || block.getType() == Material.RAW_GOLD) {
                Material drop;
                if (block.getType() == Material.RAW_IRON) {
                    drop = Material.IRON_INGOT;
                } else if (block.getType() == Material.RAW_COPPER) {
                    drop = Material.COPPER_INGOT;
                } else { // RAW_GOLD
                    drop = Material.GOLD_INGOT;
                }
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(drop));
                block.setType(Material.AIR);
                event.setCancelled(true);
            }
        }
    }

}
