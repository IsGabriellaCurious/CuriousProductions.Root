package me.cps.root.test;

import me.cps.root.util.cpsModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Curious Productions Root
 * Test Module
 *
 * Removed: 2020-04-05
 * Reason: First ever module.
 *
 * @author  Gabriella Hotten
 * @version 1.0-alpha
 * @since   2020-04-03
 */
@Deprecated
public class TestModule extends cpsModule {

    public TestModule(JavaPlugin plugin) {
        super("Test", plugin, "1.0-alpha", false);
    }

}
