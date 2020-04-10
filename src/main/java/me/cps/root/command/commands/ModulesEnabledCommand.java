package me.cps.root.command.commands;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.command.CommandHub;
import me.cps.root.command.cpsCommand;
import me.cps.root.cpsModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ModulesEnabledCommand extends cpsCommand<CommandHub> {

    public ModulesEnabledCommand(CommandHub mod) {
        super("modulesenabled", Rank.DEVELOPER, mod, "listmodules");
    }

    @Override
    public void execute(Player caller, String[] args) {
        ArrayList<cpsModule> modulesEnabled = CommandHub.modulesEnabled;
        String t = "" + ChatColor.GREEN + "Active modules are: ";
        for (int i = 0; i < modulesEnabled.size(); i++) { //again, probably not a good way of doing this, but hell it works.
            if (i == modulesEnabled.size()-1)
                if (modulesEnabled.get(i).isCore())
                    t = t  + ChatColor.RED + "*" + modulesEnabled.get(i).getModuleName() + " v" + modulesEnabled.get(i).getVersion();
                else
                    t = t + ChatColor.RED + modulesEnabled.get(i).getModuleName() + " v" + modulesEnabled.get(i).getVersion();
            else
                if (modulesEnabled.get(i).isCore())
                    t = t + ChatColor.RED + "*" + modulesEnabled.get(i).getModuleName() + " v" + modulesEnabled.get(i).getVersion() + ChatColor.GREEN + ", ";
                else
                    t = t + ChatColor.RED + modulesEnabled.get(i).getModuleName() + " v" + modulesEnabled.get(i).getVersion() + ChatColor.GREEN + ", ";
        }
        caller.sendMessage(t);
        caller.sendMessage("" + ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Modules with a * are Core and cannot be disabled.");


    }
}
