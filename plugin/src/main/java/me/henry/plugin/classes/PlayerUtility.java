package me.henry.plugin.classes;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public final class PlayerUtility {

    private PlayerUtility() {
        // Prevent instantiation
    }


    public static boolean isFalling(Entity entity) {
        if (entity == null) {
            return false;
        }
        return !entity.isOnGround() && entity.getVelocity().getY() < 0;
    }


    public static boolean isJumping(Entity entity) {
        if (entity == null) {
            return false;
        }
        return !entity.isOnGround() && entity.getVelocity().getY() > 0;
    }


    public static boolean isSprinting(Player player) {
        if (player == null) {
            return false;
        }
        return player.isSprinting();
    }


    public static double getHorizontalSpeed(Entity entity) {
        if (entity == null) {
            return 0.0;
        }
        Vector velocity = entity.getVelocity();
        return Math.sqrt(velocity.getX() * velocity.getX() + velocity.getZ() * velocity.getZ());
    }
    public static double getVerticalSpeed(Entity entity) {
        if (entity == null) {
            return 0.0;
        }
        return Math.abs(entity.getVelocity().getY());
    }

    public static void giveKnockback(Entity entity, Vector knockback) {
        if (entity != null && knockback != null) {
            entity.setVelocity(knockback);
        }
    }

    public static double getDistance(Entity entity1, Entity entity2) {
        if (entity1 == null || entity2 == null) {
            return -1;
        }
        return entity1.getLocation().distance(entity2.getLocation()); // returns in blocks?
    }


    public static boolean safeTeleport(Player player, Location location) {
        if (player == null || location == null) {
            return false;
        }
        return player.teleport(location);
    }

  
    public static void stopMovement(Entity entity) {
        if (entity != null) {
            entity.setVelocity(new Vector(0, 0, 0));
        }
    }

   
    public static Boolean isInVehicle(Player player) {
        if (player == null) {
            return false;
        }
        
        
        return player.isInsideVehicle();
    }

    public static Boolean isInBoat(Player player) {
        if (player == null) {
            return false;
        }
        
        
        return player.isInsideVehicle() && (player.getVehicle().getType() == org.bukkit.entity.EntityType.BOAT || player.getVehicle().getType() == org.bukkit.entity.EntityType.CHEST_BOAT);
    }
    
}
