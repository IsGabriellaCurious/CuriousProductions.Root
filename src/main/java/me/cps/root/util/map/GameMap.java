package me.cps.root.util.map;

import java.util.List;

/**
 * Curious Productions Root
 * CPS Utilities - Game Map
 *
 * Base for all CPS Game Manager based maps.
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-05-24
 */
public class GameMap
{
    public GameMap(String name, String builder, List<MapDataPoint> datapoints)
    {
        this.name = name;
        this.builder = builder;
        this.datapoints = datapoints;
    }

    public GameMap() {}

    private String name;
    private String builder;
    private List<MapDataPoint> datapoints;

    public String getName() {
        return name;
    }

    public String getBuilder() {
        return builder;
    }

    public List<MapDataPoint> getDatapoints() {
        return datapoints;
    }
}
