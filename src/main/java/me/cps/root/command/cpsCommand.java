package me.cps.root.command;

import me.cps.root.util.Rank;
import me.cps.root.util.cpsModule;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Curious Productions Root
 * Command Hub - cpsCommand (Abstract)
 *
 * This is what all CPS command extend and use to function.
 *
 * @author  Gabriella Hotten
 * @since   2020-04-03
 */
public abstract class cpsCommand<cpsPlugin extends cpsModule> {

    private String command; //the command /<this>
    private List<String> aliases; //any other versions of the name above
    protected cpsPlugin plugin; //the command's manager. e.g. /setrank's plugin would be Account Hub
    private Rank rank; //the rank required to run the command.

    public cpsCommand(String command, Rank rank, cpsPlugin mod, String...aliases) {
        this.plugin = mod;
        this.rank = rank;
        this.command = command;
        this.aliases = Arrays.asList(aliases);
    }

    public abstract void execute(Player player, String[] args); //this will be needed when extending this class. caller is the person who ran the cmd, args is their arguments put in.

    public String getCommand() {
        return command;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Rank getRankRequired() {
        return rank;
    }

    public cpsPlugin getPlugin() {
        return plugin;
    }
}
