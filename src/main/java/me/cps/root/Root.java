package me.cps.root;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020


import de.dytanic.cloudnet.driver.service.ServiceConfiguration;
import de.dytanic.cloudnet.driver.service.ServiceId;
import de.dytanic.cloudnet.wrapper.Wrapper;
import me.cps.root.account.AccountHub;
import me.cps.root.chat.ChatHub;
import me.cps.root.command.CommandHub;
import me.cps.root.proxy.ProxyManager;
import me.cps.root.redis.RedisHub;
import me.cps.root.scoreboard.ScoreboardCentre;
import me.cps.root.test.TestMod2;
import me.cps.root.test.TestModule;
import me.cps.root.util.Message;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Root extends JavaPlugin {

    private static ArrayList<cpsModule> modulesEnabled;
    private static Root instance;

    @Override
    public void onEnable() {
        instance = this;
        modulesEnabled = new ArrayList<>();

        //this just shows how to initialize cps modules
        // !!! ALWAYS INITIALIZE COMMAND HUB FIRST !!! - else it will nullpointerexception if you try to register a command.
        CommandHub commandHub = new CommandHub(this, true);
        RedisHub redisHub = new RedisHub(this, null, Rank.DEFAULT, 100, false, false);
        AccountHub accountHub = new AccountHub(this, "ssh.centurion.pw", "cps", "L2ZBcOxTQEvvz8zW", "cps", 3306);
        ProxyManager proxyManager = new ProxyManager(this);
        ChatHub chatHub = new ChatHub(this, true);
        ScoreboardCentre scoreboardCentre = new ScoreboardCentre(this);

        TestModule testModule = new TestModule(this);
        TestMod2 testMod2 = new TestMod2(this);

        getServer().getConsoleSender().sendMessage("§a§lStarted!");

        String t = "Modules enabled: ";
        for (int i = 0; i < modulesEnabled.size(); i++) { //for every one in modulesEnabled. probably not the best way of doing it, but oh well.
            if (i == modulesEnabled.size()-1)
                t = t + modulesEnabled.get(i).getModuleName();
            else
                t = t + modulesEnabled.get(i).getModuleName() + ", ";
        }
        getLogger().info(t);

        ServiceId serviceId = Wrapper.getInstance().getServiceId();

        Message.console(serviceId.getName() + " " + serviceId.getTaskName());
    }

    @Override
    public void onDisable() {
        RedisHub.getInstance().deInitServer(); //removes the server from the online servers list.
        getLogger().info("ded"); //there is honestly no point to this
    }

    public static ArrayList<cpsModule> getModulesEnabled() { //so it can be accessed by other classes. used mainly by cpsModule
        return modulesEnabled;
    }

    public static Root getInstance() {
        return instance;
    }
}
*/