package me.henry.plugin.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public final class RankManager {
    private final File file;
    private final FileConfiguration config;
    private final Map<UUID, Rank> ranks = new HashMap<>();

    public RankManager(File dataFolder) {
        this.file = new File(dataFolder, "ranks.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        loadRanks();
    }

    public void loadRanks() {
        ranks.clear();
        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                Rank rank = Rank.valueOf(config.getString(key));
                ranks.put(uuid, rank);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    public void saveRanks() {
        for (Map.Entry<UUID, Rank> entry : ranks.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue().name());
        }
        try {
            config.save(file);
        } catch (IOException e) {
        }
    }

    public void setRank(Player player, Rank rank) {
        ranks.put(player.getUniqueId(), rank);
        saveRanks();
        updateTablist(player);
    }

    public Rank getRank(Player player) {
        return ranks.getOrDefault(player.getUniqueId(), Rank.MEMBER);
    }

    @SuppressWarnings("deprecation")
    public void updateTablist(Player player) {
        String header = ChatColor.DARK_PURPLE + "=== ADVANTAGE SMP ===";
        String footer = ChatColor.YELLOW + "=== Created by Drexxy ===";

        player.setPlayerListHeaderFooter(header, footer);
        player.setPlayerListName(getRank(player).getPrefix() + ChatColor.WHITE + player.getName());
    }
}
