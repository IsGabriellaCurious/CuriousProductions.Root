package me.cps.root.punish;

/**
 * Curious Productions Root
 * Punish Manager - Punish Duration Enum
 *
 * Types of duration a player can receive on their punishment.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-09
 */
public enum PunishDuration {

    PERMANENT(-1, "Permanent"),
    THREEMONTHS(7889238, "3 Months"),
    ONEMONTH(2629746, "1 Month"),
    ONEWEEK(604800, "1 Week"),
    ONEDAY(86400, "1 Day");


    private long seconds;
    private String displayName;

    private PunishDuration(long seconds, String displayName) {
        this.seconds = seconds;
        this.displayName = displayName;
    }

    public long getSeconds() {
        return seconds;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PunishDuration parseFromDisplayName(String name) {
        switch (name) {
            case "Permanent":
                return PunishDuration.PERMANENT;
            case "3 Months":
                return PunishDuration.THREEMONTHS;
            case "1 Month":
                return PunishDuration.ONEMONTH;
            case "1 Week":
                return PunishDuration.ONEWEEK;
            case "1 Day":
                return PunishDuration.ONEDAY;
            default:
                return null;
        }
    }
}
