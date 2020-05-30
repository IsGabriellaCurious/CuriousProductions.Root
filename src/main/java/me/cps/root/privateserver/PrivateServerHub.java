package me.cps.root.privateserver;

import me.cps.root.util.cpsModule;
import me.cps.root.redis.RedisHub;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.UUID;

/**
 * Curious Productions Root
 * Private Server Hub
 *
 * A fancy place for players to hang out with their friends!
 * NOTICE: THIS IS IN ALPHA AND SHOULD **NOT** BE USED.
 *
 * @author  Gabriella Hotten
 * @version 1.0-alpha
 * @since   2020-05-11
 */
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
