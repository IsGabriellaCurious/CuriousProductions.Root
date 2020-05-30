package me.cps.root.staff.commands;

import me.cps.root.util.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.staff.StaffHub;
import me.cps.root.staff.StaffOptions;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Staff Hub - Toggle Chat Chat Command
 *
 * Handles the toggling of game chat (used inside of CPS Game Manager based games)
 *
 * @author  Gabriella Hotten
 * @since   2020-04-11
 */
public class ToggleGameChatCommand extends cpsCommand<StaffHub> {

    public ToggleGameChatCommand(StaffHub mod) {
        super("togglegamechat", Rank.HELPER, mod, "tgc", "gamechat");
    }

    @Override
    public void execute(Player caller, String[] args) {
        if (!getPlugin().getInStaffMode().contains(caller)) {
            caller.sendMessage(StaffHub.prefix + "§cYou must be in staff mode to do this!");
            return;
        }

        if (StaffHub.getInstance().getOption(StaffOptions.GameChat, caller))
            StaffHub.getInstance().toggleGameChat(false, false, caller);
        else
            StaffHub.getInstance().toggleGameChat(true, false, caller);
    }
}
