package me.henry.plugin.utils;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class EconomyManager {

    private final File file;
    private final FileConfiguration config;
    private final HashMap<UUID, Double> balances = new HashMap<>();

    public EconomyManager(File dataFolder) {
        this.file = new File(dataFolder, "balances.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        loadBalances();
    }

    private void loadBalances() {
        for (String key : config.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            double balance = config.getDouble(key);
            balances.put(uuid, balance);
        }
    }

    public void saveBalances() {
        for (UUID uuid : balances.keySet()) {
            config.set(uuid.toString(), balances.get(uuid));
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0.0);
    }

    public void deposit(Player player, double amount) {
        balances.put(player.getUniqueId(), getBalance(player) + amount);
    }

    public boolean withdraw(Player player, double amount) {
        double current = getBalance(player);
        if (current >= amount) {
            balances.put(player.getUniqueId(), current - amount);
            return true;
        }
        return false;
    }
}
