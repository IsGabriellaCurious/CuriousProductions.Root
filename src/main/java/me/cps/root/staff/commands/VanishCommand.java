package me.cps.root.staff.commands;

import me.cps.root.util.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.staff.StaffHub;
import me.cps.root.staff.StaffOptions;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Staff Hub - Vanish Command
 *
 * Handles the toggling of Vanish
 *
 * @author  Gabriella Hotten
 * @version 1.2
 * @since   2020-04-11
 */
public class VanishCommand extends cpsCommand<StaffHub> {

    public VanishCommand(StaffHub mod) {
        super("vanish", Rank.HELPER, mod, "v");
    }

    @Override
    public void execute(Player caller, String[] args) {
        if (!getPlugin().getInStaffMode().contains(caller)) {
            caller.sendMessage(StaffHub.prefix + "§cYou must be in staff mode to do this!");
            return;
        }

        if (StaffHub.getInstance().getOption(StaffOptions.Vanish, caller))
            StaffHub.getInstance().toggleVanish(false, false, true, caller);
        else
            StaffHub.getInstance().toggleVanish(true, false, true, caller);

    }
}
