package net.minecraft.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

// CraftBukkit start
import com.johnymuffin.poseidon.UUIDPlayerStorage;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.player.PlayerPreLoginEvent;

import static com.johnymuffin.poseidon.UUIDPlayerStorage.generateOfflineUUID;
import static com.johnymuffin.poseidon.evilmidget38.UUIDFetcher.getUUIDOf;
// CraftBukkit end

class ThreadLoginVerifier extends Thread {

    final Packet1Login loginPacket;

    final NetLoginHandler netLoginHandler;

    // CraftBukkit start
    CraftServer server;

    ThreadLoginVerifier(NetLoginHandler netloginhandler, Packet1Login packet1login, CraftServer server) {
        this.server = server;
        // CraftBukkit end

        this.netLoginHandler = netloginhandler;
        this.loginPacket = packet1login;
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

                NetLoginHandler.a(this.netLoginHandler, this.loginPacket);


            } catch (Exception e) {
                this.netLoginHandler.disconnect(ChatColor.RED + "Sorry, we can't connect to Mojang currently, please try again later");
                System.out.println("Mojang failed contact for user " + loginPacket.name + ": " + e.getMessage());
            }
        } else {
            System.out.println("Fetched UUID from Cache for " + loginPacket.name + " - " + uuid.toString());
            //netLoginHandler.authenticatePlayer(loginPacket);
            NetLoginHandler.a(this.netLoginHandler, this.loginPacket);
        }

//        try {
//            String s = NetLoginHandler.a(this.netLoginHandler);
//            URL url = new URL("http://www.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(this.loginPacket.name, "UTF-8") + "&serverId=" + URLEncoder.encode(s, "UTF-8"));
//            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(url.openStream()));
//            String s1 = bufferedreader.readLine();
//
//            bufferedreader.close();
//            if (s1.equals("YES")) {
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
//
//                NetLoginHandler.a(this.netLoginHandler, this.loginPacket);
//            } else {
//                this.netLoginHandler.disconnect("Failed to verify username!");
//            }
//        } catch (Exception exception) {
//            this.netLoginHandler.disconnect("Failed to verify username! [internal error " + exception + "]");
//            exception.printStackTrace();
//        }
    }
}
