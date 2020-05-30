package me.cps.root.disguise;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.cps.root.util.Rank;
import me.cps.root.util.cpsModule;
import me.cps.root.disguise.wrapperutils.WrapperPlayServerPlayerInfo;
import me.cps.root.disguise.commands.DisguiseCommand;
import me.cps.root.disguise.commands.UnDisguiseCommand;
import me.cps.root.scoreboard.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;

/**
 * Curious Productions Root
 * Disguise Manager
 *
 * Does all the disguise work for those special players!
 * NOTICE: THIS IS IN ALPHA AND SHOULD **NOT** BE USED.
 *
 * @author  Gabriella Hotten
 * @version 1.0-alpha
 * @since   2020-05-28
 */
public class DisguiseManager extends cpsModule {

    //1 - disguise name, 2 - origional name
    private HashMap<String, String> disguised = new HashMap<>();
    private HashMap<String, Rank> disguisedRank = new HashMap<>();

    private static DisguiseManager instance;

    public DisguiseManager(JavaPlugin plugin, boolean notice) {
        super("Disguise Manager", plugin, "1.0-alpha", true);
        instance = this;
        registerCommand(new DisguiseCommand(this));
        registerCommand(new UnDisguiseCommand(this));

        if (notice)
            new NoticeRunnable().runTaskTimerAsynchronously(getPlugin(), 0, 20);

        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(plugin, PacketType.Play.Server.PLAYER_INFO) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacket().getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.ADD_PLAYER)
                            return;

                        PlayerInfoData pid = event.getPacket().getPlayerInfoDataLists().read(0).get(0);

                        if (!disguised.containsKey(pid.getProfile().getName()))
                            return;

                        String name = disguised.get(pid.getProfile().getName());
                        Rank rank = disguisedRank.get((pid.getProfile().getName()));

                        PlayerInfoData newPid = new PlayerInfoData(pid.getProfile().withName(name), pid.getLatency(), pid.getGameMode(),
                                WrappedChatComponent.fromText(name));
                        event.getPacket().getPlayerInfoDataLists().write(0, Collections.singletonList(newPid));
                    }
                }
        );
    }

    public static DisguiseManager getInstance() {
        return instance;
    }

    public HashMap<String, Rank> getDisguisedRank() {
        return disguisedRank;
    }

    public HashMap<String, String> getDisguised() {
        return disguised;
    }

    public void disguisePlayer(Player player, String disguise, String skin, Rank rank) {
        PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromPlayer(player), 1,
                EnumWrappers.NativeGameMode.valueOf(player.getGameMode().toString()),
                WrappedChatComponent.fromText(disguise));

        WrapperPlayServerPlayerInfo wpspi = new WrapperPlayServerPlayerInfo();
        wpspi.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        wpspi.setData(Collections.singletonList(pid));
        if (disguised.containsKey(player.getName()))
            disguised.remove(player.getName());
        disguised.put(player.getName(), disguise);

        if (disguisedRank.containsKey(player.getName()))
            disguisedRank.remove(player.getName());
        disguisedRank.put(player.getName(), rank);
        player.setDisplayName(disguise);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p == player) {
                p.sendMessage("§cNOTICE: §7Due to limitations, you're disguise will not appear to you until you switch servers.");
                p.sendMessage("§8This does not compromised your disguise to other players.");
                continue;
            }
            wpspi.sendPacket(p);
            p.hidePlayer(player);
        }
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.showPlayer(player);
        }
        ScoreboardCentre.getInstance().getPrefixes().remove(player);
        ScoreboardCentre.getInstance().setPrefix(player, null, ScoreboardCentre.UpdateAction.UPDATE);
        ScoreboardCentre.getInstance().updatePrefixes(ScoreboardCentre.UpdateAction.UPDATE);

        ScoreboardCentre.getInstance().updateSuffixes();
    }

    public void unDisguise(Player player) {
        disguised.remove(player.getName());
        disguisedRank.remove(player.getName());
        PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromPlayer(player), 1,
                EnumWrappers.NativeGameMode.valueOf(player.getGameMode().toString()),
                WrappedChatComponent.fromText(player.getName()));

        WrapperPlayServerPlayerInfo wpspi = new WrapperPlayServerPlayerInfo();
        wpspi.setAction(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        wpspi.setData(Collections.singletonList(pid));
        player.setDisplayName(player.getName());

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p == player)
                continue;
            p.hidePlayer(player);
            wpspi.sendPacket(p);
        }
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            p.showPlayer(player);
        }
        ScoreboardCentre.getInstance().getPrefixes().remove(player);
        ScoreboardCentre.getInstance().setPrefix(player, null, ScoreboardCentre.UpdateAction.UPDATE);
        ScoreboardCentre.getInstance().updatePrefixes(ScoreboardCentre.UpdateAction.UPDATE);

        ScoreboardCentre.getInstance().updateSuffixes();
    }
}
