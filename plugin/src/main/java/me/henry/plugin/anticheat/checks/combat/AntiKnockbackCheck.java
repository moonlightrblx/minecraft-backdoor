package me.henry.plugin.anticheat.checks.combat;

import me.henry.plugin.anticheat.checks.Check;
import me.henry.plugin.classes.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

public class AntiKnockbackCheck extends Check {

    private final me.henry.plugin.Plugin plugin;

    public AntiKnockbackCheck(me.henry.plugin.Plugin plugin) {
        super("AntiKnockback");
        this.plugin = plugin;
    }

    @Override
    public void onVelocity(PlayerVelocityEvent event) {
        Player player = event.getPlayer();
        double knockback = event.getVelocity().length();

        if (knockback > 0.2 && player.isOnGround()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (player.getVelocity().length() < 0.05) {
                    fail(player, "Possible Anti-Knockback detected");
                    Vector kb = event.getVelocity().clone();
                    PlayerUtility.giveKnockback(player, kb);
                }
            }, 5L);
        }
    }
}
