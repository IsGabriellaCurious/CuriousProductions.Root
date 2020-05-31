package me.cps.root.networkdata;

import org.bukkit.ChatColor;

/**
 * Curious Productions Root
 * Network Data Hub - Network Data Base
 *
 * Class that holds the network info.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-30
 */
public class NetworkDataBase {

    public NetworkDataBase(String mysqlUrl, String mysqlUser, String mysqlPw, String mysqlDb, int mysqlPort, String mysqlTimezone,
                           String redisUrl, String redisPw, int redisPort,
                           String networkName, ChatColor networkPrimaryColour, ChatColor networkSecondaryColour,
                           String networkWebsite, String networkIp, String anicheatName, String configVersion)
    {
        this.mysqlUrl = mysqlUrl;
        this.mysqlUser = mysqlUser;
        this.mysqlPw = mysqlPw;
        this.mysqlDb = mysqlDb;
        this.mysqlPort = mysqlPort;
        this.mysqlTimezone = mysqlTimezone;

        this.redisUrl = redisUrl;
        this.redisPw = redisPw;
        this.redisPort = redisPort;

        this.networkName = networkName;
        this.networkPrimaryColour = networkPrimaryColour;
        this.networkSecondaryColour = networkSecondaryColour;
        this.networkWebsite = networkWebsite;
        this.networkIp = networkIp;
        this.anticheatName = anicheatName;
        this.configVersion = configVersion;
    }

    public NetworkDataBase() {}

    private String mysqlUrl;
    private String mysqlUser;
    private String mysqlPw;
    private String mysqlDb;
    private int mysqlPort;
    private String mysqlTimezone;

    private String redisUrl;
    private String redisPw;
    private int redisPort;

    private String networkName;
    private ChatColor networkPrimaryColour;
    private ChatColor networkSecondaryColour;
    private String networkWebsite;
    private String networkIp;
    private String anticheatName;
    private String configVersion;

    public String getMysqlUrl() { return mysqlUrl; }
    public String getMysqlUser() { return mysqlUser; }
    public String getMysqlPw() { return mysqlPw; }
    public String getMysqlDb() { return mysqlDb; }
    public int getMysqlPort() { return mysqlPort; }
    public String getMysqlTimezone() { return mysqlTimezone; }
    public String getRedisUrl() { return redisUrl; }
    public String getRedisPw() { return redisPw; }
    public int getRedisPort() { return redisPort; }
    public String getNetworkName() { return networkName; }
    public ChatColor getNetworkPrimaryColour() { return networkPrimaryColour; }
    public ChatColor getNetworkSecondaryColour() { return networkSecondaryColour; }
    public String getNetworkWebsite() { return networkWebsite; }
    public String getNetworkIp() { return networkIp; }
    public String getAnticheatName() { return anticheatName; }
    public String getConfigVersion() { return configVersion; }
}
