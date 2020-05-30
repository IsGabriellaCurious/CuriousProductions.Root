package me.cps.root.command;

import me.cps.root.util.Rank;
import me.cps.root.command.commands.ModulesEnabledCommand;
import me.cps.root.util.cpsModule;
import me.cps.root.util.CPSProtocol;
import me.cps.root.util.Message;
import me.cps.root.util.PerMilliRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Curious Productions Root
 * Command Hub
 *
 * Command Hub manages all server commands that are related to CPS plugins.
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-04-03
 */
public class CommandHub extends cpsModule {

    private HashMap<String, cpsCommand> registeredCommands; //useable commands, command hub will use this to check if the command exists.
    private static CommandHub instance; //obvious, the instance.

    private List<String> illegalCommands = Arrays.asList("plugins", "pl",  "bukkit:plugins", "bukkit:pl", "ver", "version", "bukkit:ver", "bukkit:version", "me", "minecraft:me", "help", "?", "bukkit:help", "bukkit:?", "ban", "banip", "mute", "warn", "pardon", "pardonip", "aac", "aac:aac"); //a list of commands that people below developer can't use.

    public static ArrayList<cpsModule> modulesEnabled = new ArrayList<>();


    public CommandHub(JavaPlugin plugin, boolean mili) {
        super("Command Hub", plugin, "1.0",true);
        CPSProtocol.setup();

        if (mili)
            plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new PerMilliRunnable(plugin), 0, 1);

        registeredCommands = new HashMap<>();
        registerSelf();
        instance = this;
        //this.registerCommand(new TestCommand(this));
        this.registerCommand(new ModulesEnabledCommand(this));
    }

    public static CommandHub getInstance() {
        return instance;
    }

    public HashMap<String, cpsCommand> getRegisteredCommands() {
        return registeredCommands;
    }

    public void registerCommand(cpsCommand command) { //basically puts a command into the registeredCommands (mostly used in cpsModule class)
        registeredCommands.put(command.getCommand(), command);
        Message.console("§fRegistered command " + command.getCommand());
        for (Object al : command.getAliases()) {
            registeredCommands.put(al.toString(), command);
            Message.console("§fRegistered alias " + al + " [" + command.getCommand() + "]");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) { //commandpreprocess is basically before the command is executed.
        String requestedCommand = event.getMessage().substring(1).split(" ")[0]; //grabs the command from the whole bit entered.
        String[] args = removeCommand(event.getMessage()); //the args!

        //this is the illegal command check (see the illegalCommands list above)
        if (illegalCommands.contains(requestedCommand.toLowerCase())) {
            if (!Rank.hasRank(event.getPlayer().getUniqueId(), Rank.DEVELOPER)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cError! That command has been disabled.");
                return;
            }
        }

        //checks the registeredCommands list to see if the command entered is valid.
        if (registeredCommands.containsKey(requestedCommand)) {
            event.setCancelled(true); //MAKE SURE TO CANCEL!!!!!
            cpsCommand command = registeredCommands.get(requestedCommand);
            if (!Rank.hasRank(event.getPlayer().getUniqueId(), command.getRankRequired())) { //permission check
                if (Rank.hasRank(event.getPlayer().getUniqueId(), Rank.HELPER)) //just makes more sense for staff to know, i guess.
                    event.getPlayer().sendMessage("§cError! You need the rank " + command.getRankRequired().getPrefix() + "§cto run this.");
                else
                    event.getPlayer().sendMessage("§cError! You don't have permission to use this.");
                return;
            }
            command.execute(event.getPlayer(), args); //runs the abstract void from cpsCommand.
        }
    }

    //thanks Nick
    //dont ask me to explain this i have no fucking clue
    private String[] removeCommand(String command)
    {
        String[] split = command.split(" ");

        if (split.length == 1)
        {
            return new String[0];
        }

        String[] result = new String[split.length - 1];
        System.arraycopy(split, 1, result, 0, split.length - 1);

        return result;
    }


}
