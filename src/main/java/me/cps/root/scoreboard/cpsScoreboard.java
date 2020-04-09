package me.cps.root.scoreboard;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class cpsScoreboard {

    private Player player;

    private Scoreboard scoreboard;
    private Objective objective;

    private ArrayList<String> scores = new ArrayList<>();

    public String title = "§b§lCPS";
    //these 2 should probably never used as colours n stuff should be in the title string with §
    public ChatColor titleColour = ChatColor.AQUA;
    public boolean boldTitle = true;

    public cpsScoreboard(Player player) {
        this.player = player;
        //this.title = title;

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("SCOREBOARD" + player.getEntityId(), "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(title);

        ScoreboardCentre.getInstance().getScoreboards().put(player, this);
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public void add(String string) {
        scores.add(string);
    }

    public void addEmpty() {
        add(" ");
    }

    public void clear() {
        scores.clear();
    }

    //probably needs to be remade due to scoreboard flicker that occurs pretty commonly

    /*
    TODO
    - When wanting to update a score, it just creates a new one, instead of changing. (maybe fix by creating Score objects and storing them somewhere)
    - that fucking flicker reeeee
     */
    public void apply() {
        objective.setDisplayName(title);
        for (int i=0; i<scores.size(); i++) {
            if (i == 16)
                return;
            objective.getScore(scores.get(i)).setScore(scores.size()-i);
        }
        player.setScoreboard(scoreboard);
    }


}
