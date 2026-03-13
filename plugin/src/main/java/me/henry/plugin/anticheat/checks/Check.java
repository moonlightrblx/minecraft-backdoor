package me.henry.plugin.anticheat.checks;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public abstract class Check {

    private final String name;

    public Check(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // only in checks that care about movement
    public void onMove(PlayerMoveEvent event) {}

    // only in checks that care about velocity
    public void onVelocity(PlayerVelocityEvent event) {}

    // only in checks that care about entity hits

    public void onHit(EntityDamageByEntityEvent event) {}
    
    protected void fail(Player player, String reason) {
        me.henry.plugin.utils.Messages.sendAlert(player, "[" + name + "] " + reason);
    }
}
