package me.cps.root.punish.gui;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.punish.PunishData;
import me.cps.root.punish.PunishManager;
import me.cps.root.punish.PunishType;
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

import java.util.ArrayList;

public class BanGUI {

    public static void openGUI(Player player, PunishData data) {
        data.setType(PunishType.BAN);

        PunishManager.getInstance().updateData(player, data);
        PunishManager.getInstance().getSwitching().remove(player);

        Inventory inv = Bukkit.getServer().createInventory(null, 9, "§cSelect a reason");

        //Illegal Client Modifications
        ItemStack client = new ItemStack(Material.PAPER, 1);
        ItemMeta clientMeta = client.getItemMeta();

        clientMeta.setDisplayName("Illegal Client Modifications");
        client.setItemMeta(clientMeta);
        inv.setItem(0, client);

        //Verbal Abuse
        ItemStack verbal = new ItemStack(Material.PAPER, 1);
        ItemMeta verbalMeta = verbal.getItemMeta();

        verbalMeta.setDisplayName("Verbal Abuse");
        verbal.setItemMeta(verbalMeta);
        inv.setItem(1, verbal);

        //Admin Abuse
        ItemStack abuse = new ItemStack(Material.PAPER, 1);
        ItemMeta abuseMeta = abuse.getItemMeta();

        abuseMeta.setDisplayName("Admin Abuse");
        abuse.setItemMeta(abuseMeta);
        inv.setItem(2, abuse);

        //Other
        ItemStack other = new ItemStack(Material.PAPER, 1);
        ItemMeta otherMeta = other.getItemMeta();
        ArrayList<String> otherLore = new ArrayList<>();

        otherMeta.setDisplayName("Other");
        otherLore.add("§8You will be asked to enter a reason.");
        otherMeta.setLore(otherLore);
        other.setItemMeta(otherMeta);
        inv.setItem(8, other);

        player.openInventory(inv);
    }


}
