package me.cps.root.util;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Message {

    public static void console(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(message);
    }

    public static void broadcast(String message) {
        Bukkit.getServer().broadcastMessage(message);
    }

}
