package com.legacyminecraft.poseidon;

import com.legacyminecraft.poseidon.uuid.UUIDPlayerDAO;
import com.legacyminecraft.poseidon.watchdog.WatchDogThread;
import com.projectposeidon.johnymuffin.UUIDManager;

/**
 * Core class for Poseidon.
 */

public class Poseidon {
    private static UUIDManager uuidManager;
    private static UUIDPlayerDAO uuidPlayerDAO;

    private static WatchDogThread watchDogThread;

    // This is a singleton class, so we can use a static initializer block
    static {
        uuidManager = UUIDManager.getInstance();
    }

    private Poseidon() {
    }


    public static UUIDPlayerDAO getUUIDPlayerDAO() {
        return uuidPlayerDAO;
    }

    @Deprecated
    public static UUIDManager getUUIDManager() {
        return uuidManager;
    }

    public static WatchDogThread getWatchDogThread() {
        return watchDogThread;
    }

    public static void setUUIDPlayerDAO(UUIDPlayerDAO uuidPlayerDAO) {
        if (Poseidon.uuidPlayerDAO != null) {
            throw new UnsupportedOperationException("UUIDPlayerDAO is already set! This is a singleton!");
        }
        Poseidon.uuidPlayerDAO = uuidPlayerDAO;
    }

    public static void setWatchDogThread(WatchDogThread watchDogThread) {
        if (Poseidon.watchDogThread != null) {
            throw new UnsupportedOperationException("WatchDogThread is already set! This is a singleton!");
        }
        Poseidon.watchDogThread = watchDogThread;
    }


}
