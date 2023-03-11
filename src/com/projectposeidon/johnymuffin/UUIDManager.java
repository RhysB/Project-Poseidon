package com.projectposeidon.johnymuffin;

import com.legacyminecraft.poseidon.PoseidonConfig;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class UUIDManager {
    private static UUIDManager singleton;
    private JSONArray UUIDJsonArray;

    private UUIDManager() {
        File configFile = new File("uuidcache.json");
        //Check if uuidcache.json exists
        if (!configFile.exists()) {
            try (FileWriter file = new FileWriter("uuidcache.json")) {
                System.out.println("[Poseidon] Generating uuidcache.json for Project Poseidon");
                UUIDJsonArray = new JSONArray();
                file.write(UUIDJsonArray.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            System.out.println("[Poseidon] Reading uuidcache.json for Project Poseidon");
            JSONParser parser = new JSONParser();
            UUIDJsonArray = (JSONArray) parser.parse(new FileReader("uuidcache.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("[Poseidon] The UUIDCache is corrupt or unreadable, resetting");
            UUIDJsonArray = new JSONArray();
            saveJsonArray();

            //e.printStackTrace();
        } catch (Exception e) {
            System.out.println("[Poseidon] Error reading uuidcache.json, changing to memory only cache: " + e + ": " + e.getMessage());
            UUIDJsonArray = new JSONArray();
        }


    }

    public UUID getUUIDGraceful(String username) {
        UUID uuid = getUUIDFromUsername(username, true);
        if (uuid == null) {
            uuid = generateOfflineUUID(username);
        }
        return uuid;
    }


    public static UUID generateOfflineUUID(String username) {
        //TODO we should look at using the modern system: UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + <playerName>).getBytes(Charsets.UTF_8));
        return UUID.nameUUIDFromBytes(username.getBytes());
    }


    public void saveJsonArray() {
        try (FileWriter file = new FileWriter("uuidcache.json")) {
            System.out.println("[Poseidon] Saving UUID Cache");
            file.write(UUIDJsonArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receivedUUID(String username, UUID uuid, Long expiry, boolean online) {
        //Check if the UUID is already in the cache, if so update the expiry
        for (int i = 0; i < UUIDJsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
            if (tmp.get("name").equals(username) && UUID.fromString((String) tmp.get("uuid")).equals(uuid) && tmp.get("onlineUUID").equals(online)) {
                tmp.replace("expiresOn", expiry);
                UUIDJsonArray.set(i, tmp);
                return;
            }
        }
        removeInstancesOfUsername(username);
        removeInstancesOfUUID(uuid);
        addUser(username, uuid, expiry, online);


    }


    private void addUser(String username, UUID uuid, Long expiry, boolean online) {
        JSONObject tmp = new JSONObject();
        tmp.put("name", username);
        tmp.put("uuid", uuid.toString());
        tmp.put("expiresOn", expiry);
        tmp.put("onlineUUID", online);
        UUIDJsonArray.add(tmp);
    }

    public UUID getUUIDFromUsername(String username) {
        for (int i = 0; i < UUIDJsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
            if (tmp.get("name").equals(username)) {
                return UUID.fromString((String) tmp.get("uuid"));
            }
        }
        return null;
    }


    public UUID getUUIDFromUsername(String username, boolean online) {
        for (int i = 0; i < UUIDJsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
            if (tmp.get("name").equals(username) && tmp.get("onlineUUID").equals(online)) {
                return UUID.fromString((String) tmp.get("uuid"));
            }
        }
        return null;
    }

    public UUID getUUIDFromUsername(String username, boolean online, Long afterUnix) {
        for (int i = 0; i < UUIDJsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
            Long expire = Long.valueOf(String.valueOf(tmp.get("expiresOn")));
            if (tmp.get("name").equals(username) && tmp.get("onlineUUID").equals(online) && expire > afterUnix) {
                return UUID.fromString((String) tmp.get("uuid"));
            }
        }
        return null;
    }

    public String getUsernameFromUUID(UUID uuid) {
        // Get most recent username from UUID
        String username = null;
        long expiry = -1;
        for (int i = 0; i < UUIDJsonArray.size(); i++) {
            JSONObject playerEntry = (JSONObject) UUIDJsonArray.get(i);
            UUID entryUUID = UUID.fromString(String.valueOf(playerEntry.get("uuid")));
            long expiresOn = Long.valueOf(String.valueOf(playerEntry.get("expiresOn")));
            if (entryUUID.equals(uuid) && expiresOn >= expiry) {
                expiry = expiresOn;
                username = String.valueOf(playerEntry.get("name"));
            }
        }
        return username;
    }

    private void removeInstancesOfUsername(String username) {
        for (int i = 0; i < UUIDJsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
            if (tmp.get("name").equals(username)) {
                UUIDJsonArray.remove(i);
            }
        }
    }

    private void removeInstancesOfUUID(UUID uuid) {
        for (int i = 0; i < UUIDJsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
            if (UUID.fromString((String) tmp.get("uuid")).equals(uuid)) {
                if ((boolean) PoseidonConfig.getInstance().getConfigOption("settings.delete-duplicate-uuids")) {
                    //Remove the duplicate UUID
                    UUIDJsonArray.remove(i);
                    //Decrement i to account for the removed element
                    i--;
                } else {
                    //This allows for plugins to use a old username and find UUID.
                    tmp.replace("expiresOn", 1);
                    UUIDJsonArray.set(i, tmp);
                }
            }
        }
    }


    public static UUIDManager getInstance() {
        if (UUIDManager.singleton == null) {
            UUIDManager.singleton = new UUIDManager();
        }
        return UUIDManager.singleton;
    }

}
