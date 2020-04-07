package me.cps.root.account.commands;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.account.AccountHub;
import me.cps.root.command.cpsCommand;
import me.cps.root.cpsModule;
import org.bukkit.entity.Player;

public class SetRankCommand extends cpsCommand<AccountHub> {

    public SetRankCommand(AccountHub mod) {
        super("setrank", Rank.JUNIORADMIN, mod);
    }

    @Override
    public void execute(Player caller, String[] args) {
        if (args == null || args.length < 2) { //checks to see if there are less than 2 arguments
            caller.sendMessage("§cInvalid usage! Please try /" + getCommand() + " <player> <rank>");
            return;
        }

        Player updated = getPlugin().getPlugin().getServer().getPlayer(args[0]); //checks to see if the player is online (will be changed soon, see below)

        if (updated == null) {
            caller.sendMessage("§cError! That player is not online."); //TODO make it so you can update offline players
            caller.sendMessage("§7§oThis feature will be updated soon. Hang tight!");
            return;
        }

        Rank rank = null;
        try {
            rank = Rank.valueOf(args[1]); //checks to see if the rank is valid.
        } catch (Exception e) {
            caller.sendMessage("§cError! Please enter a valid rank.");
            return;
        }

        if (!Rank.hasRank(caller.getUniqueId(), rank)) { //TODO put a warning in for making your own rank lower. (as you cant set it back)
            caller.sendMessage("§cError! You cannot set a rank higher than your own.");
            return;
        }

        caller.sendMessage("§aUpdating rank! Un momento...");
        getPlugin().updateRank(updated.getUniqueId(), rank); //updates the rank!
        getPlugin().getPlayers().remove(updated.getUniqueId());
        getPlugin().getPlayers().put(updated.getUniqueId(), rank); //updates the rank cache
        caller.sendMessage("§aSuccess! " + updated.getName() + "'s rank has been updated to " + rank.getColor() + rank.getName());
    }
}
