package me.cps.root;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.command.CommandHub;
import me.cps.root.command.cpsCommand;
import me.cps.root.util.Message;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class cpsModule implements Listener {

    String moduleName = "NOT SET"; //name of the module (for example what will be found in the /listmodules command
    JavaPlugin plugin; // easy access to the main class's shit and everything inbetween.
    Boolean core; //TODO actually make it so you can disable non-core modules
    String version = "0";

    public cpsModule(String name, JavaPlugin plugin, String version, Boolean core) {
        this.moduleName = name;
        this.plugin = plugin;
        this.version = version;
        this.core = core;
        if (core)
            Message.console("§a**CORE** Module " + name + " §dv" + version + " §ais being loaded...");
        else
            Message.console("§bModule " + name + " §dv" + version + " §ais being loaded...");
        CommandHub.modulesEnabled.add(this);
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

    public String getVersion() {
        return version;
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
