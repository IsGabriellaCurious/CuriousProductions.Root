package me.cps.root.staff.commands;

import me.cps.root.util.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.staff.StaffHub;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Staff Hub - Toggle Game Review Command
 *
 * Removed: 2020-04-13
 * Reason: It's a bit of dumb command. It was replaced by just being in staff mode.
 *
 * @author  Gabriella Hotten
 * @since   2020-04-11
 */
@Deprecated
public class ToggleGameReviewCommand extends cpsCommand<StaffHub> {

    public ToggleGameReviewCommand(StaffHub mod) {
        super("togglegamereview", Rank.HELPER, mod, "tgr", "gamereview");
    }

    @Override
    public void execute(Player caller, String[] args) {
        /*if (!getPlugin().getInStaffMode().contains(caller)) {
            caller.sendMessage(StaffHub.prefix + "§cYou must be in staff mode to do this!");
            return;
        }

        if (StaffHub.getInstance().getOption(StaffOptions.GameReview, caller)) {
            StaffHub.getInstance().toggleGameReview(false, false, caller);
        } else {
            StaffHub.getInstance().toggleGameReview(true, false, caller);
        }
         */
    }
}
