package me.henry.plugin.anticheat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import me.henry.plugin.anticheat.checks.Check;
import me.henry.plugin.anticheat.checks.combat.AntiKnockbackCheck;
import me.henry.plugin.anticheat.checks.combat.ReachCheck;
import me.henry.plugin.anticheat.checks.movement.BoatFlyCheck;
import me.henry.plugin.anticheat.checks.movement.FlyCheck;
import me.henry.plugin.anticheat.checks.movement.HighJumpCheck;
import me.henry.plugin.anticheat.checks.movement.SpeedCheck;

public class CheckManager {

    private final List<Check> checks = new ArrayList<>();

    public CheckManager(me.henry.plugin.Plugin plugin) {
        checks.add(new FlyCheck());
        checks.add(new HighJumpCheck());
        checks.add(new SpeedCheck());
        checks.add(new ReachCheck(plugin));
        checks.add(new BoatFlyCheck());
        checks.add(new AntiKnockbackCheck(plugin));
    }

    public void handleMove(PlayerMoveEvent event) {
        for (Check check : checks) {
            check.onMove(event);
        }
    }

    public void handleVelocity(PlayerVelocityEvent event) {
        for (Check check : checks) {
            check.onVelocity(event);
        }
    }

    public void handleHit(EntityDamageByEntityEvent event){
        for (Check check: checks){
            check.onHit(event);
        }
    }
}
