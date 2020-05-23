package me.cps.root.util;

/*
Hi there! Pls no stealing, unless you were given express
permission to read this. if not, fuck off :)

Copyright (c) IsGeorgeCurious 2020
*/

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.cps.root.command.CommandHub;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerTitle {

    public static void allNew(String top, String bottom, int fadeIn, int stay, int fadeOut) {
        PacketContainer title = new PacketContainer(PacketType.Play.Server.TITLE);
        PacketContainer subtitle = new PacketContainer(PacketType.Play.Server.TITLE);

        title.getTitleActions().write(0, EnumWrappers.TitleAction.TITLE);
        subtitle.getTitleActions().write(0, EnumWrappers.TitleAction.SUBTITLE);

        //Title
        title.getChatComponents().write(0, WrappedChatComponent.fromText(top));
        title.getIntegers().write(0, fadeIn);
        title.getIntegers().write(1, stay);
        title.getIntegers().write(2, fadeOut);

        subtitle.getChatComponents().write(0, WrappedChatComponent.fromText(bottom));

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            CPSProtocol.sendPacket(title, p);
            CPSProtocol.sendPacket(subtitle, p);
        }

    }

    public static void all(String top, String bottom, int fadeIn, int stay, int fadeOut) {
        try {
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
        } catch (Exception e) {
            Message.broadcast(top + " §8» " + bottom);
        }
    }

    public static void all(String top, String bottom, int fadeIn, int stay, int fadeOut, boolean chat) {
        try {
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

            if (chat) {
                Message.broadcast(top + " §8» " + bottom);
            }
        } catch (Exception e) {
            Message.broadcast(top + " §8» " + bottom);
        }
    }

    public static void send(String top, String bottom, int fadeIn, int stay, int fadeOut, Player player) {
        try {
            IChatBaseComponent titleTop = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + top + "\"}");
            IChatBaseComponent titleBottom = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + bottom + "\"}");

            PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleTop);
            PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleBottom);
            PacketPlayOutTitle length = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitle);
        } catch (Exception e) {
            player.sendMessage(top + " §8» " + bottom);
        }
    }

}
