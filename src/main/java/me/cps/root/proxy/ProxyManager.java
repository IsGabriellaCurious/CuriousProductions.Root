package me.cps.root.proxy;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import me.cps.root.Rank;
import me.cps.root.cpsModule;
import me.cps.root.proxy.command.HubCommand;
import me.cps.root.proxy.command.ServerCommand;
import me.cps.root.redis.RedisHub;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

public class ProxyManager extends cpsModule {

    private static ProxyManager instance;

    public ProxyManager(JavaPlugin plugin) {
        super("Proxy Manager", plugin, "1.1-beta", true);
        instance = this;
        registerCommand(new ServerCommand(this));
        registerCommand(new HubCommand(this));
    }

    public static ProxyManager getInstance() {
        return instance;
    }

    //THIS SHOULD ONLY BE RAN ON **ONLINE PLAYERS**
    public ICloudPlayer getCloudPlayer(Player player) {
        return BridgePlayerManager.getInstance().getOnlinePlayer(player.getUniqueId());
    }

    public void transferPlayer(Player p, String server) {
        ICloudPlayer player = getCloudPlayer(p);
        BridgePlayerManager.getInstance().proxySendPlayerMessage(player, "§aTransferring you to " + server);
        BridgePlayerManager.getInstance().proxySendPlayer(player, server);
    }

    //TODO premium hub support
    public void sendToLobby(Player p, boolean notif) {
        ICloudPlayer player = getCloudPlayer(p);
        if (notif)
            BridgePlayerManager.getInstance().proxySendPlayer(player, "§aSending you to a lobby...");
        BridgePlayerManager.getInstance().proxySendPlayer(player, "Lobby-1");
    }

    public void sendPlayerMessage(Player p, String message) {
        ICloudPlayer player = getCloudPlayer(p);
        BridgePlayerManager.getInstance().proxySendPlayerMessage(player, message);
    }

    public boolean serverExists(String server) {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            return jedis.smembers("cps.onlineServers").contains(server);
        }
    }

    public Rank getRequiredRank(String server) {
        //assuming the server exists
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            return Rank.valueOf(jedis.hget("cps.server." + server, "rankRequired"));
        }
    }

}
