package com.projectposeidon.johnymuffin;

import com.legacyminecraft.poseidon.PoseidonConfig;
import com.legacyminecraft.poseidon.PoseidonPlugin;
import com.legacyminecraft.poseidon.uuid.ThreadUUIDFetcher;
import net.minecraft.server.NetLoginHandler;
import net.minecraft.server.Packet1Login;
import net.minecraft.server.ThreadLoginVerifier;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerConnectionInitializationEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.UUID;

public class LoginProcessHandler {

    private NetLoginHandler netLoginHandler;
    private final Packet1Login packet1Login;
    private CraftServer server;
    private boolean onlineMode;
    private boolean loginCancelled = false;
    private LoginProcessHandler loginProcessHandler;
    private boolean loginSuccessful = false;
    private long startTime;
    private ArrayList<Plugin> pluginPauses = new ArrayList<Plugin>();
    private ArrayList<ConnectionPause> pluginPauseObjects = new ArrayList<ConnectionPause>();
    private ArrayList<String> pluginPauseNames = new ArrayList<String>();

    public LoginProcessHandler(NetLoginHandler netloginhandler, Packet1Login packet1login, CraftServer server, boolean onlineMode) {
        this.loginProcessHandler = this;
        this.netLoginHandler = netloginhandler;
        this.packet1Login = packet1login;
        this.server = server;
        this.onlineMode = onlineMode;
        processAuthentication();
        
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(new PoseidonPlugin(), () -> {
            if (!loginSuccessful && !loginCancelled) {
                cancelLoginProcess("Login Process Handler Timeout");
                System.out.println("LoginProcessHandler for user " + packet1login.name + " has failed to respond after 20 seconds. And future calls to this class will result in error");
                System.out.println("Plugin Pauses: " + pluginPauseNames.toString());

            }
        }, 400);

    }

    private void processAuthentication() {
        PlayerConnectionInitializationEvent event = new PlayerConnectionInitializationEvent(this.packet1Login.name, this.netLoginHandler.getSocket().getInetAddress(), loginProcessHandler);
        this.server.getPluginManager().callEvent(event);
        if (loginCancelled) {
            return;
        }

        if (onlineMode) {
            //Server is running online mode
            verifyMojangSession();
        } else {
            //Server is not running online mode
            getUserUUID();
        }
    }

    private void getUserUUID() {
        //UUID uuid = UUIDManager.getInstance().getUUIDFromUsername(packet1Login.name, true);
        long unixTime = (System.currentTimeMillis() / 1000L);
        UUID uuid = UUIDManager.getInstance().getUUIDFromUsername(packet1Login.name, true, unixTime);
        if (uuid == null) {
            (new ThreadUUIDFetcher(packet1Login, this, PoseidonConfig.getInstance().getBoolean("settings.use-get-for-uuids.enabled", false))).start();
        } else {
            System.out.println("Fetched UUID from Cache for " + packet1Login.name + " - " + uuid.toString());
            connectPlayer(uuid);
        }


    }

    public synchronized void userUUIDReceived(UUID uuid, boolean onlineMode) {
        if (!onlineMode) {
            if (Boolean.valueOf(String.valueOf(PoseidonConfig.getInstance().getConfigOption("settings.check-username-validity.enabled", true))) && !isUsernameValid()) {
                //Username is invalid, and is a cracked user
                return;
            }
        }


        long unixTime = (System.currentTimeMillis() / 1000L) + 1382400;
        UUIDManager.getInstance().receivedUUID(packet1Login.name, uuid, unixTime, onlineMode);
        connectPlayer(uuid);

    }

    //This function is only run if the user is cracked
    public boolean isUsernameValid() {
        String username = this.packet1Login.name;
        if (username.isEmpty()) {
            cancelLoginProcess("Sorry, you don't have a username, messing with MC?????");
            return false;
        }
        String regex = String.valueOf(PoseidonConfig.getInstance().getConfigOption("settings.check-username-validity.regex", "[a-zA-Z0-9_?]*"));
        int minimumLength = Integer.valueOf(String.valueOf(PoseidonConfig.getInstance().getConfigOption("settings.check-username-validity.min-length", 3)));
        int maximumLength = Integer.valueOf(String.valueOf(PoseidonConfig.getInstance().getConfigOption("settings.check-username-validity.max-length", 16)));

        if (username.length() > maximumLength) {
            cancelLoginProcess("Sorry, your username is too long. The maximum length is: " + maximumLength);
            return false;
        }
        if (username.length() < minimumLength) {
            cancelLoginProcess("Sorry, your username is too short. The minimum length is: " + minimumLength);
            return false;
        }
        if (!username.matches(regex)) {
            cancelLoginProcess("Sorry, your username is invalid, allowed characters: " + regex);
            return false;
        }

        return true;
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

    private void connectPlayer(UUID uuid) {
        String username = packet1Login.name;
        //Check if a player with the same UUID or Username is already online which is mainly an issue in Offline Mode servers.
        for (Player p : server.getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(username) || p.getUniqueId().equals(uuid)) {
                cancelLoginProcess(ChatColor.RED + "A player with your username or uuid is already online, try reconnecting in a minute.");
                System.out.println("User " + username + " has been blocked from connecting as they share a username or UUID with a user who is already online called " + p.getName() +
                        "\nMost likely the user has changed their UUID or the server is running in offline mode and someone has attempted to connect with their name");
            }
        }


        if (!loginSuccessful && !loginCancelled) {

            //Bukkit Login Event Start
            if (this.netLoginHandler.getSocket() == null) {
                return;
            }


            PlayerPreLoginEvent event = new PlayerPreLoginEvent(this.packet1Login.name, ((InetSocketAddress) netLoginHandler.networkManager.getSocketAddress()).getAddress(), loginProcessHandler);
            this.server.getPluginManager().callEvent(event);
            if (event.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
                cancelLoginProcess(event.getKickMessage());
                return;
            }
            //Bukkit Login Event End
            if (isPlayerConnectionPaused()) {
                startTime = System.currentTimeMillis() / 1000L;
            } else {
                loginSuccessful = true;
                NetLoginHandler.a(netLoginHandler, packet1Login);
            }
        }
    }

    /**
     * Cancel a players login before join or login events
     */
    public synchronized void cancelLoginProcess(String s) {
        if (!loginCancelled && !loginSuccessful) {
            loginCancelled = true;
            netLoginHandler.disconnect(s);
        }
    }


    /**
     * Set a pause for your plugin
     * Connection pauses are for fetching data for a player before they MIGHT be allowed to join
     *
     * @param plugin              Instance of plugin
     * @param connectionPauseName Name of connection pause (Ensure no duplicates)
     * @return ConnectionPause Object, used to remove a connection pause
     */
    public ConnectionPause addConnectionInterrupt(Plugin plugin, String connectionPauseName) {
        //Log to console a pause has started on first pause
        if (pluginPauseNames.size() == 0) {
            System.out.println("One or more plugins has paused the incoming connection for player " + packet1Login.name);
        }
        //Add plugin pause names and pauses for respective plugins
        final ConnectionPause connectionPause = new ConnectionPause(plugin.getDescription().getName(), connectionPauseName, loginProcessHandler);
        pluginPauseNames.add(plugin.getDescription().getName() + ":" + connectionPauseName);
        pluginPauseObjects.add(connectionPause);
        return connectionPause;
    }

    /**
     * Remove a pause for your plugin by the returned ConnectionPause object
     */
    public synchronized void removeConnectionPause(ConnectionPause connectionPause) {
        if (pluginPauseObjects.contains(connectionPause)) {
            pluginPauseObjects.remove(connectionPause);
            if (!loginProcessHandler.isPlayerConnectionPaused()) {
                long endTime = System.currentTimeMillis() / 1000L;
                int difference = (int) (endTime - startTime);
                System.out.println("Player " + loginProcessHandler.packet1Login.name + " was allowed to join after being on hold for " + difference + " seconds by the following plugins: " + pluginPauseNames.toString());
                loginProcessHandler.setLoginSuccessful(true);
                if (!loginCancelled) {
                    NetLoginHandler.a(netLoginHandler, packet1Login);
                }
            }
        }
    }


    /**
     * Set a pause for your plugin
     * Connection pauses are for fetching data for a player before they MIGHT be allowed to join
     */
    @Deprecated
    public void addConnectionPause(Plugin plugin) throws Exception {
        System.out.println("[Poseidon] " + plugin.getDescription().getName() + " is using the deprecated connection pause system which will be removed in the future. Contact the plugin author to get an updated version.");
        if (pluginPauses.contains(plugin)) {
            throw new Exception("Plugin " + plugin.getDescription().getName() + " has tried to pause player login multiple times");
        }
        //Log to console a pause has started on first pause
        if (pluginPauseNames.size() == 0) {
            System.out.println("One or more plugins has paused the incoming connection for player " + packet1Login.name);
        }
        //Add plugin pause names and pauses for respective plugins
        pluginPauseNames.add(plugin.getDescription().getName());
        pluginPauses.add(plugin);
    }

    /**
     * Remove a pause for your plugin
     */
    @Deprecated
    public void removeConnectionPause(Plugin plugin) {
        if (pluginPauses.contains(plugin)) {
            pluginPauses.remove(plugin);
            //Check if all pauses are removed
            if (!loginProcessHandler.isPlayerConnectionPaused()) {
                long endTime = System.currentTimeMillis() / 1000L;
                int difference = (int) (endTime - startTime);
                System.out.println("Player " + loginProcessHandler.packet1Login.name + " was allowed to join after being on hold for " + difference + " seconds by the following plugins: " + pluginPauseNames.toString());
                loginProcessHandler.setLoginSuccessful(true);
                if (!loginCancelled) {
                    NetLoginHandler.a(netLoginHandler, packet1Login);
                }
            }
        }
    }

    /**
     * See if the players connection currently paused
     */
    public boolean isPlayerConnectionPaused() {
        if (pluginPauses.size() == 0 && pluginPauseObjects.size() == 0) {
            return false;
        }
        return true;
    }


    private void setLoginSuccessful(boolean loginSuccessful) {
        this.loginSuccessful = loginSuccessful;
    }
}
