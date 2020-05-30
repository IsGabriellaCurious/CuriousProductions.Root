package me.cps.root.staff;

import me.cps.root.util.Rank;
import me.cps.root.util.cpsModule;
import me.cps.root.redis.RedisHub;
import me.cps.root.scoreboard.ScoreboardCentre;
import me.cps.root.staff.commands.StaffModeCommand;
import me.cps.root.staff.commands.ToggleGameChatCommand;
import me.cps.root.staff.commands.VanishCommand;
import me.cps.root.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;

/**
 * Curious Productions Root
 * Staff Hub
 *
 * Handles Staff Mode (incl vanish)
 * TODO: clean up this code a bit
 *
 * @author  Gabriella Hotten
 * @version 1.2
 * @since   2020-04-11
 */
public class StaffHub extends cpsModule {

    private static StaffHub instance;

    private ArrayList<Player> inStaffMode;
    private ArrayList<Player> vanished;

    public static String prefix = "§6Staff> ";

    public StaffHub(JavaPlugin plugin) {
        super("Staff Hub", plugin, "1.2", true);
        instance = this;
        registerSelf();
        inStaffMode = new ArrayList<>();
        vanished = new ArrayList<>();
        registerCommand(new VanishCommand(this));
        registerCommand(new ToggleGameChatCommand(this));
        //registerCommand(new ToggleGameReviewCommand(this));
        registerCommand(new StaffModeCommand(this));
    }

    @EventHandler(priority =  EventPriority.LOWEST)
    public void onjoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Player p : vanished) {
            if (!Rank.hasRank(player.getUniqueId(), Rank.HELPER))
                event.getPlayer().hidePlayer(p);
        }
        if (!Rank.hasRank(player.getUniqueId(), Rank.HELPER))
            return;

        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            String key = "cps.instaffmode";

            if (jedis.smembers(key).contains(player.getName())) {
                staffMode(player);
            }
        }

        if (!getInStaffMode().isEmpty()) {
            player.sendMessage("§cBe aware! The following are currently in staff mode:");
            for (Player p : inStaffMode) {
                player.sendMessage(Rank.getRank(p.getUniqueId()).getPrefix() + p.getName() + " §6[V: " + (getOption(StaffOptions.Vanish, p) ? "§aT" : "§cF") + "§6]");
            }
            player.sendMessage("§cDO NOT blow their cover if they are vanished.");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (getVanished().contains(event.getPlayer()))
            getVanished().remove(event.getPlayer());

        if (getInStaffMode().contains(event.getPlayer()))
            getInStaffMode().remove(event.getPlayer());
    }

    public static StaffHub getInstance() {
        return instance;
    }

    public ArrayList<Player> getInStaffMode() {
        return inStaffMode;
    }

    public ArrayList<Player> getVanished() {
        return vanished;
    }

    @Deprecated
    public void staffMode(Player player) {
        if (!getInStaffMode().contains(player)) {
            ScoreboardCentre.getInstance().updateSuffixes();
            ScoreboardCentre.getInstance().setSuffix(player, "§6[SM]");
            boolean vanish;
            boolean gameChat;
            //boolean gameReview;
            getPlugin().getServer().getPluginManager().callEvent(new StaffModeUpdateEvent(player, true));
            getInStaffMode().add(player);
            try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
                String key = "cps.staffmode." + player.getName();
                if (!jedis.exists(key)) {
                    setOption(StaffOptions.Vanish, true, true, player);
                    vanish = true;
                    setOption(StaffOptions.GameChat, false, true, player);
                    gameChat = false;
                    //setOption(StaffOptions.GameReview, true, true, player);
                    //gameReview = true;
                } else {
                    vanish = Boolean.valueOf(jedis.hget(key, StaffOptions.Vanish.getRedisName()));
                    setOption(StaffOptions.Vanish, vanish, true, player);
                    gameChat = Boolean.valueOf(jedis.hget(key, StaffOptions.GameChat.getRedisName()));
                    setOption(StaffOptions.GameChat, gameChat, true, player);
                    //gameReview = Boolean.valueOf(jedis.hget(key, StaffOptions.GameReview.getRedisName()));
                    //setOption(StaffOptions.GameReview, true, true, player);
                }
                jedis.sadd("cps.instaffmode", player.getName());
            }
            Rank rank = Rank.getRank(player.getUniqueId());
            player.sendMessage(prefix + "§aYou are now in staff mode.");
            player.sendMessage(prefix + "§7Your settings:");
            player.sendMessage(prefix + "§9Vanish: " + (vanish ? "§aEnabled" : "§cDisabled"));
            if (rank == Rank.HELPER)
                player.sendMessage("§8Does not apply in games, use Game Review");
            player.sendMessage(prefix + "§eAnti-Game Chat: " + (gameChat ? "§aEnabled" : "§cDisabled"));
            //player.sendMessage(prefix + "§aGame Review: " + (gameReview ? "§aEnabled" : "§cDisabled"));
            //if (rank == Rank.HELPER)
                //player.sendMessage("§8You will not be in the game. You shall be invisible and players wont know you've joined.");
            if (vanish)
                toggleVanish(true, true, true, player);

            Message.staff("§7[§o" + Rank.getRank(player.getUniqueId()).getColor() + player.getName() + " §ehas entered staff mode.§7]");
        } else {
            ScoreboardCentre.getInstance().updateSuffixes();
            ScoreboardCentre.getInstance().setSuffix(player, null);
            //setOption(StaffOptions.GameReview, false, true, player);
            player.sendMessage(prefix + "§cYou are no-longer in staff mode.");
            getInStaffMode().remove(player);
            try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
                jedis.srem("cps.instaffmode", player.getName());
            }
            getPlugin().getServer().getPluginManager().callEvent(new StaffModeUpdateEvent(player, false));
            if (getOption(StaffOptions.Vanish, player))
                toggleVanish(false, true, false, player);

            Message.staff("§7[§o" + Rank.getRank(player.getUniqueId()).getColor() + player.getName() + " §ehas left staff mode.§7]");

        }
    }

    public boolean getOption(StaffOptions option, Player player) {

        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            String key = "cps.staffmode." + player.getName();

            return Boolean.valueOf(jedis.hget(key, option.getRedisName()));
        }
    }

    public void setOption(StaffOptions option, boolean result, boolean override, Player player) {
        try (Jedis jedis = RedisHub.getInstance().getPool().getResource()) {
            String key = "cps.staffmode." + player.getName();

            jedis.hset(key, option.getRedisName(), String.valueOf(result));
            getPlugin().getServer().getPluginManager().callEvent(new StaffOptionUpdateEvent(player, option, result, override));
        }
    }

    public void toggleVanish(boolean toggle, boolean override, boolean push, Player staff) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!Rank.hasRank(p.getUniqueId(), Rank.HELPER))
                if (toggle) {
                    p.hidePlayer(staff);
                    vanished.add(staff);
                } else {
                    p.showPlayer(staff);
                    vanished.remove(staff);
                }

        }
        if (push) {
            staff.sendMessage(prefix + "§7Vanish: " + (toggle ? "§aEnabled" : "§cDisabled"));
            setOption(StaffOptions.Vanish, toggle, override, staff);
        } else {
            staff.sendMessage(prefix + "§7Temp Vanish: " + (toggle ? "§aEnabled" : "§cDisabled"));
        }
    }

    public void toggleGameChat(boolean toggle, boolean override, Player staff) {
        setOption(StaffOptions.GameChat, toggle, override,staff);
        staff.sendMessage(prefix + "§7Game Chat: " + (toggle ? "§aEnabled" : "§cDisabled"));
    }

    /*public void toggleGameReview(boolean toggle, boolean override, Player staff) {
        setOption(StaffOptions.GameReview, toggle, override, staff);
        staff.sendMessage(prefix + "§7Game Review: " + (toggle ? "§aEnabled" : "§cDisabled"));
    }*/


}
