package me.cps.root.chat;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.cps.root.util.Rank;
import me.cps.root.chat.commands.AnnounceCommand;
import me.cps.root.util.cpsModule;
import me.cps.root.disguise.DisguiseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Curious Productions Root
 * Chat Hub
 *
 * Chat Hub manages all server and global chat messages.
 * Global chat is powered through CloudNet v3
 *
 * @author  Gabriella Hotten
 * @version 1.3
 * @since   2020-04-03
 */
public class ChatHub extends cpsModule {

    private static ChatHub instance;

    private boolean globalchat;

    public ChatHub(JavaPlugin plugin, boolean globalChat) {
        super("Chat Hub", plugin, "1.3", false);
        instance = this;
        registerSelf();
        this.globalchat = globalChat;
        CloudNetDriver.getInstance().getEventManager().registerListener(new GlobalChatMessageRecievedHandler());
        CloudNetDriver.getInstance().getEventManager().registerListener(new AnnouncementHandler());
        registerCommand(new AnnounceCommand(this));
    }

    public static ChatHub getInstance() {
        return instance;
    }


    public boolean isGlobalchat() {
        return globalchat;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled())
            return;

        event.setCancelled(true); //now there is a better way of doing all this (e.g. reformatting the message, etc), but for my easy im just redoing the whole thing.
                                 //also makes disguise integration easier later, so that's a plus.


        if (DisguiseManager.getInstance().getDisguised().containsKey(event.getPlayer().getName())) {
            String name = DisguiseManager.getInstance().getDisguised().get(event.getPlayer().getName());
            Rank rank = DisguiseManager.getInstance().getDisguisedRank().get(event.getPlayer().getName());

            Bukkit.broadcastMessage(formatChatMessage(name, rank, event.getMessage()));
            if (globalchat)
                sendGlobalChatMessage(name, rank, event.getMessage());
            return;
        }
        Bukkit.broadcastMessage(formatChatMessage(event.getPlayer(), event.getMessage()));
        if (globalchat)
            sendGlobalChatMessage(event.getPlayer().getName(), Rank.getRank(event.getPlayer().getUniqueId()), event.getMessage());
    }

    public String formatChatMessage(Player player, String message) {
        return Rank.getRank(player.getUniqueId()).getPrefix() + player.getName() + " " + ChatColor.WHITE + message;
    }

    public String formatChatMessage(String player, Rank rank, String message) {
        return rank.getPrefix() + player + " " + ChatColor.WHITE + message;
    }

    private void sendGlobalChatMessage(String playerName, Rank rank, String message) {
        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cps", "globalchat", new JsonDocument()
        .append("player", playerName)
        .append("rank", rank.toString())
        .append("message", message)
        .append("server", Wrapper.getInstance().getServiceId().getName()));
    }
}
