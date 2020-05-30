package me.cps.root.punish.command;

import me.cps.root.util.Rank;
import me.cps.root.account.AccountHub;
import me.cps.root.command.cpsCommand;
import me.cps.root.punish.PunishData;
import me.cps.root.punish.PunishManager;
import me.cps.root.punish.gui.PunishGUI;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Curious Productions Root
 * Punish Manager - Punish Command
 *
 * Opens the punish GUI.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-09
 */
public class PunishCommand extends cpsCommand<PunishManager> {

    public PunishCommand(PunishManager mod) {
        super("punish", Rank.HELPER, mod, "p");
    }

    @Override
    public void execute(Player caller, String[] args) {
        if (args == null || args.length == 0) {
            caller.sendMessage("§cCorrect Usage: /punish <player>");
            return;
        }

        UUID uuid = AccountHub.getInstance().uuidFromName(args[0]);
        if (uuid == null) {
            caller.sendMessage("§cThat player does not exist!");
            return;
        }

        PunishData data = new PunishData();
        data.setPlayer(caller.getName());
        data.setTarget(args[0]);

        PunishManager.getInstance().getPlayerPunishing().put(caller, data);

        PunishGUI.openGUI(caller, args[0]);
    }
}
