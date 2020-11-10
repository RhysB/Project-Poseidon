package com.projectposeidon;

import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.UUID;

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
        generateConfigOption("settings.fetch-uuids-from", "https://api.mojang.com/profiles/minecraft");
        generateConfigOption("settings.enable-watchdog", true)
        //Statistics
        generateConfigOption("settings.statistics.key", UUID.randomUUID().toString());
        generateConfigOption("settings.enable-statistics", true);
        //Word Settings
        generateConfigOption("world-settings.optimized-explosions", false);
        generateConfigOption("world-settings.randomize-spawn", true);
        generateConfigOption("world-settings.teleport-to-highest-safe-block", true);
        generateConfigOption("world-settings.use-modern-fence-bounding-boxes", false);
        //Release2Beta Settings
        generateConfigOption("settings.release2beta.enable-ip-pass-through", false);
        generateConfigOption("settings.release2beta.proxy-ip", "127.0.0.1");
        //Modded Jar Support
        generateConfigOption("settings.support.modloader.enable", false);
        generateConfigOption("settings.support.modloader.info", "EXPERIMENTAL support for ModloaderMP.");


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

    public Object getConfigOption(String key, Object defaultValue) {
        Object value = getConfigOption(key);
        if (value == null) {
            value = defaultValue;
        }
        return value;

    }

    private void convertToNewConfig() {
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
