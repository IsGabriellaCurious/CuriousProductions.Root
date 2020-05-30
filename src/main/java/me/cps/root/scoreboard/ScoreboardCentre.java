package me.cps.root.scoreboard;

import me.cps.root.util.Rank;
import me.cps.root.util.cpsModule;
import me.cps.root.disguise.DisguiseManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

/**
 * Curious Productions Root
 * Scoreboard Centre
 *
 * Handles all the prefixes, suffixes and scoreboards for CPS!
 * TODO: rewrite this entire system because it's kina confusing and I really don't like it
 *
 * @author  Gabriella Hotten
 * @version 1.5
 * @since   2020-04-09
 */
public class ScoreboardCentre extends cpsModule {

    public enum UpdateAction {
        CREATE, UPDATE, BEGONETHOT
    }

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

        updatePrefixes(UpdateAction.CREATE);
        setPrefix(event.getPlayer(), null, UpdateAction.CREATE);

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

    public void updatePrefixes(UpdateAction action) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            setPrefix(player, getPrefixes().get(player), action);
        }
    }

    public void setPrefix(Player player, String prefix, UpdateAction action) {
        boolean disguise = DisguiseManager.getInstance().getDisguised().containsKey(player.getName());
        Rank rank =  Rank.getRank(player.getUniqueId());

        if (disguise)
            rank = DisguiseManager.getInstance().getDisguisedRank().get(player.getName());

        System.out.println(disguise);

        if (prefix == null)
            prefix = rank.getPrefix();

        for (cpsScoreboard s : getScoreboards().values()) {
            Scoreboard sc = s.getScoreboard();

            String teamName = rank.getLevel() + "APL" /*stands for  A PLAYER. e.g. npcs will be BNPC*/ + player.getEntityId();
            if (sc.getTeam(teamName) == null) {
                sc.registerNewTeam(teamName);
            }
            Team team = sc.getTeam(teamName);
            team.setPrefix(prefix);
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);

            switch (action) {
                case CREATE:
                    team.addEntry(player.getName());
                    if (disguise)
                        team.addEntry(DisguiseManager.getInstance().getDisguised().get(player.getName()));
                    break;
                case UPDATE:
                    team.unregister();
                    sc.registerNewTeam(teamName);
                    team = sc.getTeam(teamName);
                    team.setPrefix(prefix);
                    team.setNameTagVisibility(NameTagVisibility.ALWAYS);
                    team.addEntry(player.getName());
                    if (disguise)
                        team.addEntry(DisguiseManager.getInstance().getDisguised().get(player.getName()));
                    break;
                case BEGONETHOT:
                    team.unregister();
                    break;
            }

            if (getPrefixes().containsKey(player))
                getPrefixes().remove(player);

            getPrefixes().put(player, prefix);
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
