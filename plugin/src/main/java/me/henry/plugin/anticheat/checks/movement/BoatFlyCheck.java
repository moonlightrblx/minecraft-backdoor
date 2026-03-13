package me.henry.plugin.anticheat.checks.movement;

import me.henry.plugin.anticheat.checks.Check;
import me.henry.plugin.classes.PlayerUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class BoatFlyCheck extends Check {

    public BoatFlyCheck() {
        super("BoatFly");
    }

    @Override
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        double yDiff = event.getTo().getY() - event.getFrom().getY();
        double verticalSpeed = Math.abs(yDiff);

        if (verticalSpeed > 1) {
            if (PlayerUtility.isInVehicle(player) && PlayerUtility.isInBoat(player)) {
                fail(player, "Using boat fly");
                PlayerUtility.stopMovement(player);
            }
        }
    }
}
