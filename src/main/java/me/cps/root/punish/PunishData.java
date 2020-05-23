package me.cps.root.punish;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

public class PunishData {

    private String punishmentId;
    private String player;
    private String target;
    private String reason;
    private PunishType type;
    private PunishDuration duration;
    private boolean active;
    private String date;

    private String removedOn;
    private String removedBy;
    private String removedReason;

    public void setPunishmentId(String punishmentId) {
        this.punishmentId = punishmentId;
    }

    public String getPunishmentId() {
        return punishmentId;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayer() {
        return player;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setType(PunishType type) {
        this.type = type;
    }

    public PunishType getType() {
        return type;
    }

    public void setDuration(PunishDuration duration) {
        this.duration = duration;
    }

    public PunishDuration getDuration() {
        return duration;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setRemovedOn(String removedOn) {
        this.removedOn = removedOn;
    }

    public String getRemovedOn() {
        return removedOn;
    }

    public void setRemovedBy(String removedBy) {
        this.removedBy = removedBy;
    }

    public String getRemovedBy() {
        return removedBy;
    }

    public void setRemovedReason(String removedReason) {
        this.removedReason = removedReason;
    }

    public String getRemovedReason() {
        return removedReason;
    }
}
