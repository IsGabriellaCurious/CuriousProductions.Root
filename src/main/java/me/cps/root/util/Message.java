package me.cps.root.util;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Root;

public class Message {

    public static void console(String message) {
        Root.getInstance().getServer().getConsoleSender().sendMessage(message);
    }

}
