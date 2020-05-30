package me.cps.root.util;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Curious Productions Root
 * CPS Utilities - Player Velocity
 *
 * Handles the custom velocity of a player.
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-04-10
 */
public class PlayerVelocity {

    public static void velocityFrom(Player player, Player from, int multiplier) {
        Vector vector = from.getLocation().getDirection();
        vector = vector.multiply(5);
        player.setVelocity(vector);
    }

}
