package me.cps.root.networkdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.cps.root.util.Message;
import me.cps.root.util.cpsModule;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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

    public NetworkDataHub(JavaPlugin plugin) {
        super("Network Data Hub", plugin, "1.0", true);
        parseNetworkData();
    }

    public static NetworkDataBase getNetworkDataBase() { return networkDataBase; }

    public void parseNetworkData() {
        Message.console("§eStarted parse of Network Data...");
        try {
            File file = new File("/home/cps/network_data.yaml");
            ObjectMapper om = new ObjectMapper(new YAMLFactory());

            networkDataBase = om.readValue(file, NetworkDataBase.class);
            Message.console("§aParse successful!");
        } catch (Exception e) {
            Message.console("§c§lERROR: §fError parsing network data. Defaults have been applied. Please see error below:");
            e.printStackTrace();
            networkDataBase = new NetworkDataBase("localhost", "cps", "password", "cps", 3306,
                    "localhost", "", 6379,
                    "CPS", ChatColor.AQUA, ChatColor.BLUE,
                    "cps.me", "play.cps.me");
        }
    }
}
