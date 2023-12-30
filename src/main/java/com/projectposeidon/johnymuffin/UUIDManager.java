package com.projectposeidon.johnymuffin;

import com.legacyminecraft.poseidon.Poseidon;
import com.legacyminecraft.poseidon.PoseidonConfig;

import java.util.UUID;

@Deprecated
public class UUIDManager {
    private static UUIDManager singleton;
//    private JSONArray UUIDJsonArray;

    @Deprecated
    private UUIDManager() {
//        File configFile = new File("uuidcache.json");
//        //Check if uuidcache.json exists
//        if (!configFile.exists()) {
//            try (FileWriter file = new FileWriter("uuidcache.json")) {
//                System.out.println("[Poseidon] Generating uuidcache.json for Project Poseidon");
//                UUIDJsonArray = new JSONArray();
//                file.write(UUIDJsonArray.toJSONString());
//                file.flush();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            System.out.println("[Poseidon] Reading uuidcache.json for Project Poseidon");
//            JSONParser parser = new JSONParser();
//            UUIDJsonArray = (JSONArray) parser.parse(new FileReader("uuidcache.json"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            System.out.println("[Poseidon] The UUIDCache is corrupt or unreadable, resetting");
//            UUIDJsonArray = new JSONArray();
//            saveJsonArray();
//
//            //e.printStackTrace();
//        } catch (Exception e) {
//            System.out.println("[Poseidon] Error reading uuidcache.json, changing to memory only cache: " + e + ": " + e.getMessage());
//            UUIDJsonArray = new JSONArray();
//        }
//

    }

    @Deprecated
    public UUID getUUIDGraceful(String username) {
        //Attempt to get Mojang UUID first
        UUID uuid = Poseidon.getUUIDPlayerDAO().getLatestUUID(username, false, true);

        //Attempt to get stored cracked UUID
        if (uuid == null) {
            //TODO: Unsure if this call should be case-insensitive or not. Player data for other plugins depending on OS is already case-insensitive
            uuid = Poseidon.getUUIDPlayerDAO().getLatestUUID(username, true, false);
        }

        //Generate offline UUID if all else fails
        if (uuid == null) {
            uuid = generateOfflineUUID(username);
        }

        return uuid;
    }

    @Deprecated
    public static UUID generateOfflineUUID(String username) {
        //TODO we should look at using the modern system: UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + <playerName>).getBytes(Charsets.UTF_8));
        return UUID.nameUUIDFromBytes(username.getBytes());
    }


//    public void saveJsonArray() {
//        try (FileWriter file = new FileWriter("uuidcache.json")) {
//            System.out.println("[Poseidon] Saving UUID Cache");
//            file.write(UUIDJsonArray.toJSONString());
//            file.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Deprecated
    public void receivedUUID(String username, UUID uuid, Long currentUnixTime, boolean online) {
        Poseidon.getUUIDPlayerDAO().addOrUpdatePlayer(uuid, username, currentUnixTime, currentUnixTime, online);
//        //Check if the UUID is already in the cache, if so update the expiry
//        for (int i = 0; i < UUIDJsonArray.size(); i++) {
//            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
//            if (tmp.get("name").equals(username) && UUID.fromString((String) tmp.get("uuid")).equals(uuid) && tmp.get("onlineUUID").equals(online)) {
//                tmp.replace("expiresOn", expiry);
//                UUIDJsonArray.set(i, tmp);
//                return;
//            }
//        }
//        removeInstancesOfUsername(username);
//        removeInstancesOfUUID(uuid);
//        addUser(username, uuid, expiry, online);
//
    }

    @Deprecated
    public UUID getUUIDFromUsername(String username) {
        //This is case-sensitive so I'll continue this behaviour for now
        return Poseidon.getUUIDPlayerDAO().getLatestUUID(username, false);

//        for (int i = 0; i < UUIDJsonArray.size(); i++) {
//            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
//            if (tmp.get("name").equals(username)) {
//                return UUID.fromString((String) tmp.get("uuid"));
//            }
//        }
//        return null;
    }


    @Deprecated
    public UUID getUUIDFromUsername(String username, boolean online) {
        //This is case-sensitive so I'll continue this behaviour for now
        UUID uuid = Poseidon.getUUIDPlayerDAO().getLatestUUID(username, false, online);

//        //TODO: Unsure if this fallback should exist. Possibly some weird edge cases where this is needed???
//        if(uuid == null) {
//            uuid = Poseidon.getUUIDPlayerDAO().getLatestUUID(username, true, online);
//        }

        return uuid;


//        for (int i = 0; i < UUIDJsonArray.size(); i++) {
//            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
//            if (tmp.get("name").equals(username) && tmp.get("onlineUUID").equals(online)) {
//                return UUID.fromString((String) tmp.get("uuid"));
//            }
//        }
//        return null;
    }

    @Deprecated
    public UUID getUUIDFromUsername(String username, boolean online, Long unixTime) {
        //Long unixTime is the current time. Server should only get records that have not expired

        UUID uuid = Poseidon.getUUIDPlayerDAO().getLatestUUID(username, false, online);

        if (uuid == null) {
            return null;
        }

        //Check if the UUID is expired
        //TODO: A last_updated column would be nice here
        long currentUnixTime = System.currentTimeMillis() / 1000L;
        long last_seen = Poseidon.getUUIDPlayerDAO().getLastUpdated(uuid);
        long expiry = last_seen + PoseidonConfig.getInstance().getConfigLong("settings.uuid.expiry-time.value");

        if (expiry < currentUnixTime) {
            return null;
        }

        return uuid;

//        for (int i = 0; i < UUIDJsonArray.size(); i++) {
//            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
//            Long expire = Long.valueOf(String.valueOf(tmp.get("expiresOn")));
//            if (tmp.get("name").equals(username) && tmp.get("onlineUUID").equals(online) && expire > afterUnix) {
//                return UUID.fromString((String) tmp.get("uuid"));
//            }
//        }
//        return null;
    }

    @Deprecated
    public String getUsernameFromUUID(UUID uuid) {
        // Get most recent username from UUID

        return Poseidon.getUUIDPlayerDAO().getLatestUsername(uuid);

//        String username = null;
//        long expiry = -1;
//        for (int i = 0; i < UUIDJsonArray.size(); i++) {
//            JSONObject playerEntry = (JSONObject) UUIDJsonArray.get(i);
//            UUID entryUUID = UUID.fromString(String.valueOf(playerEntry.get("uuid")));
//            long expiresOn = Long.valueOf(String.valueOf(playerEntry.get("expiresOn")));
//            if (entryUUID.equals(uuid) && expiresOn >= expiry) {
//                expiry = expiresOn;
//                username = String.valueOf(playerEntry.get("name"));
//            }
//        }
//        return username;
    }

    @Deprecated
    private void removeInstancesOfUsername(String username) {
        Poseidon.getUUIDPlayerDAO().removePlayer(username);


//        for (int i = 0; i < UUIDJsonArray.size(); i++) {
//            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
//            if (tmp.get("name").equals(username)) {
//                UUIDJsonArray.remove(i);
//            }
//        }
    }

    @Deprecated
    private void removeInstancesOfUUID(UUID uuid) {
        Poseidon.getUUIDPlayerDAO().removePlayer(uuid);

//        for (int i = 0; i < UUIDJsonArray.size(); i++) {
//            JSONObject tmp = (JSONObject) UUIDJsonArray.get(i);
//            if (UUID.fromString((String) tmp.get("uuid")).equals(uuid)) {
//                if ((boolean) PoseidonConfig.getInstance().getConfigOption("settings.delete-duplicate-uuids")) {
//                    //Remove the duplicate UUID
//                    UUIDJsonArray.remove(i);
//                    //Decrement i to account for the removed element
//                    i--;
//                } else {
//                    //This allows for plugins to use a old username and find UUID.
//                    tmp.replace("expiresOn", 1);
//                    UUIDJsonArray.set(i, tmp);
//                }
//            }
//        }
    }

    @Deprecated
    public static UUIDManager getInstance() {
        if (UUIDManager.singleton == null) {
            UUIDManager.singleton = new UUIDManager();
        }
        return UUIDManager.singleton;
    }

}
