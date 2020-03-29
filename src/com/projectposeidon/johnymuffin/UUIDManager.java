package com.projectposeidon.johnymuffin;

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
        if(!configFile.exists()) {
            try (FileWriter file = new FileWriter("uuidcache.json")) {
                System.out.println("Generating uuidcache.json for Project Poseidon");
                UUIDJsonArray = new JSONArray();
                file.write(UUIDJsonArray.toJSONString());
                file.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            System.out.println("Reading uuidcache.json for Project Poseidon");
            JSONParser parser = new JSONParser();
            UUIDJsonArray = (JSONArray) parser.parse(new FileReader("uuidcache.json"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void saveJsonArray() {
        try (FileWriter file = new FileWriter("uuidcache.json")) {
            System.out.println("Saving uuidcache.json for Project Poseidon");
            file.write(UUIDJsonArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addUser(String username, UUID uuid, Long expiry, boolean online) {
        removeInstancesOfUsername(username);
        removeInstancesOfUUID(uuid);
        JSONObject tmp = new JSONObject();
        tmp.put("name", username);
        tmp.put("uuid", uuid.toString());
        tmp.put("expiresOn", expiry);
        tmp.put("onlineUUID", online);
        UUIDJsonArray.add(tmp);
    }

    public UUID getUUIDFromUsername(String username, boolean online) {
        for (int i = 0; i < UUIDJsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
            if (tmp.get("name").equals(username) && tmp.get("onlineUUID").equals(true)) {
                return UUID.fromString((String) tmp.get("uuid"));
            }
        }
        return null;
    }

    public String getUsernameFromUUID(UUID uuid) {
        for (int i = 0; i < UUIDJsonArray.size(); i++) {
            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
            if (UUID.fromString((String) tmp.get("uuid")).equals(uuid)) {
                return (String) tmp.get("name");
            }
        }
        return null;
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
                UUIDJsonArray.remove(i);
            }
        }
    }


    public synchronized static UUIDManager getInstance() {
        if (UUIDManager.singleton == null) {
            UUIDManager.singleton = new UUIDManager();
        }
        return UUIDManager.singleton;
    }

}
