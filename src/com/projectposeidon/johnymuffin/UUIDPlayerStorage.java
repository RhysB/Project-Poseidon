package com.projectposeidon.johnymuffin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDPlayerStorage {
    //TODO We should probably make our own cache for UUIDs
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
        //TODO we should look at using the modern system: UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + <playerName>).getBytes(Charsets.UTF_8));
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
        //UUIDCacheFile.getInstance().addPlayerDetails(playerName, uuid, true);
    }

    public synchronized UUID getPlayerUUID(String playerName) {
        if (playerUUIDs.containsKey(playerName)) {
            return playerUUIDs.get(playerName);
        }
//        if(UUIDCacheFile.getInstance().isPlayerKnown(playerName, true)) {
//            return UUIDCacheFile.getInstance().getPlayerUUID(playerName, true);
//        }

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
