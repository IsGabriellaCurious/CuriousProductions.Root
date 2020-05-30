package me.cps.root.util.map;

import java.util.List;

/**
 * Curious Productions Root
 * CPS Utilities - Game Map Base
 *
 * Base for all maps in a CPS Game Manager game.
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-05-24
 */
public class GameMapBase
{
    public GameMapBase(List<String> maps)
    {
        this.maps = maps;
    }

    public GameMapBase() {}

    private List<String> maps;

    public List<String> getMaps()
    {
        return maps;
    }
}
