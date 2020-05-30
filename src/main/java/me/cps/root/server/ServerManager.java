package me.cps.root.server;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceId;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.cps.root.util.Rank;
import me.cps.root.util.cpsModule;
import me.cps.root.redis.RedisHub;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.Set;
import java.util.UUID;

/**
 * Curious Productions Root
 * Server Manager
 *
 * Handles all server information inside of Redis
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-05-07
 */
public class ServerManager extends cpsModule {

    private static ServerManager instance;

    private int onlineCount;

    private String gameVersion;

    private ServiceId serviceId;

    private static final CloudNetDriver DRIVER = CloudNetDriver.getInstance();

    public ServerManager(JavaPlugin plugin, boolean ssum) {
        super("Server Manager", plugin, "1.0", true);
        CloudNetDriver.getInstance().getEventManager().registerListener(new CloudServiceInfoUpdateHandler());
        instance = this;
        serviceId = Wrapper.getInstance().getServiceId();
        if (ssum)
            publishServerStartMessage();
    }

    private void publishServerStartMessage() {
        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cps", "server_online", new JsonDocument()
        .append("name", serviceId.getName())
        .append("group", serviceId.getTaskName()));
    }

    public static ServerManager getInstance() {
        return instance;
    }

    public ServiceId getServiceId() {
        return serviceId;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            jedis.hset("cps.server." + serviceId.getName(), "onlinePlayers", String.valueOf(onlineCount));
        }
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            jedis.hset("cps.server." + serviceId.getName(), "version", gameVersion);
        }
    }

    public boolean groupOnline(String group) {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            return jedis.exists("cps.onlineGroup." + group);
        }
    }

    public int getServerOnline(String server) {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            return Integer.parseInt(jedis.hget("cps.server." + server, "onlinePlayers"));
        }
    }

    public String getServerVersion(String server) {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            return jedis.hget("cps.server." + server, "version");
        }
    }

    public int getMaxPlayers(String server) {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            return Integer.parseInt(jedis.hget("cps.server." + server, "maxPlayers"));
        }
    }

    public void setMaxPlayers(String server, int max) {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            jedis.hset("cps.server." + server, "maxPlayers", String.valueOf(max));
        }
    }

    public Set<String> getServersInGroup(String group) {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            return jedis.smembers("cps.onlineGroup." + group);
        }
    }

    public void createServer(String group, boolean newServer) {
        if (!newServer) {
            if (group.equalsIgnoreCase("CS")) {
                DRIVER.getNodeInfoProvider().sendCommandLineAsync("service CS-1 start");
                return;
            }
            DRIVER.getNodeInfoProvider().sendCommandLineAsync("service " + group + " start");
        }
        ServiceTask serviceTask = DRIVER.getServiceTaskProvider().getServiceTask(group);
        ServiceInfoSnapshot serviceInfoSnapshot = DRIVER.getCloudServiceFactory().createCloudService(serviceTask);

        DRIVER.getCloudServiceProvider(serviceInfoSnapshot).start();
    }

    public int getGlobalOnline() {
        int online = 0;
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            for (String s : jedis.smembers("cps.onlineServers")) {
                online += getServerOnline(s);
            }

            return online;
        } catch (Exception e) {
            return -1;
        }
    }

    public String getServerGameState(String server) {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            return jedis.hget("cps.server." + server, "gameState");
        }
    }

    public boolean canJoinGroup(Player player, String group, boolean inform) {
        UUID uuid = player.getUniqueId();
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            boolean result = Rank.hasRank(uuid, Rank.valueOf(jedis.hget("cps.serverGroupPermissions", group)));

            if (inform && !result) {
                player.sendMessage("§cYou don't have permission to join this group!");
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1, (float) 0.6);
            }

            return result;
        }
    }

    public boolean groupInMaintenance(String group) {
        ServiceTask serviceTask = CloudNetDriver.getInstance().getServiceTaskProvider().getServiceTask(group);

        return serviceTask.isMaintenance();
    }
}
