package com.projectposeidon;

import net.minecraft.server.NetLoginHandler;
import net.minecraft.server.Packet1Login;
import net.minecraft.server.ThreadLoginVerifier;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.player.PlayerPreLoginEvent;

import java.util.Timer;
import java.util.UUID;

public class LoginProcessHandler {

    private NetLoginHandler netLoginHandler;
    private final Packet1Login packet1Login;
    private CraftServer server;
    private boolean onlineMode;
    private boolean loginCancelled = false;
    private LoginProcessHandler loginProcessHandler;
    private boolean loginSuccessful = false;

    public LoginProcessHandler(NetLoginHandler netloginhandler, Packet1Login packet1login, CraftServer server, boolean onlineMode) {
        loginProcessHandler = this;
        this.netLoginHandler = netloginhandler;
        this.packet1Login = packet1login;
        this.server = server;
        this.onlineMode = onlineMode;
        processAuthentication();

        Timer t = new java.util.Timer();
        t.schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        // close the thread
                        if (!loginSuccessful && !loginCancelled) {
                            cancelLoginProcess("Login Process Handler Timeout");
                        }
                        if (loginSuccessful && !loginCancelled) {
                            loginProcessHandler = null;
                        }
                    }
                },
                5000
        );

    }

    private void processAuthentication() {
        if (onlineMode) {
            //Server is running online mode
            verifyMojangSession();
        } else {
            //Server is not running online mode
            getUserUUID();
        }
    }

    private void getUserUUID() {
        UUID uuid = com.johnymuffin.poseidon.UUIDPlayerStorage.getInstance().getPlayerUUID(packet1Login.name);
        if (uuid == null) {
            (new ThreadUUIDFetcher(packet1Login, this)).start();
        } else {
            System.out.println("Fetched UUID from Cache for " + packet1Login.name + " - " + uuid.toString());
            connectPlayer();
        }


    }

    public synchronized void userUUIDReceived() {
        if (!loginSuccessful & !loginCancelled) {
            connectPlayer();
        }
    }

    private void verifyMojangSession() {
        if (!loginSuccessful & !loginCancelled) {
            (new ThreadLoginVerifier(this, netLoginHandler, this.packet1Login, this.server)).start(); // CraftBukkit
        }
    }

    public synchronized void userMojangSessionVerified() {
        if (!loginSuccessful & !loginCancelled) {
            getUserUUID();
        }
    }

    private void connectPlayer() {
        if (!loginSuccessful && !loginCancelled) {
            loginSuccessful = true;

            //Bukkit Login Event Start
            if (this.netLoginHandler.getSocket() == null) {
                return;
            }
            PlayerPreLoginEvent event = new PlayerPreLoginEvent(this.packet1Login.name, this.netLoginHandler.getSocket().getInetAddress());
            this.server.getPluginManager().callEvent(event);
            if (event.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
                this.cancelLoginProcess(event.getKickMessage());
                return;
            }
            //Bukkit Login Event End

            NetLoginHandler.a(netLoginHandler, packet1Login);
        }
    }

    public synchronized void cancelLoginProcess(String s) {
        if (!loginCancelled && !loginSuccessful) {
            loginCancelled = true;
            netLoginHandler.disconnect(s);
        }
    }


}
