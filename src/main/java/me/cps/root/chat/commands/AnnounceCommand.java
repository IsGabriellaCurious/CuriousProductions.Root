package me.cps.root.chat.commands;

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import me.cps.root.util.Rank;
import me.cps.root.chat.ChatHub;
import me.cps.root.command.cpsCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Curious Productions Root
 * Chat Hub - Announce Command
 *
 * The command which sends an announcement to all servers.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-08
 */
public class AnnounceCommand extends cpsCommand<ChatHub> {

    public AnnounceCommand(ChatHub mod) {
        super("announce", Rank.JUNIORADMIN, mod);
    }

    @Override
    public void execute(Player caller, String[] args) {
        if (args == null || args.length == 0) {
            caller.sendMessage("§cCorrect Usage: /announce <message>");
            return;
        }

        String message = Arrays.asList(args).stream().collect(Collectors.joining(" "));
        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cps", "announcement", new JsonDocument()
        .append("message", message));
    }
}
