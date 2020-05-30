package me.cps.root.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * CPS Utilities - Player Utilities
 *
 * Handles everything player based.
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-04-30
 */
public class PlayerUtil {

    public static Player search(String input) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            String name = p.getName().toLowerCase();

            if (!name.startsWith(input.toLowerCase()))
                continue;

            return p;
        }
        return null;
    }

}
