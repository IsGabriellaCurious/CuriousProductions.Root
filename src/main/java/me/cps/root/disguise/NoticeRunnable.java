package me.cps.root.disguise;

import me.cps.root.util.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Curious Productions Root
 * Disguise Manager - Notice Runnable
 *
 * Sends an action bar to all disguised playing reminding them they are disguised.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-28
 */
public class NoticeRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (DisguiseManager.getInstance().getDisguised().containsKey(p.getName())) {
                ActionBar.send(p, "§cNotice: §fYou are currently §6Disguised");
            }
        }
    }
}
