package com.projectposeidon;

import org.bukkit.util.config.Configuration;

import java.io.File;

public class PoseidonConfig extends Configuration {
    private static PoseidonConfig singleton;

    private PoseidonConfig() {
        super(new File("poseidon.yml"));
        this.reload();
    }

    public void reload() {
        this.load();
        this.write();
        this.save();
    }

    private void write() {
        if (this.getString("config-version") == null) {
            System.out.println("Converting to Config Version 1");
            convertToNewConfig();
        }
        //Main
        generateConfigOption("config-version", 1);
        //Setting
        generateConfigOption("settings.allow-graceful-uuids", true);
        generateConfigOption("settings.delete-duplicate-uuids", false);
        generateConfigOption("settings.save-playerdata-by-uuid", true);
        generateConfigOption("settings.per-day-logfile", false);
        //Word Settings
        generateConfigOption("world-settings.optimized-explosions", false);
        generateConfigOption("world-settings.randomize-spawn", true);
        generateConfigOption("world-settings.teleport-to-highest-safe-block", true);


    }


    private void generateConfigOption(String key, Object defaultValue) {
        if (this.getProperty(key) == null) {
            this.setProperty(key, defaultValue);
        }
        final Object value = this.getProperty(key);
        this.removeProperty(key);
        this.setProperty(key, value);
    }

    public Object getConfigOption(String key) {
        return this.getProperty(key);
    }

    private synchronized void convertToNewConfig() {
        //Graceful UUIDS
        convertToNewAddress("settings.allow-graceful-uuids", "allowGracefulUUID");
        convertToNewAddress("settings.save-playerdata-by-uuid", "savePlayerdataByUUID");
        convertToNewAddress("world-settings.optimized-explosions", "optimizedExplosions");
    }

    private boolean convertToNewAddress(String newKey, String oldKey) {
        if (this.getString(newKey) != null) {
            return false;
        }
        if (this.getString(oldKey) == null) {
            return false;
        }
        System.out.println("Converting Config: " + oldKey + " to " + newKey);
        Object value = this.getProperty(oldKey);
        this.setProperty(newKey, value);
        this.removeProperty(oldKey);
        return true;

    }


    public synchronized static PoseidonConfig getInstance() {
        if (PoseidonConfig.singleton == null) {
            PoseidonConfig.singleton = new PoseidonConfig();
        }
        return PoseidonConfig.singleton;
    }

}
