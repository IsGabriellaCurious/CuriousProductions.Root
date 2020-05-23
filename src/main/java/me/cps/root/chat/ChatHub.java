package me.cps.root.chat;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.cps.root.Rank;
import me.cps.root.chat.commands.AnnounceCommand;
import me.cps.root.cpsModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatHub extends cpsModule {

    private Rank globalPrefix; //this was one of my whims when first testing.
    private static ChatHub instance;

    private boolean globalchat;

    public ChatHub(JavaPlugin plugin, boolean globalChat) {
        super("Chat Hub", plugin, "1.0-beta", false);
        instance = this;
        registerSelf();
        this.globalchat = globalChat;
        CloudNetDriver.getInstance().getEventManager().registerListener(new GlobalChatMessageRecievedHandler());
        CloudNetDriver.getInstance().getEventManager().registerListener(new AnnouncementHandler());
        //globalPrefix = Rank.DEFAULT;
        //registerCommand(new GlobalPrefixCommand(this));
        registerCommand(new AnnounceCommand(this));
    }

    public static ChatHub getInstance() {
        return instance;
    }

    public void setGlobalPrefix(Rank globalPrefix) {
        this.globalPrefix = globalPrefix;
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
