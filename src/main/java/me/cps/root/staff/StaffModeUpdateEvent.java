package me.cps.root.staff;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Curious Productions Root
 * Staff Hub - Staff Mode Update Event
 *
 * Event that fires when a staff members toggles in and out of staff mode.
 *
 * @author  Gabriella Hotten
 * @since   2020-04-11
 */
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
