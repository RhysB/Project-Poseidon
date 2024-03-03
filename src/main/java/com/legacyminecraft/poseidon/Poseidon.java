package com.legacyminecraft.poseidon;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;

import java.util.LinkedList;

public class Poseidon {

    /**
     * Returns a list of the server's TPS (Ticks Per Second) records for performance monitoring.
     * The list contains Double values indicating the TPS at each second, ordered from most recent to oldest.
     *
     * @return LinkedList<Double> of TPS records.
     */
    public static LinkedList<Double> getTpsRecords() {
        return ((CraftServer) Bukkit.getServer()).getServer().getTpsRecords();
    }


}
