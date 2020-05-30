package me.cps.root.util;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/*

so this event works using a runnables. every millisecond this event will fire. (almost, anyway)

potential usage:
this is so you can check for things like state changes to check for a game to end, etc.

 */

/**
 * Curious Productions Root
 * CPS Utilities - Per Milli Event
 *
 * Event that fires every 1 tick.
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-04-07
 */
public class PerMilliEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
