package me.cps.root.punish.gui;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import me.cps.root.Rank;
import me.cps.root.account.AccountHub;
import me.cps.root.punish.PunishData;
import me.cps.root.punish.PunishManager;
import me.cps.root.punish.PunishType;
import me.cps.root.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
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

public class PunishGUI {

    public static void openGUI(Player player, String target) {
        Inventory inv = Bukkit.getServer().createInventory(null, 27, "§cPunish " + target);

        //ban
        ItemStack ban = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
        ItemMeta banMeta = ban.getItemMeta();
        ArrayList<String> banLore = new ArrayList<>();

        banMeta.setDisplayName("§cBan " + target);
        banLore.add("§eClick to select a reason and duration to ban " + target);
        banMeta.setLore(banLore);
        ban.setItemMeta(banMeta);
        inv.setItem(3, ban);

        //mute
        ItemStack mute = new ItemStack(Material.STAINED_CLAY, 1, (short) 11);
        ItemMeta muteMeta = mute.getItemMeta();
        ArrayList<String> muteLore = new ArrayList<>();

        muteMeta.setDisplayName("§9Mute " + target);
        muteLore.add("§eClick to select a reason and duration to mute " + target);
        muteMeta.setLore(muteLore);
        mute.setItemMeta(muteMeta);
        inv.setItem(4, mute);

        //warn
        ItemStack warn = new ItemStack(Material.STAINED_CLAY, 1, (short) 10);
        ItemMeta warnMeta = warn.getItemMeta();
        ArrayList<String> warnLore = new ArrayList<>();

        warnMeta.setDisplayName(ChatColor.DARK_PURPLE + "Warn " + target);
        warnLore.add("§eClick to select a reason to warn " + target);
        warnMeta.setLore(warnLore);
        warn.setItemMeta(warnMeta);
        inv.setItem(5, warn);

        if (Rank.getRank(player.getUniqueId()) == Rank.IAHCVET) {
            ItemStack warning = new ItemStack(Material.YELLOW_FLOWER, 1);
            ItemMeta warningMeta = warning.getItemMeta();
            ArrayList<String> warningLore = new ArrayList<>();

            warningMeta.setDisplayName("§c§lATTENTION");
            warningLore.add("§cThis is meant for network staff members, not IA HC.");
            warningLore.add(" ");
            warningLore.add("§eWhilst you can use this, please only proceed if");
            warningLore.add("§eyou know what you are doing!");
            warningLore.add(" ");
            warningLore.add("§4You will have your rank removed if this is abused.");

            warningMeta.setLore(warningLore);
            warning.setItemMeta(warningMeta);

            inv.setItem(8, warning);
        }

        int current = 18;
        for (PunishData pd : PunishManager.getInstance().playerPunishments(AccountHub.getInstance().uuidFromName(target))) {
            ItemStack itemStack;
            if (pd.getType() == PunishType.BAN) {
                itemStack = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
            } else if (pd.getType() == PunishType.MUTE) {
                itemStack = new ItemStack(Material.STAINED_CLAY, 1, (short) 11);
            } else if (pd.getType() == PunishType.WARN) {
                itemStack = new ItemStack(Material.STAINED_CLAY, 1, (short) 10);
            } else if (pd.getType() == PunishType.BLACKLIST) {
                itemStack = new ItemStack(Material.STAINED_CLAY, 1, (short) 15);
            } else {
                itemStack = new ItemStack(Material.STAINED_CLAY, 1);
            }

            ItemMeta meta = itemStack.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();

            meta.setDisplayName(pd.getPunishmentId());

            lore.add("§7Type §8» §f" + pd.getType());
            lore.add("§7Date §8» §f" + pd.getDate());
            lore.add("§7Duration §8» §f" + pd.getDuration().getDisplayName());
            lore.add("§7Reason §8» §f" + pd.getReason());
            lore.add("§7Staff §8» §f" + pd.getPlayer());
            lore.add("§7Active §8» " + (pd.isActive() ? "§aTrue" : "§cFalse"));
            if (pd.getType() == PunishType.BLACKLIST)
                lore.add("§cRevoking this punishment will lead to a blacklist.");
            else {
                if (!pd.isActive()) {
                    lore.add(" ");
                    lore.add("§7Removed on §8» §f" + pd.getRemovedOn());
                    lore.add("§7Removed by §8» §f" + pd.getRemovedBy());
                    lore.add("§7Removed reason §8» §f" + pd.getRemovedReason());
                } else
                    lore.add("§8" + ChatColor.ITALIC + "This only updates when the player attempts to log in.");
            }

            if (pd.isActive())
                meta.addEnchant(Enchantment.DURABILITY, 1, true);


            meta.setLore(lore);

            itemStack.setItemMeta(meta);

            inv.setItem(current, itemStack);
            current++;
        }

        player.openInventory(inv);
    }


}
