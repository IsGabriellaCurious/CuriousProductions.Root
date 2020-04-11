package me.cps.root.staff.commands;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.staff.StaffHub;
import me.cps.root.staff.StaffOptions;
import org.bukkit.entity.Player;

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
            StaffHub.getInstance().toggleVanish(false, false, caller);
        else
            StaffHub.getInstance().toggleVanish(true, false, caller);

    }
}
