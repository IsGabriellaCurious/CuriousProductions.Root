package me.cps.root.proxy.command;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.proxy.ProxyManager;
import org.bukkit.entity.Player;

public class HubCommand extends cpsCommand<ProxyManager> {

    public HubCommand(ProxyManager mod) {
        super("hub", Rank.DEFAULT, mod, "lobby");
    }

    @Override
    public void execute(Player caller, String[] args) {
        caller.sendMessage("works");
    }
}
