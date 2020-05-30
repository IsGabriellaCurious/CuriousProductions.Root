package me.cps.root.chat.commands;

import me.cps.root.util.Rank;
import me.cps.root.chat.ChatHub;
import me.cps.root.command.cpsCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Chat Hub - Global Prefix Command
 *
 * Removed: 2020-04-05
 * Reason: This was a test whim when doing prefixes
 *
 * @author     Gabriella Hotten
 * @since      2020-04-03
 * @deprecated Command removed.
 */
@Deprecated
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
        //getPlugin().setGlobalPrefix(rank); (had to comment this as it will nolonger compile, 2020-05-29)
        if (rank != Rank.DEFAULT)
            caller.sendMessage("" + ChatColor.GREEN + "Global Chat Prefix updated to " + rank.getPrefix());
        else
            caller.sendMessage("" + ChatColor.GREEN + "Global Chat Prefix updated to " + rank.getColor() + "Default");

    }
}
