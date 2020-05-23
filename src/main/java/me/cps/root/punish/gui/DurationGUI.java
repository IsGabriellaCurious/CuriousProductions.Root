package me.cps.root.punish.gui;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.account.AccountHub;
import me.cps.root.punish.PunishData;
import me.cps.root.punish.PunishDuration;
import me.cps.root.punish.PunishManager;
import me.cps.root.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
