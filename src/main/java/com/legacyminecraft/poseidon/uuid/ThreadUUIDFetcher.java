package com.legacyminecraft.poseidon.uuid;

import com.legacyminecraft.poseidon.PoseidonConfig;
import com.legacyminecraft.poseidon.util.GetUUIDFetcher;
import com.legacyminecraft.poseidon.util.UUIDResult;
import com.projectposeidon.johnymuffin.LoginProcessHandler;
import net.minecraft.server.Packet1Login;
import org.bukkit.ChatColor;

import java.util.UUID;

import static com.legacyminecraft.poseidon.util.UUIDFetcher.getUUIDOf;
import static com.projectposeidon.johnymuffin.UUIDManager.generateOfflineUUID;

public class ThreadUUIDFetcher extends Thread {

    final Packet1Login loginPacket;
    //    final NetLoginHandler netLoginHandler;
    final LoginProcessHandler loginProcessHandler;
    final boolean useGetMethod;

    public ThreadUUIDFetcher(Packet1Login packet1Login, LoginProcessHandler loginProcessHandler, boolean useGetMethod) {
//        this.netLoginHandler = netloginhandler; // The login handler
        this.loginProcessHandler = loginProcessHandler;
        this.loginPacket = packet1Login; // The login packet
        this.useGetMethod = useGetMethod;

    }

    public void run() {
        if (useGetMethod) {
            getMethod();
        } else {
            postMethod();
        }

    }

    public void getMethod() {
        UUIDResult uuidResult;
        uuidResult = GetUUIDFetcher.getUUID(loginPacket.name);
        if (uuidResult.getReturnType().equals(UUIDResult.ReturnType.ONLINE)) {
            System.out.println("Fetched UUID from Mojang for " + loginPacket.name + " - " + uuidResult.getUuid().toString());
            loginProcessHandler.userUUIDReceived(uuidResult.getUuid(), true);
            return;
        } else if (uuidResult.getReturnType().equals(UUIDResult.ReturnType.OFFLINE)) {
            if ((boolean) PoseidonConfig.getInstance().getProperty("settings.allow-graceful-uuids")) {
                System.out.println(loginPacket.name + " does not have a Mojang UUID associated with their name");
                UUID offlineUUID = uuidResult.getUuid();
                loginProcessHandler.userUUIDReceived(offlineUUID, false);
                System.out.println("Using Offline Based UUID for " + loginPacket.name + " - " + offlineUUID);
            } else {
                System.out.println(loginPacket.name + " does not have a UUID with Mojang. Player has been kicked as graceful UUID is disabled");
                loginProcessHandler.cancelLoginProcess(ChatColor.RED + "Sorry, we only support premium accounts");
            }
            return;
        }
        System.out.println("Mojang failed contact for user " + loginPacket.name + ":");
        uuidResult.getException().printStackTrace();
        loginProcessHandler.cancelLoginProcess(ChatColor.RED + "Sorry, we can't connect to Mojang currently, please try again later");

    }


    public void postMethod() {
        UUID uuid;
        try {
            uuid = getUUIDOf(loginPacket.name);
            if (uuid == null) {
                if ((boolean) PoseidonConfig.getInstance().getProperty("settings.allow-graceful-uuids")) {
                    System.out.println(loginPacket.name + " does not have a Mojang UUID associated with their name");
                    UUID offlineUUID = generateOfflineUUID(loginPacket.name);
                    loginProcessHandler.userUUIDReceived(offlineUUID, false);
                    System.out.println("Using Offline Based UUID for " + loginPacket.name + " - " + offlineUUID);
                } else {
                    System.out.println(loginPacket.name + " does not have a UUID with Mojang. Player has been kicked as graceful UUID is disabled");
                    loginProcessHandler.cancelLoginProcess(ChatColor.RED + "Sorry, we only support premium accounts");
                }
            } else {
                System.out.println("Fetched UUID from Mojang for " + loginPacket.name + " - " + uuid.toString());
                loginProcessHandler.userUUIDReceived(uuid, true);
            }
        } catch (Exception e) {
            System.out.println("Mojang failed contact for user " + loginPacket.name + ":");
            e.printStackTrace();
            loginProcessHandler.cancelLoginProcess(ChatColor.RED + "Sorry, we can't connect to Mojang currently, please try again later");
        }

    }


}


