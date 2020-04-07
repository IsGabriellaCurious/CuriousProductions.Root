package me.cps.root.redis;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import de.dytanic.cloudnet.driver.service.ServiceId;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.cps.root.Rank;
import me.cps.root.cpsModule;
import me.cps.root.util.Message;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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
    public RedisHub(JavaPlugin plugin, String password /* password to be left null if one is not needed */, Rank rankRequired, int maxPlayers, boolean donatorPriority, boolean gameServer) {
        super("Redis Hub", plugin, "1.0-alpha", true);
        instance = this;

        serviceId = Wrapper.getInstance().getServiceId();

        this.password = password;
        this.rankRequired = rankRequired;
        this.maxPlayers = maxPlayers;
        this.donatorPriority = donatorPriority;
        this.gameServer = gameServer;
        if (password == null)
            pwRequired = false;
        else
            pwRequired = true;

        pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);
        //pool.getResource().select(13); //CPS will be using DB13 //OK THAT DOESN'T WORK, IT DOES TO DB0

        initServer();
    }

    public static RedisHub getInstance() {
        return instance;
    }

    //this will register the server inside of redis, will be used later when bungeecord comes into all this (and probably cloudnet)
    private void initServer() {
        try (Jedis jedis = pool.getResource()) {
            if (pwRequired)
                jedis.auth(password);

            serviceId = Wrapper.getInstance().getServiceId(); //what we need to know about the server from this. (cloudnte)

            String key = "cps.server." + serviceId.getTaskName() + "." + serviceId.getName();
            if (!jedis.exists(key)) {
                Message.console("§cError! Server config key not found. Creating one now...");
                jedis.hset(key, "rankRequired", rankRequired.toString());
                jedis.hset(key, "maxPlayers", String.valueOf(maxPlayers));
                jedis.hset(key, "donatorPriority", String.valueOf(donatorPriority));
                jedis.hset(key, "gameServer", String.valueOf(gameServer));
                Message.console("§aConfig created and applied!");
            } else {
                Message.console("§aServer config found. Loading...");
                rankRequired = Rank.valueOf(jedis.hget(key, "rankRequired"));
                maxPlayers = Integer.valueOf(jedis.hget(key, "maxPlayers"));
                donatorPriority = Boolean.valueOf(jedis.hget(key, "donatorPriority"));
                gameServer = Boolean.valueOf(jedis.hget(key, "gameServer"));
                Message.console("§aServer config has been loaded and applied successfully!");
            }

            jedis.sadd("cps.onlineServers", serviceId.getName());
            Message.console("§aServer registered in the onlineServers registry!");

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
            jedis.srem("cps.onlineServers,", serviceId.getName());
            Message.console("§cServer removed from online servers list successfully.");
        } catch (Exception e) {
            Message.console("§cFailed to remove server from the online servers list. See stack trace below...");
            e.printStackTrace();
        }
    }
}
