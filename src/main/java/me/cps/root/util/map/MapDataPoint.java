package me.cps.root.util.map;

import org.bukkit.ChatColor;

/**
 * Curious Productions Root
 * CPS Utilities - Map Data Point
 *
 * Base for all data points in CPS Game Manager maps.
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-05-24
 */
public class MapDataPoint
{
    public MapDataPoint(String type, ChatColor colour, String x, String y, String z, String extraInfo)
    {
        this.type = type;
        this.colour = colour;
        this.x = Integer.parseInt(x);
        this.y = Integer.parseInt(y);
        this.z = Integer.parseInt(z);
        this.extraInfo = extraInfo;
    }

    public MapDataPoint() {}


    private String type;
    private ChatColor colour;
    private Integer x;
    private Integer y;
    private Integer z;
    private String extraInfo;

    public String getType() {
        return type;
    }

    public ChatColor getColour() {
        return colour;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public String getExtraInfo() {
        return extraInfo;
    }
}
