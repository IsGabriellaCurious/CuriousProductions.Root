package me.cps.root.punish;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import de.dytanic.cloudnet.common.document.gson.JsonDocument;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import me.cps.root.account.AccountHub;
import me.cps.root.cpsModule;
import me.cps.root.punish.command.ConsolePunishCommand;
import me.cps.root.punish.command.PunishCommand;
import me.cps.root.punish.gui.BanGUI;
import me.cps.root.punish.gui.DurationGUI;
import me.cps.root.punish.gui.PunishGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PunishManager extends cpsModule {

    private static PunishManager instance;
    private HashMap<Player, PunishData> playerPunishing = new HashMap<>();
    private ArrayList<Player> switching = new ArrayList<>();
    private HashMap<Player, String> removingReason = new HashMap<>();
    private HashMap<Player, PunishData> settingReason = new HashMap<>();

    public PunishManager(JavaPlugin plugin, boolean register18) {
        super("Punish Manager", plugin, "1.0-alpha", true);
        instance = this;
        CloudNetDriver.getInstance().getEventManager().registerListener(new PlayerPunishHandler());
        registerSelf();
        plugin.getCommand("consolepunish").setExecutor(new ConsolePunishCommand());

        if (register18) {
            registerListener(new PunishInventoryHandlers18());
        }
        registerCommand(new PunishCommand(this));
    }

    public HashMap<Player, PunishData> getPlayerPunishing() {
        return playerPunishing;
    }

    public void updateData(Player player, PunishData data) {
        PunishManager.getInstance().getPlayerPunishing().remove(player);
        PunishManager.getInstance().getPlayerPunishing().put(player, data);
    }

    public ArrayList<Player> getSwitching() {
        return switching;
    }

    public static PunishManager getInstance() {
        return instance;
    }

    public HashMap<Player, String> getRemovingReason() {
        return removingReason;
    }

    public HashMap<Player, PunishData> getSettingReason() {
        return settingReason;
    }

    public void punish(UUID punished, String punisher, PunishType punishType, String reason, PunishDuration durationSeconds, boolean antiCheat) {
        long milliToEnd = durationSeconds.getSeconds() * 1000;
        long end = milliToEnd + System.currentTimeMillis();

        if (durationSeconds.getSeconds() == -1)
            end = -1;

        if (antiCheat)
            punisher = "Exile Spray AC";

        boolean idFound = false;
        String punishmentId = "";
        while (!idFound) {
            String id = createPunishmentId();
            if (punishmentIdExists(id)) {
                idFound = false;
            } else {
                idFound = true;
                punishmentId = id;
            }
        }

        try {
            Connection connection = AccountHub.getInstance().createConnection();

            if (punishType != PunishType.WARN) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO `punish.active` (punishmentId, uuid, punisher, type, duration, reason) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, punishmentId);
                statement.setString(2, punished.toString());
                statement.setString(3, punisher);
                statement.setString(4, punishType.toString());
                statement.setLong(5, end);
                statement.setString(6, reason);

                statement.executeUpdate();
            }

            PreparedStatement history = connection.prepareStatement("INSERT INTO `punish.history` (punishmentId, uuid, punisher, type, duration, durationType, reason, active, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            history.setString(1, punishmentId);
            history.setString(2, punished.toString());
            history.setString(3, punisher);
            history.setString(4, punishType.toString());
            history.setLong(5, end);
            history.setString(6, durationSeconds.toString());
            history.setString(7, reason);
            if (punishType != PunishType.WARN)
                history.setString(8, "true");
            else
                history.setString(8, "false");
            history.setString(9, "" + java.time.LocalDate.now());

            history.executeUpdate();

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cps", "punish", new JsonDocument()
        .append("punished", punished.toString())
        .append("punisher", punisher)
        .append("type", punishType.toString())
        .append("duration", end)
        .append("reason", reason)
        .append("isAc", antiCheat));
    }

    public String createPunishmentId() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());

            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    public boolean punishmentIdExists(String id) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.history` WHERE punishmentId=?");
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            boolean result = rs.next();

            connection.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PunishData getPunishmentData(String id) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.history` WHERE punishmentId=?");
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            PunishData pData = new PunishData();
            if (rs.next()) {
                pData.setPunishmentId(rs.getString("punishmentId"));
                pData.setPlayer(rs.getString("punisher"));
                pData.setTarget(AccountHub.getInstance().nameFromUUID(UUID.fromString(rs.getString("uuid"))));
                pData.setReason(rs.getString("reason"));
                pData.setType(PunishType.valueOf(rs.getString("type")));
                pData.setDuration(PunishDuration.valueOf(rs.getString("durationType")));
                pData.setActive(Boolean.parseBoolean(rs.getString("active")));
                pData.setDate(rs.getString("date"));

                pData.setRemovedOn(rs.getString("removedOn"));
                pData.setRemovedBy(rs.getString("removedBy"));
                pData.setRemovedReason(rs.getString("removedReason"));
            }

            connection.close();
            return pData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<PunishData> playerPunishments(UUID uuid) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.history` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();

            ArrayList<PunishData> data = new ArrayList<>();

            while (rs.next()) {
                PunishData pData = new PunishData();
                pData.setPunishmentId(rs.getString("punishmentId"));
                pData.setPlayer(rs.getString("punisher"));
                pData.setTarget(AccountHub.getInstance().nameFromUUID(UUID.fromString(rs.getString("uuid"))));
                pData.setReason(rs.getString("reason"));
                pData.setType(PunishType.valueOf(rs.getString("type")));
                pData.setDuration(PunishDuration.valueOf(rs.getString("durationType")));
                pData.setActive(Boolean.parseBoolean(rs.getString("active")));
                pData.setDate(rs.getString("date"));

                pData.setRemovedOn(rs.getString("removedOn"));
                pData.setRemovedBy(rs.getString("removedBy"));
                pData.setRemovedReason(rs.getString("removedReason"));

                data.add(pData);
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getDuration(UUID uuid) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.active` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getLong("duration");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getReason(UUID uuid) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.active` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();

            String result = "";
            if (rs.next())
                result = rs.getString("reason");

            connection.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public PunishType getType(UUID uuid) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.active` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();

            PunishType result = null;
            if (rs.next())
                result = PunishType.valueOf(rs.getString("type"));

            connection.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPunisher(UUID uuid) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.active` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();

            String result = "";
            if (rs.next())
                result = rs.getString("punisher");

            connection.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isBanned(UUID uuid) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.active` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();

            boolean result;
            long duration = -1;
            String banId = "";
            if (rs.next()) {
                result = rs.getString("type").equalsIgnoreCase(PunishType.BAN.toString());
                duration = rs.getLong("duration");
                banId = rs.getString("punishmentId");
            } else
                result = false;

            if (duration < System.currentTimeMillis() && duration != -1 && !banId.equalsIgnoreCase("")) {
                removePunishment(banId, AccountHub.getInstance().nameFromUUID(uuid), "Exile Spray AC", "Punishment Expired [AUTO]");
                result = false;
            }

            connection.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removePunishment(String id, String target, String remover, String reason) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("DELETE FROM `punish.active` WHERE punishmentId=?");
            statement.setString(1, id);
            statement.executeUpdate();

            PreparedStatement sts = connection.prepareStatement("UPDATE `punish.history` SET active=?, removedOn=?, removedBy=?, removedReason=? WHERE punishmentId=?");
            sts.setString(1, "false");
            sts.setString(2, "" + java.time.LocalDate.now());
            sts.setString(3, remover);
            sts.setString(4, reason);
            sts.setString(5, id);

            sts.executeUpdate();
            connection.close();

            CloudNetDriver.getInstance().getMessenger().sendChannelMessage("cps", "punishmentRemoved", new JsonDocument()
            .append("player", remover)
            .append("target", target));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isBlacklisted(UUID uuid) {
        try {
            Connection connection = AccountHub.getInstance().createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * from `punish.active` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();

            boolean result;
            if (rs.next()) {
                result = rs.getString("type").equalsIgnoreCase(PunishType.BLACKLIST.toString());
            } else
                result = false;

            connection.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getDurationMessage(long duration) {

        if (duration == -1)
            return "Permanent";

        long remaining = (duration -  System.currentTimeMillis()) / 1000;

        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        while (remaining >= 86400) {
            days++;
            remaining -= 86400;
        }

        while (remaining >= 3600) {
            hours++;
            remaining -= 3600;
        }

        while (remaining >= 60) {
            minutes++;
            remaining -= 60;
        }

        while (remaining >= 1) {
            seconds++;
            remaining -= 1;
        }

        return
                (days != 0 ? days + " days, " : "") +
                (hours != 0 ? hours + " hours, " : "") +
                (minutes != 0 ? minutes + " minutes, " : "") +
                (seconds != 0 ? seconds + " seconds" : "");

    }

    public String generateBanMessage(UUID punished) {
        return "§r§c§lYou have been banned from this server!§r\n\n§7Reason §8» §f" + getReason(punished)
                + "§r\n§7Duration §8» §f" + getDurationMessage(getDuration(punished))
                + "§r\n§7Moderator §8» §f" + getPunisher(punished)
                + "§r\n\n§eYou can appeal your punishment by DMing Gabriella#6859";
    }

    public String generateBlacklistMessage(UUID punished) {
        return "§r§4§lYou have been blacklisted from this server!§r\n\n§7Reason §8» §f" + getReason(punished)
                + "§r\n§7Moderator §8» §f" + getPunisher(punished)
                + "§r\n\n§cThis punishment cannot be appealed.";
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        if (isBanned(uuid))
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, generateBanMessage(uuid));
        else if (isBlacklisted(uuid))
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, generateBlacklistMessage(uuid));
        else
            event.allow();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (getRemovingReason().containsKey(player)) {
            event.setCancelled(true);
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                player.sendMessage("§cCancelled!");
                getRemovingReason().remove(player);
            } else {
                removePunishment(getRemovingReason().get(player), getPunishmentData(getRemovingReason().get(player)).getTarget(), player.getName(), event.getMessage());
                player.sendMessage("§aSuccessfully removed punishment " + getRemovingReason().get(player));
                getRemovingReason().remove(player);
            }
        }
        if (getSettingReason().containsKey(player)) {
            PunishData data = getSettingReason().get(player);
            event.setCancelled(true);
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                player.sendMessage("§cCancelled!");
                getSettingReason().remove(player);
                Bukkit.getServer().getScheduler().runTask(getPlugin(), () -> {
                    BanGUI.openGUI(player, data);
                });
                getSwitching().remove(player);
            } else {
                getSettingReason().remove(player);
                data.setReason(event.getMessage());
                updateData(player, data);
                Bukkit.getServer().getScheduler().runTask(getPlugin(), () -> {
                    DurationGUI.openGUI(player, data);
                });
                getSwitching().remove(player);
            }

        }
    }
}
