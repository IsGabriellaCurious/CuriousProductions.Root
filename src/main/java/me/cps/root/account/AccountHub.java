package me.cps.root.account;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.cpsModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class AccountHub extends cpsModule {

    /*
    Notes to self:
    execute query is for select statements
    execute update is for insert statements

    when returning:
    1 - success
    0 - failure
     */

    private HashMap<UUID, Rank> players; //basically the rank cache so i don't have the bother mysql for every chat message, etc.
    private static AccountHub instance; //instace of this

    public boolean nullBoolean; //basically a return null during the player exists check if there is an error
    public boolean serverLockdown = false; //a pointless security precaution. if you we can't connect to mysql, the server doesn't allow ANYONE into itself.

    //mysql's connection info
    private Connection connection;
    private String host;
    private String username;
    private String password;
    private String database;
    private int port;

    public AccountHub(JavaPlugin plugin, String h, String u, String pw, String db, int p) {
        super("Account Hub", plugin, true);
        this.host = h;
        this.username = u;
        this.password = pw;
        this.database = db;
        this.port = p;
        registerSelf();
        players = new HashMap<>();
        instance = this;

        int result = -1;
        plugin.getServer().getConsoleSender().sendMessage("§eAs a security measure, we are testing the SQL connection...");
        //a very basic check using try-catch. it tries to open a connection, if it fails it goes to the catch and locks the server down. else, it closes the connection and continues.
        try {
            openConnection();

            result = 1;
            connection.close();
            plugin.getServer().getConsoleSender().sendMessage("§aTest successful! Connection is closed and we are good to go.");
        } catch (Exception e) {
            plugin.getServer().getConsoleSender().sendMessage("§cERROR! Server is entering lockdown. See stack below...");
            e.printStackTrace();
            setLockdown(true);
            result = 0;
        }
        plugin.getLogger().info("Result " + result); //1 - success, 0 - failed, -1 - fuck knows what happened.
    }

    public Connection getConnection() {
        return connection;
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

    public void openConnection() throws SQLException, ClassNotFoundException {
        getPlugin().getLogger().info("Opening connection!");
        if (connection != null && ! connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password); //connection url
        }
    }

    //the reason this and loadPlayer uses integers as results and during the login checks, it can disallow the player to join if there was an error. (this is so nothing else breaks as they won't be in any caches)
    public int createPlayer(UUID uuid) {
        try {
            openConnection();

            getPlugin().getServer().getConsoleSender().sendMessage("§aCreating player with UUID §c" + uuid.toString());
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO `account` (uuid, rank) VALUE (?, ?)");
            statement.setString(1, uuid.toString());
            statement.setString(2, Rank.DEFAULT.toString());

            statement.executeUpdate();
            connection.close();

            getPlugin().getServer().getConsoleSender().sendMessage("§aSuccess!");
            return 1;
        } catch (Exception e) {
            getPlugin().getServer().getConsoleSender().sendMessage("§cError! See stack below...");
            e.printStackTrace();
            return 0;
        }
    }

    public int loadPlayer(UUID uuid) {
        try {
            openConnection();

            getPlugin().getServer().getConsoleSender().sendMessage("§aLoading player with UUID §c" + uuid.toString());
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM `account` WHERE uuid=?");
            statement.setString(1, uuid.toString());
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();

            Rank rank = null;

            if (resultSet.next()) {
                rank = Rank.valueOf(resultSet.getString("rank"));
                getPlayers().put(uuid, rank);
                connection.close();
                getPlugin().getServer().getConsoleSender().sendMessage("§aSuccess!");
                return 1;
            } else {
                connection.close();
                getPlugin().getServer().getConsoleSender().sendMessage("§cError! Result set empty!");
                return 0;
            }
        } catch (Exception e) {
            getPlugin().getServer().getConsoleSender().sendMessage("§cError! See stack below...");
            e.printStackTrace();
            return 0;
        }
    }

    public boolean playerExists(UUID uuid) {
        try {
            openConnection();

            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM `account` WHERE uuid=?");
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

    //all the events! also pretty self-explanatory.
    //NOTE TO SELF: ALWAYS PUT A RESET (ChatColor.REST or §r) BEFORE THE NEW LINE (\n) AS IT WONT CENTER THE MESSAGE OTHERWISE!!

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent event) {
        if (serverLockdown) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "\n\n§b§lCPS§r\n§cServer lockdown enabled.§r\n§cPlease try again later.");
            return;
        }

        if (playerExists(event.getUniqueId())) {
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
        getPlugin().getLogger().info("Added player " + event.getName() + " to the rank cache.");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        getPlayers().remove(event.getPlayer().getUniqueId());
        getPlugin().getLogger().info("Removed player " + event.getPlayer().getName() + " from the rank cache.");
    }
}
