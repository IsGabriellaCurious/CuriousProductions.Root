package me.cps.root.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

/**
 * Curious Productions Root
 * CPS Utilities - Item Builder
 *
 * Handles the building of items
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-05-12
 */
public class ItemBuilder {
    public static ItemStack buildWithEnchant(Material material, int amount, short s, Enchantment enchantment, int modifier) {
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
