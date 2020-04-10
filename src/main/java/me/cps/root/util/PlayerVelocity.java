package me.cps.root.util;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PlayerVelocity {

    public static void velocityFrom(Player player, Player from, int multiplier) {
        Vector vector = from.getLocation().getDirection();
        vector = vector.multiply(5);
        player.setVelocity(vector);
    }

}
