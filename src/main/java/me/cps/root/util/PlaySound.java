package me.cps.root.util;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

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
