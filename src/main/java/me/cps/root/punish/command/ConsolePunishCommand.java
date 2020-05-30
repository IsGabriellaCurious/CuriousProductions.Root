package me.cps.root.punish.command;

import me.cps.root.account.AccountHub;
import me.cps.root.punish.PunishDuration;
import me.cps.root.punish.PunishManager;
import me.cps.root.punish.PunishType;
import me.cps.root.util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Curious Productions Root
 * Punish Manager - Console Punish Command
 *
 * A neat little command for AAC to use when banning cheaters.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-09
 */
public class ConsolePunishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            String player = args[0];
            PunishType type = PunishType.valueOf(args[1]);
            PunishDuration duration = PunishDuration.valueOf(args[2]);
            String reason = Message.combine(args, 3);

            PunishManager.getInstance().punish(AccountHub.getInstance().uuidFromName(player), "", type, reason, duration, true);
        }

        return true;
    }
}
