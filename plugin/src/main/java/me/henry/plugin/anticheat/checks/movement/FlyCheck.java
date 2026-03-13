package me.henry.plugin.anticheat.checks.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.henry.plugin.anticheat.checks.Check;

public class FlyCheck extends Check {

    public FlyCheck() {
        super("Fly");
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        if (player.isFlying() && !player.getAllowFlight()) {
            fail(player, "Flying in Survival/Adventure mode");
            player.setFlying(false);
            me.henry.plugin.classes.PlayerUtility.stopMovement(player);
        }
    }
}
