package me.cps.root.punish;

/**
 * Curious Productions Root
 * Punish Manager - Punish Type Enum
 *
 * Types of punishment a player can receive.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-09
 */
public enum PunishType {

    WARN("warned"), BAN("banned"), MUTE("muted"), BLACKLIST("blacklisted");

    private String message;

    private PunishType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
