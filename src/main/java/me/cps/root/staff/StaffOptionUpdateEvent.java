package me.cps.root.staff;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//things like game manager and hub should listen for these events and handle the event individually

public class StaffOptionUpdateEvent extends Event {

    private Player player;
    private StaffOptions option;
    private boolean result;
    private boolean override;

    public StaffOptionUpdateEvent(Player player, StaffOptions option, boolean result, boolean override) {
        this.player = player;
        this.option = option;
        this.result = result;
        this.override = override;
    }

    public Player getPlayer() {
        return player;
    }

    public StaffOptions getOption() {
        return option;
    }

    public boolean isOptionEnabled() {
        return result;
    }

    public boolean isOverride() {
        return override;
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
