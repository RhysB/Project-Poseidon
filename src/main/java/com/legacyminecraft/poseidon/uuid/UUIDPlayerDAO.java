package com.legacyminecraft.poseidon.uuid;

import java.util.UUID;

public interface UUIDPlayerDAO {

    //void addPlayer(UUID uuid, String username, long lastSeen, boolean mojangUUID);

    void addOrUpdatePlayer(UUID uuid, String username, long lastSeen, long last_updated, boolean mojangUUID);

    void removePlayer(UUID uuid, String username);

    void removePlayer(UUID uuid);

    void removePlayer(String username);

    UUID getLatestUUID(String username);

    public UUID getLatestUUID(String username, boolean caseInsensitivity);

    UUID getLatestUUID(String username, boolean caseInsensitivity, boolean mojangUUID);

    String getLatestUsername(UUID uuid);

    long getLastSeen(UUID uuid);

    long getLastUpdated(UUID uuid);

//    long getLastSeen(String username);

    void updateLastSeen(UUID uuid, long lastSeen);

    void updateLastUpdated(UUID uuid, long lastUpdated);

    boolean isPlayerInCache(UUID uuid);

    boolean isPlayerInCache(String username);

    boolean isPlayerInCache(UUID uuid, String username);

    Boolean isMojangUUID(UUID uuid);

    UUID[] getAllPlayers();

    String[] getAllUsernames(UUID uuid);

    void closeDatabase();
}
