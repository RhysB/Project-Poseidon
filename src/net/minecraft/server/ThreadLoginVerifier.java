package net.minecraft.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

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
            SessionAPI.hasJoined(loginPacket.name, netLoginHandler.b(), getIP(), (int responseCode, String username, String uuid, String ip) ->
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
            
            String s = NetLoginHandler.a(this.netLoginHandler);
            URL url = new URL("http://www.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(this.loginPacket.name, "UTF-8") + "&serverId=" + URLEncoder.encode(s, "UTF-8"));
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
            String s1 = bufferedreader.readLine();

            bufferedreader.close();
            if (s1.equals("YES")) {
//                // CraftBukkit start
//                if (this.netLoginHandler.getSocket() == null) {
//                    return;
//                }
//
//                PlayerPreLoginEvent event = new PlayerPreLoginEvent(this.loginPacket.name, this.netLoginHandler.getSocket().getInetAddress());
//                this.server.getPluginManager().callEvent(event);
//
//                if (event.getResult() != PlayerPreLoginEvent.Result.ALLOWED) {
//                    this.netLoginHandler.disconnect(event.getKickMessage());
//                    return;
//                }
//                // CraftBukkit end
                this.loginProcessHandler.userMojangSessionVerified();
                //NetLoginHandler.a(this.netLoginHandler, this.loginPacket);
            } else {
                //this.netLoginHandler.disconnect("Failed to verify username!");
                this.loginProcessHandler.cancelLoginProcess("Failed to verify username!");
            }
        } catch (Exception exception) {
            //this.netLoginHandler.disconnect("Failed to verify username! [internal error " + exception + "]");
            this.loginProcessHandler.cancelLoginProcess("Failed to verify username! [internal error " + exception + "]");
            exception.printStackTrace();
        }
    }
}
