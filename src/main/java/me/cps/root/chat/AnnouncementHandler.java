package me.cps.root.chat;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import me.cps.root.server.ServerManager;
import me.cps.root.util.Message;
import me.cps.root.util.PlayerTitle;

public class AnnouncementHandler {

    @EventListener
    public void handle(ChannelMessageReceiveEvent event) {
        if (!event.getChannel().equalsIgnoreCase("cps"))
            return;

        if (!event.getMessage().equalsIgnoreCase("announcement"))
            return;

        if (ServerManager.getInstance().getGameVersion().contains("1.8")) {
            PlayerTitle.all("§e§lANNOUNCEMENT", "§f" + event.getData().getString("message"), 10, 100, 10, true);
        } else {
            Message.broadcast("§e§lANNOUNCEMENT §8» §f" + event.getData().getString("message"));
        }
    }
}
