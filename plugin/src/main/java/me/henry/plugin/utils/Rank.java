package me.henry.plugin.utils;

import org.bukkit.ChatColor;

public enum Rank {
    OWNER(ChatColor.BLACK + "[Owner] ", 3),
    ADMIN(ChatColor.DARK_RED + "[Admin] ", 2),
    MOD(ChatColor.BLUE + "[Mod] ", 1),
    MEMBER(ChatColor.GRAY + "[Member] ", 0);

    private final String prefix;
    private final int level;

    Rank(String prefix, int level) {
        this.prefix = prefix;
        this.level = level;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getLevel() {
        return level;
    }

    public boolean isAtLeast(Rank other) {
        return this.level >= other.level;
    }
}
