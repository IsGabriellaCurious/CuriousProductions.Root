package me.cps.root.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Curious Productions Root
 * CPS Utilities - Per Milli Runnable
 *
 * Runnable that fires an event every 1 tick.
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-04-07
 */
public class PerMilliRunnable implements Runnable {

    private JavaPlugin plugin;

    public PerMilliRunnable(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        PerMilliEvent event = new PerMilliEvent();
        Bukkit.getPluginManager().callEvent(event);
    }
}
