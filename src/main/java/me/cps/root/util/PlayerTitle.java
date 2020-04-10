package me.cps.root.util;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerTitle {

    public static void all(String top, String bottom, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent titleTop = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + top + "\"}");
        IChatBaseComponent titleBottom = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + bottom + "\"}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleTop);
        PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleBottom);
        PacketPlayOutTitle length = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subTitle);
        }
    }

    public static void send(String top, String bottom, int fadeIn, int stay, int fadeOut, Player player) {
        IChatBaseComponent titleTop = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + top + "\"}");
        IChatBaseComponent titleBottom = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + bottom + "\"}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleTop);
        PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleBottom);
        PacketPlayOutTitle length = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitle);
    }

}
