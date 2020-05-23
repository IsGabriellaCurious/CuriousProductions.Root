package me.cps.root.punish;

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
