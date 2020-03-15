package net.minecraft.server;

import org.bukkit.craftbukkit.CraftServer;

import com.projectposeidon.johnymuffin.LoginProcessHandler;
import com.projectposeidon.moderator_man.SessionAPI;

// CraftBukkit start
// CraftBukkit end

public class ThreadLoginVerifier extends Thread {

    final Packet1Login loginPacket;

    final NetLoginHandler netLoginHandler;

    final LoginProcessHandler loginProcessHandler;  //Project Poseidon

    // CraftBukkit start
    CraftServer server;

    public ThreadLoginVerifier(LoginProcessHandler loginProcessHandler, NetLoginHandler netloginhandler, Packet1Login packet1login, CraftServer server) {
        this.server = server;
        // CraftBukkit end
        this.loginProcessHandler = loginProcessHandler;  //Project Poseidon

        this.netLoginHandler = netloginhandler;
        this.loginPacket = packet1login;
    }
    
    private String getIP()
    {
        return netLoginHandler.networkManager.socket.getInetAddress().getHostAddress();
    }

    public void run() {
        try {
            SessionAPI.hasJoined(loginPacket.name, netLoginHandler.getServerID(), getIP(), (int responseCode, String username, String uuid, String ip) ->
            {
                // make sure the request didn't fail (-1), and the response wasn't empty (204)
                if (responseCode != -1 && responseCode != 204)
                {
                    // make sure username and ip match up (docs say username is case insensitive https://wiki.vg/Protocol_Encryption#Server)
                    if (username.equalsIgnoreCase(loginPacket.name) && ip == getIP())
                    {
                        loginProcessHandler.userMojangSessionVerified();
                    } else {
                        loginProcessHandler.cancelLoginProcess("Failed to verify username!");
                    }
                } else {
                    //TODO: should this message be different? -moderator_man
                    loginProcessHandler.cancelLoginProcess("Failed to verify username!");
                }
            });
        } catch (Exception exception) {
            //this.netLoginHandler.disconnect("Failed to verify username! [internal error " + exception + "]");
            this.loginProcessHandler.cancelLoginProcess("Failed to verify username! [internal error " + exception + "]");
            exception.printStackTrace();
        }
    }
}
