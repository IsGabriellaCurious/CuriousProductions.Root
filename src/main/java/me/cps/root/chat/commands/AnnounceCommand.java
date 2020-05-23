package me.cps.root.chat.commands;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import me.cps.root.Rank;
import me.cps.root.chat.ChatHub;
import me.cps.root.command.cpsCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

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
