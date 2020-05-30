package me.cps.root.util;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Curious Productions Root
 * CPS Utilities - CPS Protocol
 *
 * Handles the sending of Protocol Lib Packets
 * NOTICE: I don't recall this ever being properly used. (apart from attempts with titles)
 *
 * @author  Gabriella Hotten
 * @version 1.0
 * @since   2020-05-10
 */
public class CPSProtocol {

    public static ProtocolManager protocolManager;

    public static void setup() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public static void sendPacket(PacketContainer packet, Player player) {
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(
                    "Cannot send packet " + packet, e);
        }
    }
}
