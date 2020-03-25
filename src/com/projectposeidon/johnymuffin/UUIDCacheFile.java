//package com.projectposeidon.johnymuffin;
//
//import org.bukkit.util.config.Configuration;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.UUID;
//
//public class UUIDCacheFile {
//    private static UUIDCacheFile singleton;
//    private Configuration dataFile;
//    private HashMap<String, HashMap<String, String>> UUIDCache = new HashMap<String, HashMap<String, String>>();
//
//    private UUIDCacheFile() {
//        //Generate config and store in hashmap
//        File file = new File("uuid-cache.yml");
//        dataFile = new Configuration(file);
//        try {
//            dataFile.load();
//            if (dataFile.getProperty("data") == null) {
//                dataFile.setProperty("data", (Object) new HashMap());
//            }
//            UUIDCache = (HashMap<String, HashMap<String, String>>) dataFile.getProperty("data");
//        } catch (Exception e) {
//            System.out.println("UUID Cache is corrupt, regenerating file");
//            file.delete();
//            dataFile.load();
//            if (dataFile.getProperty("data") == null) {
//                dataFile.setProperty("data", (Object) new HashMap());
//            }
//            UUIDCache = (HashMap<String, HashMap<String, String>>) dataFile.getProperty("data");
//        }
//
//    }
//    public void removeAllInstanceOfUUID(UUID uuid) {
//        UUIDCache.keySet().removeIf(key -> UUIDCache.get(key).get("uuid").equalsIgnoreCase(uuid.toString()));
//    }
//
//    public int removeExpiredCaches() {
//        int count = 0;
//        long unixTime = System.currentTimeMillis() / 1000L;
//        unixTime = unixTime - 1296000; //Caches expire after 15 days
//        for(String key : UUIDCache.keySet()) {
//            if(Long.valueOf(UUIDCache.get(key).get("unix")) < unixTime) {
//                count = count + 1;
//                UUIDCache.remove(key);
//            }
//        }
//        return count;
//    }
//
//
//    /**
//     * @param username   Username of player
//     * @param uuid       UUID of player
//     * @param mojangUUID Is this a Mojang UUID?
//     */
//    public synchronized void addPlayerDetails(String username, UUID uuid, Boolean mojangUUID) {
//        removeAllInstanceOfUUID(uuid);
//        if(UUIDCache.containsKey(username)) {
//            UUIDCache.remove(username);
//        }
//        final HashMap<String, String> tmp = new HashMap<String, String>();
//        tmp.put("uuid", uuid.toString());
//        long unixTime = System.currentTimeMillis() / 1000L;
//        tmp.put("unix", String.valueOf(unixTime));
//        tmp.put("isMojang", mojangUUID.toString());
//        UUIDCache.put(username, tmp);
//
//    }
//
//    /**
//     * @param username       Username of player
//     * @param mojangUUIDOnly Restrict search to online UUIDs?
//     * @return True if player is known
//     */
//    public synchronized boolean isPlayerKnown(String username, Boolean mojangUUIDOnly) {
//        if (mojangUUIDOnly) {
//            if (UUIDCache.containsKey(username)) {
//                if (Boolean.valueOf(UUIDCache.get(username).get("isMojang"))) {
//                    return true;
//                }
//            }
//        } else {
//            if (UUIDCache.containsKey(username)) {
//                return true;
//            }
//        }
//        return false;
//
//    }
//
//    /**
//     * @param username Username of player
//     * @param mojangUUIDOnly Restrict getting a UUID to a online user
//     * @return UUID of player if in cache, otherwise null
//     */
//    public synchronized UUID getPlayerUUID(String username, Boolean mojangUUIDOnly) {
//        if(mojangUUIDOnly) {
//            if (UUIDCache.containsKey(username)) {
//                if (Boolean.valueOf(UUIDCache.get(username).get("isMojang"))) {
//                    return getUUID(UUIDCache.get(username).get("uuid"));
//
//                }
//            }
//        } else {
//            if (UUIDCache.containsKey(username)) {
//                if (!Boolean.valueOf(UUIDCache.get(username).get("isMojang"))) {
//                    return getUUID(UUIDCache.get(username).get("uuid"));
//
//                }
//            }
//        }
//        return null;
//    }
//
//    private static UUID getUUID(String id) {
//        //return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" +id.substring(20, 32));
//        return UUID.fromString(id);
//    }
//
//    public void saveConfig() {
//        dataFile.setProperty("data", UUIDCache);
//        dataFile.save();
//    }
//
//
//    public synchronized static UUIDCacheFile getInstance() {
//        if (UUIDCacheFile.singleton == null) {
//            UUIDCacheFile.singleton = new UUIDCacheFile();
//        }
//        return UUIDCacheFile.singleton;
//    }
//
//}
