package me.cps.root.disguise.commands;

import me.cps.root.util.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.disguise.DisguiseManager;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Disguise Manager - UnDisguise Command
 *
 * Command for players to undisguise themselves.
 *
 * @author  Gabriella Hotten
 * @version 1.0-alpha
 * @since   2020-05-28
 */
public class UnDisguiseCommand extends cpsCommand<DisguiseManager> {

    public UnDisguiseCommand(DisguiseManager mod) {
        super("undisguise", Rank.VIP, mod, "unnick");
    }

    @Override
    public void execute(Player caller, String[] args) {
        if (!getPlugin().getDisguised().containsKey(caller.getName())) {
            caller.sendMessage("§cYou are not disguised!");
            return;
        }

        getPlugin().unDisguise(caller);
        caller.sendMessage("§cYou have been undisguised.");
    }
}
