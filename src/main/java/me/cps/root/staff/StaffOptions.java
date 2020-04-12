package me.cps.root.staff;

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
