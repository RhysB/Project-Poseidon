package com.projectposeidon.api;

import com.projectposeidon.johnymuffin.UUIDPlayerStorage;

import java.util.UUID;

public final class PoseidonUUID {

    private PoseidonUUID() {

    }

    /**
     * @param username Username of a player who has connected
     * @return A Mojang UUID if known, otherwise null
     */
    public static UUID getPlayerMojangUUID(String username) {
        return UUIDPlayerStorage.getInstance().getPlayerUUID(username);
    }

    /**
     * @param username Username of a player who has connected
     * @return A Mojang UUID if known, otherwise a offline uuid
     */
    public static UUID getPlayerGracefulUUID(String username) {
        return UUIDPlayerStorage.getInstance().getUUIDGraceful(username);
    }

    /**
     * @param username Username of a player
     * @return A offline UUID for a player
     */
    public static UUID getPlayerOfflineUUID(String username) {
        return UUIDPlayerStorage.generateOfflineUUID(username);
    }



}
