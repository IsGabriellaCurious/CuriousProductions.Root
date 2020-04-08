package me.cps.root;

import me.cps.root.account.AccountHub;
import org.bukkit.ChatColor;

import java.util.UUID;

public enum Rank {

    OWNER("Owner", ChatColor.AQUA, "[OWNER]", true),
    DEVELOPER("Developer", ChatColor.YELLOW, "[DEV]", false),

    ADMIN("Admin", ChatColor.DARK_RED, "[ADMIN]", false),
    JUNIORADMIN("Junior Admin", ChatColor.RED, "[JUNIOR ADMIN]", false),

    SENIORMOD("Senior Mod", ChatColor.BLUE, "[SENIOR MOD]", false),
    MODERATOR("Moderator", ChatColor.BLUE, "[MOD]", false),
    HELPER("Helper", ChatColor.DARK_PURPLE, "[HELPER]", false),

    FAMOUS("VIP", ChatColor.LIGHT_PURPLE, "[VIP]", true),

    DONATORPLUS("Donator+", ChatColor.DARK_AQUA, "[DONATOR+]", false),
    DONATOR("Donator", ChatColor.GOLD, "[DONATOR]", false),

    DEFAULT("Default", ChatColor.GRAY, "", false);

    private String name; //actual rank's name (NOT PREFIX). this will be used in the scoreboard at a later date.
    private ChatColor color; //the colour of the prefix
    private String prefix; //the prefix in chat and tab (etc)
    private boolean changeTag; //will they be able to change what their tag says (soon)

    private Rank (String name, ChatColor color, String prefix, boolean changeTag) {
        this.name = name;
        this. color = color;
        this.prefix = prefix;
        this.changeTag = changeTag;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getPrefix() {
        if (prefix == "")
            return ChatColor.GRAY + "";

        return color + prefix + " "; //TODO support for the changeTag feature
    }

    public boolean isChangeTag() {
        return changeTag;
    }

    public static Rank getRank(UUID uuid) {
        return AccountHub.getInstance().getPlayers().get(uuid);
    }

    //goes straight to mysql. should only be used in prejoin events when the player is most likely not in the rank cache yet
    public static Rank forceGetRank(UUID uuid) {
        return AccountHub.getInstance().forceGetRank(uuid);
    }

    public static boolean hasRank(UUID uuid, Rank rank) {
        return getRank(uuid).compareTo(rank) <= 0;
    }

    public static boolean forceHasRank(UUID uuid, Rank rank) {
        return forceGetRank(uuid).compareTo(rank) <= 0;
    }

    //this will be used for tab list positioning (as you know, tab lists work via numeric/alphabetical order)
    public static String getLevel(Rank r) {
        switch (r) {
            case OWNER:
                return "1";
            case DEVELOPER:
                return "2";
            case ADMIN:
                return "3";
            case JUNIORADMIN:
                return "4";
            case SENIORMOD:
                return "5";
            case MODERATOR:
                return "6";
            case HELPER:
                return "7";
            case FAMOUS:
                return "A";
            case DONATORPLUS:
                return "B";
            case DONATOR:
                return "C";
            case DEFAULT:
                return "Z";
            default:
                return null;

        }
    }

    public String getLevel() {
        return getLevel(this);
    }
}
