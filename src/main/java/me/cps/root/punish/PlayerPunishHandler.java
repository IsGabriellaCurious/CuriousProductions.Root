package me.cps.root.punish;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import me.cps.root.util.Rank;
import me.cps.root.account.AccountHub;
import me.cps.root.util.Message;
import me.cps.root.util.center.Centered;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Curious Productions Root
 * Punish Manager - Player Punish Handler
 *
 * Handles the informing/removing the player when they are punished (cross server)
 *
 * @author  Gabriella Hotten
 * @since   2020-05-09
 */
public class PlayerPunishHandler {

    @EventListener
    public void handle(ChannelMessageReceiveEvent event) {
        if (!event.getChannel().equalsIgnoreCase("cps"))
            return;

        if (!event.getMessage().equalsIgnoreCase("punish"))
            return;

        UUID punished = UUID.fromString(event.getData().getString("punished"));
        String punisher = event.getData().getString("punisher");
        PunishType type = PunishType.valueOf(event.getData().getString("type"));
        long duration = event.getData().getLong("duration");
        String reason = event.getData().getString("reason");
        boolean isAc = event.getData().getBoolean("isAc");
        String durationMessage = PunishManager.getInstance().getDurationMessage(duration);

        Player player = Bukkit.getServer().getPlayer(punished);
        if (player != null) {
            if (type == PunishType.BAN) {
                BridgePlayerManager.getInstance().proxyKickPlayer(punished, PunishManager.getInstance().generateBanMessage(player.getUniqueId()));
            } else if (type == PunishType.BLACKLIST) {
                BridgePlayerManager.getInstance().proxyKickPlayer(punished, PunishManager.getInstance().generateBanMessage(player.getUniqueId()));
                Message.broadcast("§c" + player.getName() + " has been blacklisted by " + punisher + ".");
            } else if (type == PunishType.MUTE) {
                player.sendMessage("§cYou have been muted by " + punisher + " for " + durationMessage);
                player.sendMessage("§cReason §8» §f" + reason);
            } else if (type == PunishType.WARN) {
                player.sendMessage("§cYou have been warned by " + punisher + "!");
                player.sendMessage("§cReason §8» §f" + reason);
            }

            if (isAc) {
                Message.broadcast(Centered.create("§8»§m----------------------------------------§r§8«"));
                Message.broadcast(Centered.create("§c" + player.getName() + "§f has been " + type.getMessage() + " by §9§lExile Spray AC"));
                Message.broadcast(" ");
                Message.broadcast(Centered.create("§eReason §8» §f" + reason));
                Message.broadcast(Centered.create("§eDuration §8» §f" + durationMessage));
                Message.broadcast(Centered.create("§8»§m----------------------------------------§r§8«"));
            } else {
                if (type == PunishType.BAN)
                    Message.broadcast("§b" + player.getName() + " has been removed from your game for breaking our rules.");
            }
        }


        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Rank.hasRank(p.getUniqueId(), Rank.HELPER)) {
                p.sendMessage("§7Player " + AccountHub.getInstance().nameFromUUID(punished) + " has been " + type.getMessage() + " by " + punisher + (type != PunishType.WARN ? " for " + durationMessage + "." : "."));
            }
        }



    }

    @EventListener
    public void unPunish(ChannelMessageReceiveEvent event) {
        if (!event.getChannel().equalsIgnoreCase("cps"))
            return;

        if (!event.getMessage().equalsIgnoreCase("punishmentRemoved"))
            return;

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (Rank.hasRank(p.getUniqueId(), Rank.HELPER)) {
                p.sendMessage("§7" + event.getData().getString("player") + " has removed a punishment for " + event.getData().getString("target") + ".");
            }
        }
    }
}
