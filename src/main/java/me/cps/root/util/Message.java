package me.cps.root.util;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Message {

    public static void console(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

    public static void broadcast(String message) {
        Bukkit.getServer().broadcastMessage(message);
    }

    public static void staff(String message) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Rank.hasRank(p.getUniqueId(), Rank.HELPER)) {
                p.sendMessage(message);
            }
        }
    }

}
