package me.cps.root.staff;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StaffModeUpdateEvent extends Event {

    private Player player;
    private boolean toggle;

    public StaffModeUpdateEvent(Player player, boolean toggle) {
        this.player = player;
        this.toggle = toggle;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isToggle() {
        return toggle;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
