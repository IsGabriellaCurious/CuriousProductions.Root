package me.cps.root.chat.commands;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.chat.ChatHub;
import me.cps.root.command.cpsCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/*
COMMAND NO LONGER USED. (disabled)

* REMOVED 05/04/20
* REASON: this was a test whim when doing prefixes.
 */

public class GlobalPrefixCommand extends cpsCommand<ChatHub> {

    public GlobalPrefixCommand(ChatHub mod) {
        super("globalprefix", Rank.DEVELOPER, mod);
    }

    @Override
    public void execute(Player caller, String[] args) {
        if (args == null) {
            caller.sendMessage("" + ChatColor.RED + "Invalid usage! Please try /" + getCommand() + " <rank>");
            return;
        }

        Rank temp = null;

        try {
            temp = Rank.valueOf(args[0]);
        } catch (Exception e) {
            caller.sendMessage("" + ChatColor.RED + "Invalid rank! Please try again.");
            return;
        }

        final Rank rank = temp;
        getPlugin().setGlobalPrefix(rank);
        if (rank != Rank.DEFAULT)
            caller.sendMessage("" + ChatColor.GREEN + "Global Chat Prefix updated to " + rank.getPrefix());
        else
            caller.sendMessage("" + ChatColor.GREEN + "Global Chat Prefix updated to " + rank.getColor() + "Default");

    }
}
