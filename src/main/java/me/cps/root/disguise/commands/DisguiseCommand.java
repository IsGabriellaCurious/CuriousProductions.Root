package me.cps.root.disguise.commands;

import me.cps.root.util.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.disguise.DisguiseManager;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Disguise Manager - Disguise Command
 *
 * Command used for a player to disguise themselves.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-28
 */
public class DisguiseCommand extends cpsCommand<DisguiseManager> {

    public DisguiseCommand(DisguiseManager mod) {
        super("disguise", Rank.OWNER, mod, "nick");
    }

    @Override
    public void execute(Player caller, String[] args) {
        if (args == null || args.length != 2) {
            caller.sendMessage("§cError! Correct usage: /disguise <name> <rank>");
            return;
        }

        //TODO rank validation check!
        getPlugin().disguisePlayer(caller, args[0], null, Rank.valueOf(args[1]));
        caller.sendMessage("§aYou have been disguised as " + Rank.valueOf(args[1]).getPrefix() + args[0]);
    }
}
