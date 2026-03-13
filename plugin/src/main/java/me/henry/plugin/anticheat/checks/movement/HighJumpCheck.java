package me.henry.plugin.anticheat.checks.movement;

import me.henry.plugin.anticheat.checks.Check;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class HighJumpCheck extends Check {

    public HighJumpCheck() {
        super("HighJump");
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isFlying() || player.getAllowFlight()) return;

        double yDiff = event.getTo().getY() - event.getFrom().getY();
        if (yDiff > 0.6 && !player.isOnGround()) {
            fail(player, "Possible Fly/High-Jump detected (yDiff=" + yDiff + ")");
        }
    }
}
