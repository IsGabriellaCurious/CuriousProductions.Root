package me.cps.root.util;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * CPS Utilities - Play Sound
 *
 * Handles the sending of sounds to players.
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-04-08
 */
public class PlaySound {

    public static void all(Sound sound, float vol, float pitch) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, vol, pitch);
        }
    }

    public static void play(Player player, Sound sound, float vol, float pitch) {
        player.playSound(player.getLocation(), sound, vol, pitch);
    }

}
