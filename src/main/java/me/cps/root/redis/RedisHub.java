package me.cps.root.redis;

import de.dytanic.cloudnet.driver.service.ServiceId;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.cps.root.networkdata.NetworkDataHub;
import me.cps.root.util.Rank;
import me.cps.root.util.cpsModule;
import me.cps.root.util.Message;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Curious Productions Root
 * Redis Hub
 *
 * Handles everything redis! Mainly server configurations.
 *
 * @author  Gabriella Hotten
 * @version 1.3
 * @since   2020-04-07
 */
public class RedisHub extends cpsModule {

    /*
    When doing redis stuff, do a format something like this:
    public void yes() {
        try (Jedis jedis = pool.getResource()) {
            if (pwRequired)
                jedis.auth(password);
            jedis.set("foo", "bar");
        }
    }

    using the "try" bit, you won't have to bother closing anything as it will be done automatic.
     */

    private static RedisHub instance;

    private JedisPool pool;

    private String password;
    private boolean pwRequired;

    private ServiceId serviceId;

    //Server Configuration params
    public Rank rankRequired;
    public int maxPlayers;
    public boolean donatorPriority; //e.g. will kick a regular player from the server if the server is full and a game server.
    public boolean gameServer;

    //when the server config params are entered, they act only as a default. redis has ultimate control over the server config.
    public RedisHub(JavaPlugin plugin, Rank rankRequired, int maxPlayers, boolean donatorPriority, boolean gameServer) {
        super("Redis Hub", plugin, "1.3", true);
        instance = this;

        serviceId = Wrapper.getInstance().getServiceId();

        this.password = NetworkDataHub.getNetworkDataBase().getRedisPw();
        this.rankRequired = rankRequired;
        this.maxPlayers = maxPlayers;
        this.donatorPriority = donatorPriority;
        this.gameServer = gameServer;
        if (password == null || password.equals("") || password.equals("null"))
            pwRequired = false;
        else
            pwRequired = true;

        pool = new JedisPool(new JedisPoolConfig(), NetworkDataHub.getNetworkDataBase().getRedisUrl(), NetworkDataHub.getNetworkDataBase().getRedisPort());
        //pool.getResource().select(13); //CPS will be using DB13 //OK THAT DOESN'T WORK, IT DOES TO DB0

        initServer();
    }

    public static RedisHub getInstance() {
        return instance;
    }

    public JedisPool getPool() {
        return pool;
    }

    public ServiceId getServiceId() {
        return serviceId;
    }

    public String getPassword() {
        return password;
    }

    public boolean isPwRequired() {
        return pwRequired;
    }

    //this will register the server inside of redis, will be used later when bungeecord comes into all this (and probably cloudnet)
    private void initServer() {
        try (Jedis jedis = pool.getResource()) {
            if (pwRequired)
                jedis.auth(password);

            serviceId = Wrapper.getInstance().getServiceId(); //what we need to know about the server from this. (cloudnte)

            String key = "cps.server." + serviceId.getName();
            if (!jedis.exists(key)) {
                Message.console("§cError! Server config key not found. Creating one now...");
                jedis.hset(key, "rankRequired", rankRequired.toString());
                jedis.hset(key, "maxPlayers", String.valueOf(maxPlayers));
                jedis.hset(key, "donatorPriority", String.valueOf(donatorPriority));
                jedis.hset(key, "gameServer", String.valueOf(gameServer));
                jedis.hset(key, "onlinePlayers", "0");
                jedis.hset(key, "version", "waiting");
                Message.console("§aConfig created and applied!");
            } else {
                Message.console("§aServer config found. Loading...");
                rankRequired = Rank.valueOf(jedis.hget(key, "rankRequired"));
                maxPlayers = Integer.parseInt(jedis.hget(key, "maxPlayers"));
                donatorPriority = Boolean.parseBoolean(jedis.hget(key, "donatorPriority"));
                gameServer = Boolean.parseBoolean(jedis.hget(key, "gameServer"));
                Message.console("§aServer config has been loaded and applied successfully!");
            }

            jedis.sadd("cps.onlineServers", serviceId.getName());
            Message.console("§aServer registered in the onlineServers registry!");

            jedis.sadd("cps.onlineGroup." + serviceId.getTaskName(), serviceId.getName());
            Message.console("§aServer registered in the onlineGroup." + serviceId.getTaskName() + " registry!");

            Message.console("§aServer " + serviceId.getName() + " has been initialized successfully!");
        } catch (Exception e) {
            Message.console("§cThere was an error initializing the server! Please see the stack trace below...");
            e.printStackTrace();
        }
    }

    public void deInitServer() {
        try (Jedis jedis = pool.getResource()) {
            if (pwRequired)
                jedis.auth(password);

            Message.console("§cShutting down the server...");
            jedis.srem("cps.onlineServers", serviceId.getName());
            Message.console("§cServer removed from online servers list successfully.");

            jedis.srem("cps.onlineGroup." + serviceId.getTaskName(), serviceId.getName());
            Message.console("§cServer removed from the onlineGroup." + serviceId.getTaskName() + " list successfully.");

            jedis.hset("cps.server." + serviceId.getName(), "onlinePlayers", "0");
        } catch (Exception e) {
            Message.console("§cFailed to remove server from the online servers list. See stack trace below...");
            e.printStackTrace();
        }
    }
}
