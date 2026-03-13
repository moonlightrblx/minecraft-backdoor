package me.henry.plugin.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import me.henry.plugin.Plugin;
import me.henry.plugin.utils.CustomItemUtil;
import me.henry.plugin.utils.Messages;
import me.henry.plugin.utils.Rank;
public class Dupe implements CommandExecutor {

    private static final Set<Material> BLACKLIST = new HashSet<>(Arrays.asList(
            Material.BEDROCK,
            Material.COMMAND_BLOCK,
            Material.BARRIER,
            Material.STRUCTURE_BLOCK,
            Material.STRUCTURE_VOID,
            Material.NETHERITE_BLOCK,
            Material.NETHERITE_INGOT,
            Material.NETHERITE_SWORD,
            Material.NETHERITE_PICKAXE,
            Material.NETHERITE_AXE,
            Material.NETHERITE_SHOVEL,
            Material.NETHERITE_HOE,
            Material.NETHERITE_HELMET,
            Material.NETHERITE_CHESTPLATE,
            Material.NETHERITE_LEGGINGS,
            Material.NETHERITE_BOOTS,
            Material.DRAGON_EGG,
            Material.ELYTRA
    ));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        Rank rank = Plugin.getInstance().getRankManager().getRank(player);

        

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getType() == Material.AIR) {
            player.sendMessage(Messages.formatErr("You must hold an item to dupe!"));
            return true;
        }

        // Check for blacklisted items
        if (isBlacklisted(item) && !player.isOp()) {
            player.sendMessage(Messages.formatErr("You cannot dupe this item!"));
            return true;
        }

        // Prevent duping custom items
        if (item.hasItemMeta() && CustomItemUtil.isCustom(item, Plugin.getInstance())) {
            player.sendMessage(Messages.formatErr("You cannot dupe a custom item!"));
            return true;
        }

        // Safe duplication with clone
        try {
            player.getInventory().addItem(item.clone());
            player.sendMessage(Messages.formatSuccess("Duplicated: " + item.getType() + " x" + item.getAmount()));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage(Messages.formatErr("Failed to duplicate this item."));
        }

        return true;
    }

    private boolean isBlacklisted(ItemStack item) {
        if (item == null) return false;

        if (BLACKLIST.contains(item.getType())) return true;

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof BlockStateMeta) {
            BlockStateMeta bsm = (BlockStateMeta) meta;
            if (bsm.getBlockState() instanceof ShulkerBox) {
                ShulkerBox shulker = (ShulkerBox) bsm.getBlockState();
                for (ItemStack shulkerItem : shulker.getInventory().getContents()) {
                    if (shulkerItem != null && BLACKLIST.contains(shulkerItem.getType())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
