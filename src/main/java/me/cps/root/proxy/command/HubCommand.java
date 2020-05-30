package me.cps.root.proxy.command;

import me.cps.root.util.Rank;
import me.cps.root.command.cpsCommand;
import me.cps.root.proxy.ProxyManager;
import org.bukkit.entity.Player;

/**
 * Curious Productions Root
 * Proxy Manager - Hub Command
 *
 * At some point will handle going to the hub. At the moment, we just use CloudNet's /hub command.
 *
 * @author  Gabriella Hotten
 * @since   2020-04-11
 */
public class HubCommand extends cpsCommand<ProxyManager> {

    public HubCommand(ProxyManager mod) {
        super("hub", Rank.OWNER, mod, "lobby");
    }

    @Override
    public void execute(Player caller, String[] args) {
        caller.sendMessage("works");
    }
}
