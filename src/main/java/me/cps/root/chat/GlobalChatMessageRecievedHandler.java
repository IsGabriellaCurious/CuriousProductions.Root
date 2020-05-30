package me.cps.root.chat;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.cps.root.util.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Curious Productions Root
 * Chat Hub - Global Message Handler
 *
 * Listens for, then handles any global chat messages.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-07
 */
public class GlobalChatMessageRecievedHandler{


    @EventListener
    public void handle(ChannelMessageReceiveEvent event) {
        if (!ChatHub.getInstance().isGlobalchat())
            return;

        if (event.getChannel().equalsIgnoreCase("cps")) {
            if (event.getMessage().equalsIgnoreCase("globalchat")) {
                if (event.getData().getString("server").equalsIgnoreCase(Wrapper.getInstance().getServiceId().getName()))
                    return;

                Bukkit.getServer().broadcastMessage(ChatColor.DARK_GRAY + "[" + event.getData().getString("server") + "] " + ChatHub.getInstance().formatChatMessage(
                        event.getData().getString("player"),
                        Rank.valueOf(event.getData().getString("rank")),
                        event.getData().getString("message")
                ));
            }
        }
    }
}
