package me.cps.root;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.command.CommandHub;
import me.cps.root.command.cpsCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class cpsModule implements Listener {

    String moduleName = "NOT SET"; //name of the module (for example what will be found in the /listmodules command
    JavaPlugin plugin; // easy access to the main class's shit and everything inbetween.
    Boolean core; //TODO actually make it so you can disable non-core modules

    public cpsModule(String name, JavaPlugin plugin, Boolean core) {
        this.moduleName = name;
        this.plugin = plugin;
        this.core = core;
        if (core)
            plugin.getServer().getConsoleSender().sendMessage("§a**CORE** Module " + name + " is being loaded...");
        else
            plugin.getServer().getConsoleSender().sendMessage("§bModule " + name + " is being loaded...");
        Root.getModulesEnabled().add(this);
    }


    public String getModuleName() {
        return moduleName;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Boolean isCore() {
        return core;
    }

    public void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    public void registerSelf() { //this is so you can make the class that extends cpsModule a listener and don't bother having to really register it or implement listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void registerCommand(cpsCommand command) { //just a quicker way of registering a cpsCommand, rather than accessing the command hub, etc, etc
        CommandHub.getInstance().registerCommand(command);
    }

}
