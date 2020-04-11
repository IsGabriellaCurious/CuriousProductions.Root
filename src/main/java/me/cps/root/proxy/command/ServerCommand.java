package me.cps.root.proxy.command;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.proxy.ProxyManager;
import me.cps.root.redis.RedisHub;
import org.bukkit.entity.Player;

public class ServerCommand extends cpsCommand<ProxyManager> {


    public ServerCommand(ProxyManager mod) {
        super("server", Rank.DEFAULT, mod);
    }

    @Override
    public void execute(Player caller, String[] args) {
        if (args == null || args.length == 0) {
            caller.sendMessage("§aYou are connected to §e" + RedisHub.getInstance().getServiceId().getName());
            return;
        }

        String server = args[0];

        if (!getPlugin().serverExists(server)) {
            caller.sendMessage("§cError! That server is not online or does not exist.");
            return;
        }

        if (!Rank.hasRank(caller.getUniqueId(), getPlugin().getRequiredRank(server))) {
            caller.sendMessage("§Error! You don't have permission to join this server.");
            return;
        }

        getPlugin().transferPlayer(caller, server);
    }
}
