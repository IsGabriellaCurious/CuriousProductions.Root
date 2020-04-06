package me.cps.root.chat;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.chat.commands.GlobalPrefixCommand;
import me.cps.root.cpsModule;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatHub extends cpsModule {

    private Rank globalPrefix; //this was one of my whims when first testing.
    private ChatHub instance;

    public ChatHub(JavaPlugin plugin) {
        super("Chat Hub", plugin, false);
        instance = this;
        registerSelf();
        //globalPrefix = Rank.DEFAULT;
        //registerCommand(new GlobalPrefixCommand(this));
    }

    public ChatHub getInstance() {
        return instance;
    }

    public void setGlobalPrefix(Rank globalPrefix) {
        this.globalPrefix = globalPrefix;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true); //now there is a better way of doing all this (e.g. reformatting the message, etc), but for my easy im just redoing the whole thing.
                                 //also makes disguise integration easier later, so that's a plus.

        String full = Rank.getRank(event.getPlayer().getUniqueId()).getPrefix() + event.getPlayer().getDisplayName() + " " + ChatColor.WHITE + event.getMessage();
        Bukkit.broadcastMessage(full);
    }
}
