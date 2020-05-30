package me.cps.root.proxy.command;

import me.cps.root.util.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.proxy.ProxyManager;
import me.cps.root.redis.RedisHub;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Proxy Manager - Server Command
 *
 * Commands for players to use to switch servers.
 *
 * @author  Gabriella Hotten
 * @since   2020-04-11
 */
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

        if (!Rank.hasRank(caller.getUniqueId(), Rank.HELPER)) {
            caller.sendMessage("§cError! You don't have permission to use this.");
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
