package org.bukkit.event.player;

import com.projectposeidon.johnymuffin.ConnectionPause;
import com.projectposeidon.johnymuffin.LoginProcessHandler;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import java.net.InetAddress;

/**
 * Stores details for players attempting to log in
 */
public class PlayerPreLoginEvent extends Event {
    private Result result;
    private String message;
    private String name;
    private InetAddress ipAddress;
    private LoginProcessHandler loginProcessHandler; // Project Poseidon

    public PlayerPreLoginEvent(String name, InetAddress ipAddress, LoginProcessHandler loginProcessHandler) {
        super(Type.PLAYER_PRELOGIN);
        this.loginProcessHandler = loginProcessHandler;
        this.result = Result.ALLOWED;
        this.message = "";
        this.name = name;
        this.ipAddress = ipAddress;
    }
    //Project Poseidon Start

    /**
     * Set a pause for your plugin
     * Connection pauses are for fetching data for a player before they MIGHT be allowed to join
     *
     * @param plugin              Instance of plugin
     * @param connectionPauseName Name of connection pause (Ensure no duplicates)
     * @return ConnectionPause Object, used to remove a connection pause
     */
    public ConnectionPause addConnectionPause(Plugin plugin, String connectionPauseName) {
        return loginProcessHandler.addConnectionInterrupt(plugin, connectionPauseName);
    }

    /**
     * Remove a pause for your plugin by the returned ConnectionPause object
     */
    public void removeConnectionPause(ConnectionPause connectionPause) {
        loginProcessHandler.removeConnectionPause(connectionPause);
    }

    /**
     * Cancel a players login before join or login events if a connection pause is still active
     */
    public void cancelPlayerLogin(String kickMessage) {
        loginProcessHandler.cancelLoginProcess(kickMessage);
    }

    /**
     * See if the players connection currently paused
     */
    public boolean isPlayerConnectionPaused() {
        return loginProcessHandler.isPlayerConnectionPaused();
    }

    /**
     * Gets the LoginProcessHandler instance for the connection
     *
     * @return Gets the LoginProcessHandler
     */
    @Deprecated
    public LoginProcessHandler getLoginProcessHandler() {
        return loginProcessHandler;
    }

    //Project Poseidon End


    /**
     * Gets the current result of the login, as an enum
     *
     * @return Current Result of the login
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets the new result of the login, as an enum
     *
     * @param result New result to set
     */
    public void setResult(final Result result) {
        this.result = result;
    }

    /**
     * Gets the current kick message that will be used if getResult() != Result.ALLOWED
     *
     * @return Current kick message
     */
    public String getKickMessage() {
        return message;
    }

    /**
     * Sets the kick message to display if getResult() != Result.ALLOWED
     *
     * @param message New kick message
     */
    public void setKickMessage(final String message) {
        this.message = message;
    }

    /**
     * Allows the player to log in
     */
    public void allow() {
        result = Result.ALLOWED;
        message = "";
    }

    /**
     * Disallows the player from logging in, with the given reason
     *
     * @param result  New result for disallowing the player
     * @param message Kick message to display to the user
     */
    public void disallow(final Result result, final String message) {
        this.result = result;
        this.message = message;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player IP address.
     *
     * @return
     */
    public InetAddress getAddress() {
        return ipAddress;
    }

    /**
     * Basic kick reasons for communicating to plugins
     */
    public enum Result {

        /**
         * The player is allowed to log in
         */
        ALLOWED,
        /**
         * The player is not allowed to log in, due to the server being full
         */
        KICK_FULL,
        /**
         * The player is not allowed to log in, due to them being banned
         */
        KICK_BANNED,
        /**
         * The player is not allowed to log in, due to them not being on the white list
         */
        KICK_WHITELIST,
        /**
         * The player is not allowed to log in, for reasons undefined
         */
        KICK_OTHER
    }
}
