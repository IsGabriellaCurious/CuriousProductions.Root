package me.cps.root.staff;

/**
 * Curious Productions Root
 * Staff Hub - Staff (Mode) Options Enum
 *
 * All available options for staff to toggle in staff mode
 *
 * @author  Gabriella Hotten
 * @since   2020-04-11
 */
public enum StaffOptions {

    Vanish("vanish"),
    GameChat("gamechat");
    //GameReview("gamereview");

    private String redisName;

    private StaffOptions(String redisName) {
        this.redisName = redisName;
    }

    public String getRedisName() {
        return redisName;
    }
}
