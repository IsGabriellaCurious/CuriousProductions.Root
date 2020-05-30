package me.cps.root.staff.commands;

import me.cps.root.util.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.staff.StaffHub;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Staff Hub - Staff Mode Command
 *
 * Handles the toggling of staff mode.
 *
 * @author  Gabriella Hotten
 * @since   2020-04-11
 */
public class StaffModeCommand extends cpsCommand<StaffHub> {

    public StaffModeCommand(StaffHub mod) {
        super("staffmode", Rank.HELPER, mod, "togglestaff");
    }

    @Override
    @Deprecated
    public void execute(Player caller, String[] args) {
        getPlugin().staffMode(caller);
    }
}
