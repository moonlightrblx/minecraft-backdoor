package me.henry.plugin.anticheat.checks.movement;

import me.henry.plugin.anticheat.checks.Check;
import me.henry.plugin.classes.PlayerUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class SpeedCheck extends Check {

    public SpeedCheck() {
        super("Speed");
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        double xDiff = event.getTo().getX() - event.getFrom().getX();
        double zDiff = event.getTo().getZ() - event.getFrom().getZ();
        double horizontalSpeed = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

        if (horizontalSpeed > 1.2 && !player.isInsideVehicle()) {
            fail(player, "Using speedhack (speed=" + String.format("%.2f", horizontalSpeed) + ")");
            PlayerUtility.stopMovement(player);
        }
    }
}
