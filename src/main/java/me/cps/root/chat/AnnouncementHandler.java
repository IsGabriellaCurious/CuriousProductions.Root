package me.cps.root.chat;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import me.cps.root.server.ServerManager;
import me.cps.root.util.Message;
import me.cps.root.util.PlayerTitle;

/**
 * Curious Productions Root
 * Chat Hub - Announcement Handler
 *
 * Listens for, then handles announcements by broadcasting them to the server.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-08
 */
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
