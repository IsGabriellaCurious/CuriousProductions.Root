package me.cps.root.account;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.account.commands.SetRankCommand;
import me.cps.root.cpsModule;
import me.cps.root.redis.RedisHub;
import me.cps.root.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class AccountHub extends cpsModule {

    /*
    Notes to self:
    execute query is for select statements
    execute update is for insert, update statements

    when returning:
    1 - success
    0 - failure
     */

    private HashMap<UUID, Rank> players; //basically the rank cache so i don't have the bother mysql for every chat message, etc.
    private static AccountHub instance; //instance of this

    public boolean nullBoolean; //basically a return null during the player exists check if there is an error
    public boolean serverLockdown = false; //a pointless security precaution. if you we can't connect to mysql, the server doesn't allow ANYONE into itself.

    //mysql's connection info
    //private Connection connection;
    private String host;
    private String username;
    private String password;
    private String database;
    private int port;

    public AccountHub(JavaPlugin plugin, String h, String u, String pw, String db, int p) {
        super("Account Hub", plugin, "1.2", true);
        this.host = h;
        this.username = u;
        this.password = pw;
        this.database = db;
        this.port = p;
        registerSelf();
        players = new HashMap<>();
        instance = this;

        int result = -1;
        Message.console("§eAs a security measure, we are testing the SQL connection...");
        //a very basic check using try-catch. it tries to open a connection, if it fails it goes to the catch and locks the server down. else, it closes the connection and continues.
        try {
            Connection conn = createConnection();

            result = 1;
            conn.close();
            Message.console("§aTest successful! Connection is closed and we are good to go.");
        } catch (Exception e) {
            Message.console("§cERROR! Server is entering lockdown. See stack below...");
            e.printStackTrace();
            setLockdown(true);
            result = 0;
        }
        plugin.getLogger().info("Result " + result); //1 - success, 0 - failed, -1 - fuck knows what happened.

        registerCommand(new SetRankCommand(this));
    }

    public static AccountHub getInstance() {
        return instance;
    }

    public HashMap<UUID, Rank> getPlayers() {
        return players;
    }

    public void setLockdown(boolean lockdown) { //sets the lockdown boolean to true and kicks any people that managed to join before the server was ready for them
        if (lockdown) {
            this.serverLockdown = true;
            for (Player p : getPlugin().getServer().getOnlinePlayers()) {
                p.kickPlayer("\n\n§b§lCPS\n§cServer lockdown enabled.");
            }
            getPlugin().getServer().getConsoleSender().sendMessage("§cLockdown enabled.");
        } else {
            this.serverLockdown = false;
            getPlugin().getServer().getConsoleSender().sendMessage("§aLockdown disabled.");
        }
    }


    //now for all the mysql crap. most of it is pretty self-explanitory so i won't bother explain most of it.

    public Connection createConnection() throws SQLException, ClassNotFoundException {

        synchronized (this) {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password); //connection url
            return conn;
        }
    }

    //the reason this and loadPlayer uses integers as results and during the login checks, it can disallow the player to join if there was an error. (this is so nothing else breaks as they won't be in any caches)
    public int createPlayer(UUID uuid) {
        try {
            Connection connection = createConnection();

            Message.console("§aCreating player with UUID §c" + uuid.toString());
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `account` (uuid, rank) VALUE (?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, Rank.DEFAULT.toString());

            statement.executeUpdate();
            connection.close();

            Message.console("§aSuccess!");
            return 1;
        } catch (Exception e) {
            Message.console("§cError! See stack below...");
            e.printStackTrace();
            return 0;
        }
    }

    public int loadPlayer(UUID uuid) {
        try {
            Connection connection = createConnection();

            Message.console("§aLoading player with UUID §c" + uuid.toString());
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `account` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            Rank rank = null;

            if (resultSet.next()) {
                rank = Rank.valueOf(resultSet.getString("rank"));
                getPlayers().put(uuid, rank);
                connection.close();
                Message.console("§aSuccess!");
                return 1;
            } else {
                connection.close();
                Message.console("§cError! Result set empty!");
                return 0;
            }
        } catch (Exception e) {
            Message.console("§cError! See stack below...");
            e.printStackTrace();
            return 0;
        }
    }

    public Rank forceGetRank(UUID uuid) {
        try {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT `rank` FROM `account` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            Rank r = null;
            if (resultSet.next())
                r = Rank.valueOf(resultSet.getString("rank"));
            else
                r = Rank.DEFAULT;

            connection.close();
            return r;

        } catch (Exception e) {
            e.printStackTrace();
            return Rank.DEFAULT;
        }
    }

    public String nameFromUUID(UUID uuid) {
        try {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT `name` FROM `account` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            String result = null;
            if (resultSet.next())
                result = resultSet.getString("name");
            else
                result = "";

            connection.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UUID uuidFromName(String name) {
        try {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT `uuid` FROM `account` WHERE name=?");
            statement.setString(1,name);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            UUID result = null;
            if (resultSet.next())
                result = UUID.fromString(resultSet.getString("uuid"));

            connection.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean playerExists(UUID uuid) {
        try {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `account` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            boolean exists = resultSet.next();
            connection.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return nullBoolean;
        }
    }

    public int updateRank(UUID uuid, Rank rank) {
        try {
            Message.console("§aPreparing to update " + uuid.toString() + "'s rank to " + rank.toString());
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("UPDATE `account` SET rank=? WHERE uuid=?");
            statement.setString(1, rank.toString());
            statement.setString(2, uuid.toString());

            Message.console("§aUpdating...");
            statement.executeUpdate();

            connection.close();
            Message.console("§aSuccess!");
            return 1;
        } catch (Exception e) {
            Message.console("§cError! See stack trace below...");
            e.printStackTrace();
            return 0;
        }
    }

    //all the events! also pretty self-explanatory.
    //NOTE TO SELF: ALWAYS PUT A RESET (ChatColor.REST or §r) BEFORE THE NEW LINE (\n) AS IT WONT CENTER THE MESSAGE OTHERWISE!!

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        if (serverLockdown) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n\n§b§lCPS§r\n§cServer lockdown enabled.§r\n§cPlease try again later.");
            return;
        }

        if (getPlugin().getServer().getOnlinePlayers().size() >= RedisHub.getInstance().maxPlayers && !RedisHub.getInstance().gameServer) {
            if (!RedisHub.getInstance().donatorPriority) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "\n\n§b§lCPS§r\n§cThis server is full. Please try again later.");
                return;
            }
        }

        boolean exists = playerExists(event.getUniqueId());

        if (exists) {
            int result = loadPlayer(event.getUniqueId());
            if (result == 0) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n\n§b§lCPS§r\n§cThere was an error loading your account. Please try again.§r\n\n§fIf the issue continues, please contact an " + Rank.ADMIN.getPrefix());
                return;
            }
        } else {
            int result = createPlayer(event.getUniqueId());
            if (result == 0) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n\n§b§lCPS§r\n§cThere was an error creating your account. Please try again.§r\n\n§fIf the issue continues, please contact an " + Rank.ADMIN.getPrefix());
                return;
            } else {
                loadPlayer(event.getUniqueId());
            }
        }

        if (!Rank.hasRank(event.getUniqueId(), RedisHub.getInstance().rankRequired)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n\n§b§lCPS§r\n§cYou don't have the required rank to join this server!§r\n§fYou need " + RedisHub.getInstance().rankRequired.getPrefix() + "§fto join.");
            getPlayers().remove(event.getUniqueId());
            return;
        }

        getPlugin().getLogger().info("Added player " + event.getName() + " to the rank cache.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Rank.hasRank(event.getPlayer().getUniqueId(), Rank.JUNIORADMIN)) {
            if (!event.getPlayer().isOp())
                event.getPlayer().setOp(true);
        }
        try {
            Connection connection = createConnection();

            PreparedStatement statement = connection.prepareStatement("UPDATE `account` SET name=? WHERE uuid=?");
            statement.setString(1, event.getPlayer().getName());
            statement.setString(2, event.getPlayer().getUniqueId().toString());

            statement.executeUpdate();

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        getPlayers().remove(event.getPlayer().getUniqueId());
        getPlugin().getLogger().info("Removed player " + event.getPlayer().getName() + " from the rank cache.");
    }
}
