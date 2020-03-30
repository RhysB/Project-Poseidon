package com.projectposeidon.api;

import com.projectposeidon.johnymuffin.UUIDManager;

import java.util.UUID;

public final class PoseidonUUID {

    private PoseidonUUID() {

    }

    /**
     * @param username Username of a player who has connected
     * @return A Mojang UUID if known, otherwise null
     */
    public static UUID getPlayerMojangUUID(String username) {
        return UUIDManager.getInstance().getUUIDFromUsername(username, true);
    }

    /**
     * @param username Username of a player who has connected
     * @return A Mojang UUID if known, otherwise a offline uuid
     */
    public static UUID getPlayerGracefulUUID(String username) {
        return UUIDManager.getInstance().getUUIDGraceful(username);
    }

    /**
     * @param username Username of a player
     * @return A offline UUID for a player
     */
    public static UUID getPlayerOfflineUUID(String username) {
        return UUIDManager.generateOfflineUUID(username);
    }



}
