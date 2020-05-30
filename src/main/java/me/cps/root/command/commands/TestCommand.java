package me.cps.root.command.commands;

import me.cps.root.util.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.util.cpsModule;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Command Hub - Test Command
 *
 * Removed: 2020-04-04
 * Reason: First ever command, and it throw an error when no args.
 *
 * @author     Gabriella Hotten
 * @since      2020-04-03
 * @deprecated Command removed
 */
@Deprecated
public class TestCommand extends cpsCommand {

    public TestCommand(cpsModule mod) {
        super("testcmd", Rank.DEVELOPER, mod, "testcommand", "testingcommand");
    }

    @Override
    public void execute(Player caller, String[] args) {
        caller.sendMessage("This worked, lmao!");
        if (args[0].equalsIgnoreCase("super")) {
            caller.sendMessage("AND YOU USED SUPER POWERS!!!");
        }
    }
}
