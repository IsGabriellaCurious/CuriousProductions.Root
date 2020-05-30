package me.cps.root.proxy;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import me.cps.root.util.Rank;
import me.cps.root.util.cpsModule;
import me.cps.root.proxy.command.HubCommand;
import me.cps.root.proxy.command.ServerCommand;
import me.cps.root.redis.RedisHub;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

/**
 * Curious Productions Root
 * Proxy Manager
 *
 * Handles the switching of servers.
 *
 * @author  Gabriella Hotten
 * @version 1.1
 * @since   2020-04-08
 */
public class ProxyManager extends cpsModule {

    private static ProxyManager instance;

    public ProxyManager(JavaPlugin plugin) {
        super("Proxy Manager", plugin, "1.1", true);
        instance = this;
        registerCommand(new ServerCommand(this));
        registerCommand(new HubCommand(this));
        registerSelf();
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        if (CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask("CPSProxy").isMaintenance()) {
            if (Rank.forceHasRank(event.getUniqueId(), Rank.DEVELOPER))
                event.allow();
            else
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cThis network is currently in maintenance mode.");
        }
    }

}
