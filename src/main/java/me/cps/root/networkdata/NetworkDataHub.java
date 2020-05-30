package me.cps.root.networkdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.cps.root.util.Message;
import me.cps.root.util.cpsModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Curious Productions Root
 * Network Data Hub
 *
 * Holds all information about the network.
 * This through a yaml file found at /home/cps/network_data.yaml
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-05-30
 */
public class NetworkDataHub extends cpsModule {

    private static NetworkDataBase networkDataBase;
    private String pluginVersion;
    private String networkConfigVersion;

    public NetworkDataHub(JavaPlugin plugin) {
        super("Network Data Hub", plugin, "1.0", true);
        this.pluginVersion = plugin.getDescription().getVersion();
        parseNetworkData();
        Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            checkForUpdates(ServerType.NETWORKCONFIG);
            checkForUpdates(ServerType.ROOT);
        }, 100);

    }

    public static NetworkDataBase getNetworkDataBase() { return networkDataBase; }

    public void parseNetworkData() {
        Message.console("§eStarted parse of Network Data...");
        try {
            File file = new File("/home/cps/network_data.yaml");
            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            networkDataBase = om.readValue(file, NetworkDataBase.class);
            this.networkConfigVersion = networkDataBase.getConfigVersion();
            Message.console("§aParse successful!");
        } catch (Exception e) {
            Message.console("§c§lERROR: §fError parsing network data. Defaults have been applied. Please see error below:");
            e.printStackTrace();
            networkDataBase = new NetworkDataBase("localhost", "cps", "password", "cps", 3306,
                    "localhost", "", 6379,
                    "CPS", ChatColor.AQUA, ChatColor.BLUE,
                    "cps.me", "play.cps.me", "CPS AC", "1.0");
        }
    }

    //Should only be used for this server type as it gets the current version from the plugin yml
    public void checkForUpdates(ServerType serverType) {
        Message.console("----------------------------------------");
        Message.console("Checking for updates for " + serverType);
        try {
            URL url = new URL(serverType.getVersionUrl());

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ver = in.readLine();
            in.close();

            String _localVer = (serverType == ServerType.NETWORKCONFIG ? networkConfigVersion : pluginVersion);

            Message.console("Latest version is " + ver);
            Message.console("Our version is " + _localVer);

            if (_localVer.equals(ver)) {
                Message.console("§a" + serverType.toString() + " is up to date!");
            } else {
                Message.console("§cWarning! This " + (serverType == ServerType.NETWORKCONFIG ? "config" : "plugin") + " is out of date. Please visit " + serverType.getGithubUrl() + " to get the latest " + (serverType == ServerType.NETWORKCONFIG ? "config file." : "plugin."));
            }

        } catch (Exception e) {
            Message.console("§cError checking for updates.");
            e.printStackTrace();
        }
        Message.console("----------------------------------------");
    }
}
