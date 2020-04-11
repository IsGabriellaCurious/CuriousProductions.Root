package me.cps.root.staff.commands;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.staff.StaffHub;
import org.bukkit.entity.Player;

public class StaffModeCommand extends cpsCommand<StaffHub> {

    public StaffModeCommand(StaffHub mod) {
        super("staffmode", Rank.HELPER, mod, "togglestaff");
    }

    @Override
    public void execute(Player caller, String[] args) {
        getPlugin().staffMode(caller);
    }
}
