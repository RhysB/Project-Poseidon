package com.johnymuffin.poseidon;

import net.minecraft.server.NetLoginHandler;
import net.minecraft.server.Packet1Login;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.CraftServer;

import java.util.UUID;

import static com.johnymuffin.poseidon.UUIDPlayerStorage.generateOfflineUUID;
import static com.johnymuffin.poseidon.evilmidget38.UUIDFetcher.getUUIDOf;

public class ThreadUUIDFetcher extends Thread {

    final Packet1Login loginPacket;
    final NetLoginHandler netLoginHandler;
    CraftServer server;

    public ThreadUUIDFetcher(NetLoginHandler netloginhandler, Packet1Login packet1login, CraftServer server) {
        this.server = server;
        this.netLoginHandler = netloginhandler; // The login handler
        this.loginPacket = packet1login; // The login packet

    }

    public void run() {
        UUID uuid = UUIDPlayerStorage.getInstance().getPlayerUUID(loginPacket.name);
        if(uuid == null) {
            try {
                uuid = getUUIDOf(loginPacket.name);
                if(uuid == null) {
                    System.out.println(loginPacket.name + " does not have a Mojang UUID associated with their name");
                    System.out.println("Using Offline Based UUID for " + loginPacket.name + " - " + generateOfflineUUID(loginPacket.name));
                } else {
                    System.out.println("Fetched UUID from Mojang for " + loginPacket.name + " - " + uuid.toString());
                    UUIDPlayerStorage.getInstance().addPlayerOnlineUUID(loginPacket.name, uuid);
                }

                netLoginHandler.authenticatePlayer(loginPacket);


            } catch (Exception e) {
                this.netLoginHandler.disconnect(ChatColor.RED + "Sorry, we can't connect to Mojang currently, please try again later");
                e.printStackTrace();
            }
        } else {
            System.out.println("Fetched UUID from Cache for " + loginPacket.name + " - " + uuid.toString());
            netLoginHandler.authenticatePlayer(loginPacket);
        }



    }
}


