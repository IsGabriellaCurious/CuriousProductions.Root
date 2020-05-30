package me.cps.root.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * CPS Utilities - Message
 *
 * Handles most message types (such as broadcasts, console messages, string[] combines, etc)
 *
 * @author  Gabriella Hotten
 * @version 1.2
 * @since   2020-04-07
 */
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

    public static String combine(String[] args, int start) {
        String msg = "";
        for (int i = start; i<args.length; i++) {
            msg = msg + args[i] + " ";
        }
        return msg;
    }


}
