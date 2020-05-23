package me.cps.root.util;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
