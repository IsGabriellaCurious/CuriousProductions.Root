package me.cps.root.util;

import me.cps.root.account.AccountHub;
import org.bukkit.ChatColor;

import java.util.UUID;

/**
 * Curious Productions Root
 * CPS Utilities - Rank Enum
 *
 * Holds all the available ranks.
 * TODO: remove all the IA shit
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-04-03
 */
public enum Rank {

    OWNERHC("Ex-IA High Command", ChatColor.LIGHT_PURPLE, "[IA HC]", true),
    OWNER("Owner", ChatColor.AQUA, "[OWNER]", true),
    DEVELOPER("Developer", ChatColor.YELLOW, "[DEV]", false),

    ADMIN("Admin", ChatColor.DARK_RED, "[ADMIN]", false),
    JUNIORADMIN("Junior Admin", ChatColor.RED, "[JUNIOR ADMIN]", false),

    IAHCVET("Ex-IA High Command", ChatColor.DARK_AQUA, "[IA HC]", false),

    SENIORMOD("Senior Mod", ChatColor.BLUE, "[SENIOR MOD]", false),
    MODERATOR("Moderator", ChatColor.BLUE, "[MOD]", false),
    HELPER("Helper", ChatColor.DARK_PURPLE, "[HELPER]", false),

    IAHIGHVET("Ex-IA High Rank", ChatColor.BLUE, "[IA HR]", false),
    IAMIDVET("Ex-IA Mid Rank", ChatColor.DARK_BLUE, "[IA MR]", false),
    IAVET("IA Veteran", ChatColor.DARK_BLUE, "[IA VET]", false),

    VIP("VIP", ChatColor.LIGHT_PURPLE, "[VIP]", true),
    BUILDER("Builder", ChatColor.LIGHT_PURPLE, "[BUILDER]", false),

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
            case OWNERHC:
                return "1";
            case OWNER:
                return "1";
            case DEVELOPER:
                return "2";
            case ADMIN:
                return "3";
            case JUNIORADMIN:
                return "4";
            case IAHCVET:
                return "5";
            case SENIORMOD:
                return "6";
            case MODERATOR:
                return "7";
            case HELPER:
                return "8";
            case IAHIGHVET:
                return "9";
            case IAMIDVET:
                return "A";
            case IAVET:
                return "B";
            case VIP:
                return "C";
            case BUILDER:
                return "D";
            case DONATORPLUS:
                return "E";
            case DONATOR:
                return "F";
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
