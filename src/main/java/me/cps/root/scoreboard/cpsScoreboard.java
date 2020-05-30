package me.cps.root.scoreboard;

import me.cps.root.networkdata.NetworkDataHub;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

/**
 * Curious Productions Root
 * Scoreboard Centre - cpsScoreboard
 *
 * Main class that holds every player's scoreboard information
 * TODO: rewrite this entire system because it's kina confusing and I really don't like it
 *
 * @author  Gabriella Hotten
 * @since   2020-04-09
 */
public class cpsScoreboard {

    private Player player;

    private Scoreboard scoreboard;
    private Objective objective;

    private ArrayList<String> scores = new ArrayList<>();
    private ArrayList<String> beforeNew = new ArrayList<>(); //a cache, bascially.
    private ArrayList<String> toChange = new ArrayList<>(); //line to change
    private boolean clearCache = false;

    public String title = NetworkDataHub.getNetworkDataBase().getNetworkPrimaryColour() + NetworkDataHub.getNetworkDataBase().getNetworkName(); //default title
    //these 2 should probably never used as colours n stuff should be in the title string with §
    public ChatColor primaryColour = NetworkDataHub.getNetworkDataBase().getNetworkPrimaryColour();
    public ChatColor secondaryColour = NetworkDataHub.getNetworkDataBase().getNetworkSecondaryColour();
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

    public void clearCacheOnNext() {
        clearCache = true;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void add(String string) {
        scores.add(string);
    }

    public void addEmpty() {
        scores.add(" ");
    }

    public void clear() {
        scores.clear();
    }

    public void resetScore(String line) {
        scoreboard.resetScores(line);
    }

    public void apply() {
        if (clearCache) {
            for (String s : beforeNew)
                scoreboard.resetScores(s);
            beforeNew.clear();
            toChange.clear();
            clearCache = false;
        }

        objective.setDisplayName(title);

        //this makes spacing possible
        int currentSpace = 1;

        for (int i=0; i<scores.size(); i++) {
            if (scores.get(i).equalsIgnoreCase(" ")) {
                String old = scores.get(i);
                String newS = scores.get(i);
                for (int ii=0; ii<currentSpace; ii++) {
                    newS = newS + " ";
                }
                currentSpace++;
                scores.set(i, newS);
            }
        }

        if (!beforeNew.isEmpty()) {
            for (int i=0; i<scores.size(); i++) {
                try {
                    String cached = beforeNew.get(i);
                    String toBe = scores.get(i);
                    if (!(cached.equalsIgnoreCase(toBe))) {
                        toChange.add(String.valueOf(i));
                    }
                } catch (Exception e) {
                    //do nothing as this is just incase there is an out of bounds exception.
                }
            }
        } else {
            for (int i=0; i<scores.size(); i++) {
                objective.getScore(scores.get(i)).setScore(scores.size()-i);
            }
        }

        for (int i=0; i<scores.size(); i++) {

            if (toChange.contains(String.valueOf(i))) {
                resetScore(beforeNew.get(i));
                objective.getScore(scores.get(i)).setScore(scores.size()-i);
            } else {
                try {
                    if (!(beforeNew.get(i).equalsIgnoreCase(scores.get(i))))
                        objective.getScore(scores.get(i)).setScore(scores.size()-i);
                } catch (Exception e) { //again, in case of out of bounds exceitpion
                    objective.getScore(scores.get(i)).setScore(scores.size()-i);
                }
            }

        }

        beforeNew.clear();
        toChange.clear();

        for (String s : scores) {
            beforeNew.add(s);
        }

        player.setScoreboard(scoreboard);
    }


}
