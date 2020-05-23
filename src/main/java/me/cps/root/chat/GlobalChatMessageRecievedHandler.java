package me.cps.root.chat;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.cps.root.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

public class GlobalChatMessageRecievedHandler{


    @EventListener
    public void handle(ChannelMessageReceiveEvent event) {
        if (!ChatHub.getInstance().isGlobalchat())
            return;

        if (event.getChannel().equalsIgnoreCase("cps")) {
            if (event.getMessage().equalsIgnoreCase("globalchat")) {
                if (event.getData().getString("server").equalsIgnoreCase(Wrapper.getInstance().getServiceId().getName()))
                    return;

                Bukkit.getServer().broadcastMessage(ChatColor.DARK_GRAY + "[" + event.getData().getString("server") + "]" + ChatHub.getInstance().formatChatMessage(
                        event.getData().getString("player"),
                        Rank.valueOf(event.getData().getString("rank")),
                        event.getData().getString("message")
                ));
            }
        }
    }
}
