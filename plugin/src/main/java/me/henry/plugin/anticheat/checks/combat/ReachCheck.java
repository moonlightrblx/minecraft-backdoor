package me.henry.plugin.anticheat.checks.combat;

import me.henry.plugin.anticheat.checks.Check;
import me.henry.plugin.classes.PlayerUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class ReachCheck extends Check {

    private final me.henry.plugin.Plugin plugin;

    public ReachCheck(me.henry.plugin.Plugin plugin) {
        super("ReachCheck");
        this.plugin = plugin;
    }

    @Override
    public void onHit(EntityDamageByEntityEvent event) {
        Entity ent = event.getDamager();
        Entity target = event.getEntity();

        if (ent.getType() == EntityType.PLAYER){
      
            if (PlayerUtility.getDistance(ent, target) > 3){
                event.setDamage(0); // cancel all damage because we are too bad to do damage :3
                fail((Player)ent, "Reach flag ");
            }
        }
        

        
    }
}
