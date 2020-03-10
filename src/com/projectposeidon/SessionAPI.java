package com.projectposeidon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * A wrapper class for the Minecraft session API
 * 
 * TODO maybe make the HTTP requests asynchronous? idk if it really matters
 * 
 * @author moderator_man
 */
public class SessionAPI
{
    public static final String SESSION_BASE = "http://session.minecraft.net/game/";

    public static boolean hasJoined(String username, String serverId)
    {
        String response = httpGetRequest(SESSION_BASE + String.format("checkserver.jsp?user=%s&serverId=%s", username, serverId));
        if (response != "YES")
            return false;
        return true;
    }

    private static String httpGetRequest(String url)
    {
        try
        {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Project-Poseidon/0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();
            return response.toString();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
    }
}
