package me.cps.root.command;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.command.commands.ModulesEnabledCommand;
import me.cps.root.command.commands.TestCommand;
import me.cps.root.cpsModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandHub extends cpsModule {

    private HashMap<String, cpsCommand> registeredCommands; //useable commands, command hub will use this to check if the command exists.
    private static CommandHub instance; //obvious, the instance.

    private List<String> illegalCommands = Arrays.asList("plugins", "pl", "ver", "version", "me", "minecraft:me"); //a list of commands that people below developer can't use.

    public CommandHub(JavaPlugin plugin) {
        super("Command Hub", plugin, true);

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
        getPlugin().getServer().getConsoleSender().sendMessage("§fRegistered command " + command.getCommand());
        for (Object al : command.getAliases()) {
            registeredCommands.put(al.toString(), command);
            getPlugin().getServer().getConsoleSender().sendMessage("§fRegistered alias " + al + " [" + command.getCommand() + "]");
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) { //commandpreprocess is basically before the command is executed.
        String requestedCommand = event.getMessage().substring(1).split(" ")[0]; //grabs the command from the whole bit entered.
        String[] args = removeCommand(event.getMessage()); //the args!

        //this is the illegal command check (see the illegalCommands list above)
        if (illegalCommands.contains(requestedCommand)) {
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
                event.getPlayer().sendMessage("§cError! You do not have permission to run this command.");
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
