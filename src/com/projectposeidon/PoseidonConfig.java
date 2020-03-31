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
        isAllowGracefulUUIDEnabled();
        isUUIDPlayerDataEnabled();
        isExplosionsOptimized();
    }

    public synchronized Boolean isAllowGracefulUUIDEnabled() {
        String key = "allowGracefulUUID";
        if (this.getString(key) == null) {
            this.setProperty(key, true);
        }
        final Boolean setting = this.getBoolean(key, true);
        this.removeProperty(key);
        this.setProperty(key, setting);
        return setting;

    }

    public synchronized Boolean isUUIDPlayerDataEnabled() {
        String key = "savePlayerdataByUUID";
        if (this.getString(key) == null) {
            this.setProperty(key, true);
        }
        final Boolean setting = this.getBoolean(key, true);
        this.removeProperty(key);
        this.setProperty(key, setting);
        return setting;

    }

    public synchronized Boolean isExplosionsOptimized() {
        String key = "optimizedExplosions";
        if (this.getString(key) == null) {
            this.setProperty(key, false);
        }
        final Boolean setting = this.getBoolean(key, false);
        this.removeProperty(key);
        this.setProperty(key, setting);
        return setting;

    }

    public synchronized static PoseidonConfig getInstance() {
        if (PoseidonConfig.singleton == null) {
            PoseidonConfig.singleton = new PoseidonConfig();
        }
        return PoseidonConfig.singleton;
    }

}
