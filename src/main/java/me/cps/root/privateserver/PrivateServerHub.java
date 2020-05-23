package me.cps.root.privateserver;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.cpsModule;
import me.cps.root.redis.RedisHub;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class PrivateServerHub extends cpsModule
{
    private static PrivateServerHub instance;

    public PrivateServerHub(JavaPlugin plugin)
    {
        super("Private Server Hub", plugin, "1.0-alpha", true);
        instance = this;
    }

    public static PrivateServerHub getInstance()
    {
        return instance;
    }


    public void createPrivateServer(UUID uuid)
    {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource())
        {
            if (RedisHub.getInstance().isPwRequired())
                jedis.auth(RedisHub.getInstance().getPassword());

            String key = "cps.private." + uuid.toString();
            jedis.hset(key, "h", "");
        }
    }
}
