package me.cps.root.scoreboard;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.cpsModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardCentre extends cpsModule {

    private static ScoreboardCentre instance;

    private HashMap<Player, cpsScoreboard> scoreboards = new HashMap<>();
    private HashMap<Player, Team> teams = new HashMap<>(); //not sure if this will be needed yet
    private HashMap<Player, String> prefixes = new HashMap<>();
    private HashMap<Player, String> suffixes = new HashMap<>();

    public ScoreboardCentre(JavaPlugin plugin) {
        super("Scoreboard Centre", plugin, "1.5", true);
        instance = this;
        registerSelf();
    }

    public HashMap<Player, cpsScoreboard> getScoreboards() {
        return scoreboards;
    }

    public HashMap<Player, Team> getTeams() {
        return teams;
    }

    public static ScoreboardCentre getInstance() {
        return instance;
    }

    public HashMap<Player, String> getPrefixes() {
        return prefixes;
    }

    public HashMap<Player, String> getSuffixes() {
        return suffixes;
    }

    @Deprecated
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        cpsScoreboard scoreboard = new cpsScoreboard(event.getPlayer());

        updatePrefixes();
        setPrefix(event.getPlayer(), null);

        updateSuffixes();

        if (event.getPlayer().getPlayerListName().length() > 23) {
            String name = event.getPlayer().getPlayerListName().substring(0, 23);
            event.getPlayer().setPlayerListName(name);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        scoreboards.remove(event.getPlayer());
        teams.remove(event.getPlayer());
        prefixes.remove(event.getPlayer());
    }

    public void resetCacheAll() {
        for (cpsScoreboard s : scoreboards.values()) {
            s.clearCacheOnNext();
        }
    }

    public void resetCache(Player player) {
        cpsScoreboard s = scoreboards.get(player);
        s.clearCacheOnNext();
    }

    public void updatePrefixes() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            setPrefix(player, getPrefixes().get(player));
        }
    }

    public void setPrefix(Player player, String prefix) {
        Rank rank =  Rank.getRank(player.getUniqueId());
        if (prefix == null)
            prefix = rank.getPrefix();

        for (cpsScoreboard s : getScoreboards().values()) {
            Scoreboard sc = s.getScoreboard();

            String teamName = rank.getLevel() + "APL" /*stands for  A PLAYER. e.g. npcs will be BNPC*/ + player.getEntityId();
            Team team = null;
            if (sc.getTeam(teamName) == null) {
                sc.registerNewTeam(teamName);
                team = sc.getTeam(teamName);
                team.addEntry(player.getName());
            } else
                team = sc.getTeam(teamName);

            team.setPrefix(prefix);

            if (getPrefixes().containsKey(player))
                getPrefixes().remove(player);

            getPrefixes().put(player, prefix);
            //team.addEntry(player.getName());
        }

    }

    public void updateSuffixes() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            setSuffix(player, getSuffixes().get(player));
        }
    }

    public void setSuffix(Player player, String suffix) {
        Rank rank =  Rank.getRank(player.getUniqueId());
        if (suffix == null)
            suffix = "";

        for (cpsScoreboard s : getScoreboards().values()) {
            Scoreboard sc = s.getScoreboard();

            String teamName = rank.getLevel() + "APL" /*stands for  A PLAYER. e.g. npcs will be BNPC*/ + player.getEntityId();
            Team team = null;
            if (sc.getTeam(teamName) == null) {
                sc.registerNewTeam(teamName);
                team = sc.getTeam(teamName);
                team.addEntry(player.getName());
            } else
                team = sc.getTeam(teamName);

            team.setSuffix(" " + suffix);

            if (getSuffixes().containsKey(player))
                getSuffixes().remove(player);

            getSuffixes().put(player, suffix);
            //team.addEntry(player.getName());
        }

    }



}
