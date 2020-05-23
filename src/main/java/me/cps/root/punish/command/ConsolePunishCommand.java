package me.cps.root.punish.command;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.account.AccountHub;
import me.cps.root.punish.PunishDuration;
import me.cps.root.punish.PunishManager;
import me.cps.root.punish.PunishType;
import me.cps.root.util.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

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
