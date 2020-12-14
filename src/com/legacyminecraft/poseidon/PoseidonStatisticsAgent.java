package com.legacyminecraft.poseidon;

import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.CraftServer;
import org.json.simple.JSONObject;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class PoseidonStatisticsAgent {
    //Default details
    private int protocolID = 1;
    public final String postTo = "https://poseidon.johnymuffin.com/statistics.php";
    //Unique Details
    private String uniqueID;
    private final String sessionID;
    private final String version;
    private final String branch;
    private final Long startTime;
    private Object syncLock = new Object();

    public PoseidonStatisticsAgent(MinecraftServer server, CraftServer craftServer) {
        //This really shouldn't be needed, but it runs once, whats the harm?
        synchronized (syncLock) {
            this.startTime = (System.currentTimeMillis() / 1000L);
            this.uniqueID = PoseidonConfig.getInstance().getString("settings.statistics.key");
            //Create temp value
            Random rnd = new Random();
            this.sessionID = String.valueOf(100000 + rnd.nextInt(900000));
            this.version = craftServer.getPoseidonVersion();
            this.branch = craftServer.getPoseidonReleaseType();
        }

        PoseidonStatisticsSender poseidonStatisticsSender = new PoseidonStatisticsSender();
        poseidonStatisticsSender.start();

    }

    public JSONObject getPing() {
        synchronized (syncLock) {
            JSONObject ping = new JSONObject();
            ping.put("protocol", protocolID);
            ping.put("uniqueID", uniqueID);
            ping.put("sessionID", sessionID);
            ping.put("version", version);
            ping.put("branch", branch);
            int uptime = (int) ((System.currentTimeMillis() / 1000L) - startTime);
            ping.put("uptime", uptime);
            return ping;
        }
    }

    public class PoseidonStatisticsSender extends Thread {
        public volatile boolean errored = false;

        public void run() {
            while (true && !this.isInterrupted()) {
                HttpURLConnection connection = null;
                try {
                    System.out.println("Submitting Project Poseidon Statistics.");
                    final JSONObject ping = getPing();
                    URL url = new URL(postTo);
                    //Create Connection
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    //Write Body
                    OutputStream stream = connection.getOutputStream();
                    stream.write(ping.toJSONString().getBytes());
                    stream.flush();
                    stream.close();
                    //Get Response
                    String response = String.valueOf(new InputStreamReader(connection.getInputStream()));
                    connection.disconnect();
                    errored = false;
                } catch (Exception exception) {
                    if (!errored) {
                        System.out.println("Failed to submit statistics for Project Poseidon. " + exception + " : " + exception.getMessage() + ".");
                    }
                    errored = true;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                        connection = null;
                    }
                    try {
                        Thread.sleep(300000L);
                    } catch (InterruptedException exception) {
                        System.out.println("Project Poseidon statistics thread has been closed.");
                        break;
                    }
                }
            }
        }

    }

}
