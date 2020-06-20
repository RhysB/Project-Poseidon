package org.bukkit.event.player;

import com.projectposeidon.johnymuffin.LoginProcessHandler;
import org.bukkit.event.Event;

import java.net.InetAddress;

public class PlayerConnectionInitializationEvent extends Event {
    private String username;
    private InetAddress ipAddress;
    private LoginProcessHandler loginProcessHandler;
    private boolean connecting = true;


    public PlayerConnectionInitializationEvent(String username, InetAddress ipAddress, LoginProcessHandler loginProcessHandler) {
        super(Type.Player_Connection_Initialization);
        this.username = username;
        this.ipAddress = ipAddress;
        this.loginProcessHandler = loginProcessHandler;
    }

    public void disconnectPlayer(String kickReason) {
        loginProcessHandler.cancelLoginProcess(kickReason);
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return username;
    }

    /**
     * Gets the player IP address.
     *
     * @return
     */
    public InetAddress getAddress() {
        return ipAddress;
    }

}
