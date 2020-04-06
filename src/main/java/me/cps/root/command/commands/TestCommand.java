package me.cps.root.command.commands;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.cpsModule;
import org.bukkit.entity.Player;

/*
COMMAND NO LONGER USED. (disabled)

* REMOVED 04/04/20
* REASON: this was the first test command, also throws a nasty error when no args are used.
 */

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
