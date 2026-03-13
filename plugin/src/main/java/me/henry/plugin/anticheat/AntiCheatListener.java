package me.henry.plugin.anticheat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class AntiCheatListener implements Listener {

    private final CheckManager checkManager;

    public AntiCheatListener(me.henry.plugin.Plugin plugin) {
        this.checkManager = new CheckManager(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        checkManager.handleMove(event);
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent event) {
        checkManager.handleVelocity(event);
    }
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event){
        checkManager.handleHit(event);
    }
}
