package com.projectposeidon.johnymuffin;

import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class UUIDCacheFile {
    private static UUIDCacheFile singleton;
    private Configuration dataFile;
    private HashMap<String, HashMap<String, String>> UUIDCache = new HashMap<String, HashMap<String, String>>();

    private UUIDCacheFile() {
        dataFile = new Configuration(new File("uuid-cache.yml"));
        dataFile.load();
        if (dataFile.getProperty("data") == null) {
            dataFile.setProperty("data", (Object) new HashMap());
        }
        UUIDCache = (HashMap<String, HashMap<String, String>>) dataFile.getProperty("data");

    }

    /**
     * @param username   Username of player
     * @param uuid       UUID of player
     * @param mojangUUID Is this a Mojang UUID?
     */
    public void addPlayerDetails(String username, UUID uuid, Boolean mojangUUID) {
        final HashMap<String, String> tmp = new HashMap<String, String>();
        tmp.put("uuid", uuid.toString());
        long unixTime = System.currentTimeMillis() / 1000L;
        tmp.put("unix", String.valueOf(unixTime));
        tmp.put("isMojang", mojangUUID.toString());
        UUIDCache.put(username, tmp);

    }

    /**
     * @param username       Username of player
     * @param mojangUUIDOnly Restrict search to online UUIDs?
     * @return True if player is known
     */
    public boolean isPlayerKnown(String username, Boolean mojangUUIDOnly) {
        if (mojangUUIDOnly) {
            if (UUIDCache.containsKey(username)) {
                if (Boolean.valueOf(UUIDCache.get(username).get("isMojang"))) {
                    return true;
                }
            }
        } else {
            if (UUIDCache.containsKey(username)) {
                return true;
            }
        }
        return false;

    }

    /**
     * @param username Username of player
     * @param mojangUUIDOnly Restrict getting a UUID to a online user
     * @return UUID of player if in cache, otherwise null
     */
    public UUID getPlayerUUID(String username, Boolean mojangUUIDOnly) {
        if(mojangUUIDOnly) {
            if (UUIDCache.containsKey(username)) {
                if (Boolean.valueOf(UUIDCache.get(username).get("isMojang"))) {
                    return getUUID(UUIDCache.get(username).get("uuid"));

                }
            }
        } else {
            if (UUIDCache.containsKey(username)) {
                if (!Boolean.valueOf(UUIDCache.get(username).get("isMojang"))) {
                    return getUUID(UUIDCache.get(username).get("uuid"));

                }
            }
        }
        return null;
    }

    private static UUID getUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" +id.substring(20, 32));
    }

    public void saveConfig() {
        dataFile.setProperty("data", UUIDCache);
        dataFile.save();
    }


    public synchronized static UUIDCacheFile getInstance() {
        if (UUIDCacheFile.singleton == null) {
            UUIDCacheFile.singleton = new UUIDCacheFile();
        }
        return UUIDCacheFile.singleton;
    }

}
