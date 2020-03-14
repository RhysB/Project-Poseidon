package com.projectposeidon.johnymuffin;

import net.minecraft.server.NetLoginHandler;
import net.minecraft.server.Packet1Login;
import net.minecraft.server.ThreadLoginVerifier;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class LoginProcessHandler {

    private NetLoginHandler netLoginHandler;
    private final Packet1Login packet1Login;
    private CraftServer server;
    private boolean onlineMode;
    private boolean loginCancelled = false;
    private LoginProcessHandler loginProcessHandler;
    private boolean loginSuccessful = false;

    private ArrayList<Plugin> pluginPauses = new ArrayList<Plugin>();

    public LoginProcessHandler(NetLoginHandler netloginhandler, Packet1Login packet1login, CraftServer server, boolean onlineMode) {
        this.loginProcessHandler = this;
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
                            System.out.println("LoginProcessHandler for user " + packet1login.name + " has failed to respond after 20 seconds. And future calls to this class will result in error");
                            loginProcessHandler = null;
                            cancelLoginProcess("Login Process Handler Timeout");
                        }
                    }
                },
                30000
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
        UUID uuid = UUIDPlayerStorage.getInstance().getPlayerUUID(packet1Login.name);
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

            //Bukkit Login Event Start
            if (this.netLoginHandler.getSocket() == null) {
                return;
            }
            PlayerPreLoginEvent event = new PlayerPreLoginEvent(this.packet1Login.name, this.netLoginHandler.getSocket().getInetAddress(), loginProcessHandler);
            this.server.getPluginManager().callEvent(event);
            if (event.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
                cancelLoginProcess(event.getKickMessage());
                return;
            }
            //Bukkit Login Event End
            if (isPlayerConnectionPaused()) {
                long startTime = System.currentTimeMillis() / 1000L;
                //Check every 0.5 seconds to see if playerConnection is unpaused
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!loginProcessHandler.isPlayerConnectionPaused()) {
                            long endTime = System.currentTimeMillis() / 1000L;
                            int difference = (int) (endTime - startTime);
                            System.out.println("Player " + loginProcessHandler.packet1Login.name + " was allowed to join after being on hold for " + difference + " seconds");
                            loginProcessHandler.setLoginSuccessful(true);
                            NetLoginHandler.a(netLoginHandler, packet1Login);
                            this.cancel();
                        }
                    }
                }, 0, 500);


            } else {
                loginSuccessful = true;
                NetLoginHandler.a(netLoginHandler, packet1Login);
            }
        }
    }

    public synchronized void cancelLoginProcess(String s) {
        if (!loginCancelled && !loginSuccessful) {
            loginCancelled = true;
            netLoginHandler.disconnect(s);
        }
    }

    /**
     * Set a pause for your plugin
     */
    public void addConnectionPause(Plugin plugin) throws Exception {
        if(pluginPauses.contains(plugin)) {
            throw new Exception("Plugin " + plugin.getDescription().getName() + " has tried to pause player login multiple times");
        }
        pluginPauses.add(plugin);
    }

    /**
     * Remove a pause for your plugin
     */
    public void removeConnectionPause(Plugin plugin) {
        if(pluginPauses.contains(plugin)) {
            pluginPauses.remove(plugin);
        }
    }
    /**
     * See if the players connection currently paused
     */
    public boolean isPlayerConnectionPaused() {
        if(pluginPauses.size() == 0) {
            return false;
        }
        return true;
    }






    private void setLoginSuccessful(boolean loginSuccessful) {
        this.loginSuccessful = loginSuccessful;
    }
}
