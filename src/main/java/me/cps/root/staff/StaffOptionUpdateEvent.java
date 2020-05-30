package me.cps.root.staff;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Curious Productions Root
 * Staff Hub
 *
 * Event that fires when a staff member toggles a Staff Mode Option (e.g. vanish)
 * NOTICE: things like game manager and hub should listen for these events and handle the event individually
 *
 * @author  Gabriella Hotten
 * @version 1.2
 * @since   2020-04-11
 */
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
