package me.henry.plugin.utils;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import me.henry.plugin.Plugin;

public class CustomItemUtil {

    private static final NamespacedKey KEY = new NamespacedKey(Plugin.getInstance(), "custom_item");

    public static void markAsCustom(ItemStack item, JavaPlugin plugin) {
        if (item == null || item.getItemMeta() == null) return;
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(KEY, PersistentDataType.BYTE, (byte)1);
        item.setItemMeta(meta);
    }
    public static void markAsCustomWithName(ItemStack item, JavaPlugin plugin, String keyName) {
        if (item == null || item.getItemMeta() == null) return;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, keyName);
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1);
        item.setItemMeta(meta);
    }
    public static void markAsCustomWithKey(ItemMeta meta, JavaPlugin plugin, NamespacedKey key) {
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte)1);
    }

    public static void unmarkAsCustom(ItemStack item, JavaPlugin plugin) {
        if (item == null || item.getItemMeta() == null) return;
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(KEY);
        item.setItemMeta(meta);
    }
    public static boolean isCustom(ItemStack item, JavaPlugin plugin) {
        if (item == null || item.getItemMeta() == null) return false;
        return item.getItemMeta().getPersistentDataContainer().has(KEY, PersistentDataType.BYTE);
    }
    public static boolean isCustom(ItemStack item, String keyName) {
        if (item == null || item.getItemMeta() == null) return false;

        NamespacedKey key = new NamespacedKey(Plugin.getInstance(), keyName);
        return item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE);
    }
 
}
