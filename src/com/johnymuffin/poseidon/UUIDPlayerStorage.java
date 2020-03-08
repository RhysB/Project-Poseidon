package com.johnymuffin.poseidon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDPlayerStorage {
    private static UUIDPlayerStorage singleton;
    private Map<String, UUID> playerUUIDs = new HashMap();

    private UUIDPlayerStorage() {

    }

    public UUID getUUIDGraceful(String username) {
        //return UUID.fromString("ca5c33ce-5825-45e3-ab92-0127c05c2016");

        if (playerUUIDs.containsKey(username)) {
            return playerUUIDs.get(username);
        }
        return generateOfflineUUID(username);
    }


    public static UUID generateOfflineUUID(String username) {
        return UUID.nameUUIDFromBytes(username.getBytes());
    }


    private void removeDuplicateUUIDs(UUID uuid) {
        for (String key : playerUUIDs.keySet()) {
            if (playerUUIDs.get(key).equals(uuid)) {
                playerUUIDs.remove(key);
            }
        }
    }

    public synchronized void addPlayerOnlineUUID(String playerName, UUID uuid) {
        if (playerUUIDs.containsKey(playerName)) {
            playerUUIDs.remove(playerName);
        }
        removeDuplicateUUIDs(uuid);
        playerUUIDs.put(playerName, uuid);
    }

    public synchronized UUID getPlayerUUID(String playerName) {
        if (playerUUIDs.containsKey(playerName)) {
            return playerUUIDs.get(playerName);
        }
        return null;
    }

    public boolean isUUIDKnown(String playerName) {
        if (playerUUIDs.containsKey(playerName)) {
            return true;
        }
        return false;
    }

    public boolean isPlayerNameKnown(UUID uuid) {
        for (String key : playerUUIDs.keySet()) {
            if (playerUUIDs.get(key).equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public String getPlayerName(UUID uuid) {
        for (String key : playerUUIDs.keySet()) {
            if (playerUUIDs.get(key).equals(uuid)) {
                return key;
            }
        }
        return null;
    }


    public synchronized static UUIDPlayerStorage getInstance() {
        if (UUIDPlayerStorage.singleton == null) {
            UUIDPlayerStorage.singleton = new UUIDPlayerStorage();
        }
        return UUIDPlayerStorage.singleton;
    }


}
