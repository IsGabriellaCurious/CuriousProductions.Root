package me.cps.root.scoreboard;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.cpsModule;
import me.cps.root.util.PerMilliEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class ScoreboardCentre extends cpsModule {

    private static ScoreboardCentre instance;

    private HashMap<Player, cpsScoreboard> scoreboards = new HashMap<>();

    public ScoreboardCentre(JavaPlugin plugin) {
        super("Scoreboard Centre", plugin, "1.0-alpha", true);
        instance = this;
        registerSelf();
    }

    public HashMap<Player, cpsScoreboard> getScoreboards() {
        return scoreboards;
    }

    public static ScoreboardCentre getInstance() {
        return instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        cpsScoreboard scoreboard = new cpsScoreboard(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        scoreboards.remove(event.getPlayer());
    }

    /*@EventHandler
    public void onMili(PerMilliEvent evnet) {
        for (cpsScoreboard s : scoreboards.values()) {
            s.apply();
        }
    }*/
}
