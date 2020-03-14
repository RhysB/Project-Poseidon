package com.projectposeidon;

import net.minecraft.server.NetLoginHandler;
import net.minecraft.server.Packet1Login;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.CraftServer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.johnymuffin.poseidon.UUIDPlayerStorage.generateOfflineUUID;
import static com.johnymuffin.poseidon.evilmidget38.UUIDFetcher.getUUIDOf;

public class ThreadUUIDFetcher extends Thread {

    final Packet1Login loginPacket;
//    final NetLoginHandler netLoginHandler;
    final LoginProcessHandler loginProcessHandler;

    public ThreadUUIDFetcher(Packet1Login packet1Login, LoginProcessHandler loginProcessHandler) {
//        this.netLoginHandler = netloginhandler; // The login handler
        this.loginProcessHandler = loginProcessHandler;
        this.loginPacket = packet1Login; // The login packet

    }

    public void run() {
        UUID uuid = com.johnymuffin.poseidon.UUIDPlayerStorage.getInstance().getPlayerUUID(loginPacket.name);
        if(uuid == null) {
            try {
                uuid = getUUIDOf(loginPacket.name);
                if(uuid == null) {
                    System.out.println(loginPacket.name + " does not have a Mojang UUID associated with their name");
                    System.out.println("Using Offline Based UUID for " + loginPacket.name + " - " + generateOfflineUUID(loginPacket.name));
                } else {
                    System.out.println("Fetched UUID from Mojang for " + loginPacket.name + " - " + uuid.toString());
                    com.johnymuffin.poseidon.UUIDPlayerStorage.getInstance().addPlayerOnlineUUID(loginPacket.name, uuid);
                }
                loginProcessHandler.userUUIDReceived();
                //netLoginHandler.authenticatePlayer(loginPacket);
                //netLoginHandler.playerUUIDFetched(loginPacket);


            } catch (Exception e) {
                //this.netLoginHandler.disconnect(ChatColor.RED + "Sorry, we can't connect to Mojang currently, please try again later");
                System.out.println("Mojang failed contact for user " + loginPacket.name + ": " + e.getMessage());
                loginProcessHandler.cancelLoginProcess(ChatColor.RED + "Sorry, we can't connect to Mojang currently, please try again later");
            }
        } else {
            System.out.println("Fetched UUID from Cache for " + loginPacket.name + " - " + uuid.toString());
            //netLoginHandler.authenticatePlayer(loginPacket);
            loginProcessHandler.userUUIDReceived();
        }



    }
}


