package me.cps.root.punish.gui;

import me.cps.root.punish.PunishData;
import me.cps.root.punish.PunishDuration;
import me.cps.root.punish.PunishManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Curious Productions Root
 * Punish Manager - Duration GUI
 *
 * Handles the opening of the Duration menu.
 *
 * @author  Gabriella Hotten
 * @since   2020-05-10
 */
public class DurationGUI {

    public static void openGUI(Player player, PunishData data) {
        PunishManager.getInstance().getSwitching().remove(player);

        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§cSelect a duration");

        int current = PunishDuration.values().length-1;
        for (PunishDuration pd : PunishDuration.values()) {
            ItemStack itemStack = new ItemStack(Material.PAPER, 1);
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(pd.getDisplayName());
            itemStack.setItemMeta(itemMeta);
            inv.setItem(current, itemStack);

            current -= 1;
        }

        player.openInventory(inv);
    }


}
