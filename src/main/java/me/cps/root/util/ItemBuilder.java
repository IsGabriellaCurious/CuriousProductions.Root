package me.cps.root.util;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class ItemBuilder
{
    public static ItemStack buildWithEnchant(Material material, int amount, short s, Enchantment enchantment, int modifier)
    {
        ItemStack itemStack = new ItemStack(material, amount, s);
        ItemMeta itemMeta =  itemStack.getItemMeta();

        itemMeta.addEnchant(enchantment, modifier, true);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ItemStack build(Material material, int amount)
    {
        return new ItemStack(material, amount);
    }

    public static ItemStack build(Material material, int amount, short s)
    {
        return new ItemStack(material, amount, s);
    }


}
