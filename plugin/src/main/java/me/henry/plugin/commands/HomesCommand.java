package me.henry.plugin.commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.destroystokyo.paper.ParticleBuilder;

import me.henry.plugin.utils.Messages;

public class HomesCommand implements CommandExecutor, Listener {

    private final Map<UUID, Map<Integer, HomeData>> homes = new HashMap<>();
    private final Map<UUID, HomeTeleportTask> teleportTasks = new HashMap<>();
    private final Map<UUID, Integer> waitingForNameSlot = new HashMap<>();

    private final org.bukkit.plugin.java.JavaPlugin plugin;
    private final File homesFile;
    private final FileConfiguration homesConfig;

    public HomesCommand(org.bukkit.plugin.java.JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Create homes.yml if not exist
        homesFile = new File(plugin.getDataFolder(), "homes.yml");
        if (!homesFile.exists()) {
            homesFile.getParentFile().mkdirs();
            try {
                homesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);
        loadHomes();
    }

    public Map<UUID, HomeTeleportTask> getTeleportTasks() {
        return teleportTasks;
    }

    public static class HomeData {
        String name;
        Location location;

        public HomeData(String name, Location location) {
            this.name = name;
            this.location = location;
        }
    }

    private void loadHomes() {
        for (String uuidKey : homesConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidKey);
            Map<Integer, HomeData> playerHomes = new HashMap<>();
            if (homesConfig.getConfigurationSection(uuidKey) == null) continue;
            for (String slotKey : homesConfig.getConfigurationSection(uuidKey).getKeys(false)) {
                int slot = Integer.parseInt(slotKey);
                String name = homesConfig.getString(uuidKey + "." + slotKey + ".name");
                Location loc = homesConfig.getLocation(uuidKey + "." + slotKey + ".location");
                if (name != null && loc != null) {
                    playerHomes.put(slot, new HomeData(name, loc));
                }
            }
            homes.put(uuid, playerHomes);
        }
    }

    private void saveHomes() {
        for (String key : homesConfig.getKeys(false)) {
            homesConfig.set(key, null);
        }

        for (UUID uuid : homes.keySet()) {
            for (Map.Entry<Integer, HomeData> entry : homes.get(uuid).entrySet()) {
                String path = uuid.toString() + "." + entry.getKey();
                homesConfig.set(path + ".name", entry.getValue().name);
                homesConfig.set(path + ".location", entry.getValue().location);
            }
        }

        try {
            homesConfig.save(homesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (sender != null){
                sender.sendMessage(Messages.formatErr("Only players can use this command!"));
            }
            return true;
        }
        openHomeGUI((Player) sender);
        return true;
    }

    private void openHomeGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "§1Your Homes"); // keep GUI title raw
        Map<Integer, HomeData> playerHomes = homes.getOrDefault(player.getUniqueId(), new HashMap<>());

        for (int i = 0; i < 5; i++) {
            ItemStack item;
            if (playerHomes.containsKey(i)) {
                HomeData hd = playerHomes.get(i);
                item = new ItemStack(Material.RED_BED);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(hd.name); // raw name
                meta.setLore(java.util.Arrays.asList("Left click: Teleport", "Right click: Delete")); // raw lore
                item.setItemMeta(meta);
            } else {
                item = new ItemStack(Material.BEDROCK);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("Empty Slot " + (i + 1)); // raw
                meta.setLore(java.util.Arrays.asList("Left click: Set home here")); // raw
                item.setItemMeta(meta);
            }
            inv.setItem(i, item);
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player player = (Player) e.getWhoClicked();
        if (!e.getView().getTitle().equals("§1Your Homes")) return; // raw GUI check
        e.setCancelled(true);

        int slot = e.getSlot();
        Map<Integer, HomeData> playerHomes = homes.getOrDefault(player.getUniqueId(), new HashMap<>());

        if (playerHomes.containsKey(slot)) {
            if (e.isLeftClick()) {
                HomeData hd = playerHomes.get(slot);
                player.closeInventory();
                player.sendMessage(Messages.format("Teleporting to " + hd.name + " in 5 seconds. Don't move!"));
                HomeTeleportTask task = new HomeTeleportTask(player, hd.location);
                player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation().add(0, 0, 1), 1, 0 , 0 , 0.6); 
                task.runTaskTimer(plugin, 0, 1);
                
                teleportTasks.put(player.getUniqueId(), task);
            } else if (e.isRightClick()) {
                homes.get(player.getUniqueId()).remove(slot);
                saveHomes();
                player.sendMessage(Messages.formatErr("Home deleted!"));
                openHomeGUI(player);
            }
        } else {
            player.closeInventory();
            player.sendMessage(Messages.format("Type the name for your new home in chat:"));
            waitingForNameSlot.put(player.getUniqueId(), slot);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (!waitingForNameSlot.containsKey(player.getUniqueId())) return;

        e.setCancelled(true);
        String name = e.getMessage();
        int slot = waitingForNameSlot.get(player.getUniqueId());
        homes.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        homes.get(player.getUniqueId()).put(slot, new HomeData(name, player.getLocation()));
        waitingForNameSlot.remove(player.getUniqueId());

        saveHomes();

        player.sendMessage(Messages.format("Home '" + name + "' set!"));
        Bukkit.getScheduler().runTask(plugin, () -> openHomeGUI(player));
    }

    public class HomeTeleportTask extends BukkitRunnable {
        private final Player player;
        private final Location target;
        private final Location start;
        private int timer = 100; // 100 ticks is 5 seconds cause 100 // 5 == 20

        public HomeTeleportTask(Player player, Location target) {
            this.player = player;
            this.target = target;
            this.start = player.getLocation();
        }
        @Override
        public void run() {
             // subtract every tick
            
            // the "run" func runs every tick so we spawn one every tick
            
            Location current = player.getLocation();
            if (!player.isOnline()
                    || current.getX() != start.getX()
                    || current.getY() != start.getY()
                    || current.getZ() != start.getZ()) {
                player.sendMessage(Messages.formatErr("Teleport cancelled!"));
                cancel();
                return;
            }
            
            timer--;

            if (timer <= 0) {
                
                player.teleport(target);

                player.sendMessage(Messages.format("Teleported!"));
                cancel();
            }
        }
    }
}
