package me.cps.root.proxy;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.player.ICloudPlayer;
import me.cps.root.cpsModule;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ProxyManager extends cpsModule {

    private static ProxyManager instance;

    public ProxyManager(JavaPlugin plugin) {
        super("Proxy Manager", plugin, "1.0-alpha", true);
        instance = this;
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
        BridgePlayerManager.getInstance().proxySendPlayer(player, server);
        BridgePlayerManager.getInstance().proxySendPlayerMessage(player, "§aTransferring you to " + server);
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

}
