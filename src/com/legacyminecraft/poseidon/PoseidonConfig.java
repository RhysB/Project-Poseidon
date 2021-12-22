package com.legacyminecraft.poseidon;

import org.bukkit.util.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

public class PoseidonConfig extends Configuration {
    private static PoseidonConfig singleton;
    private final int configVersion = 2;
    private Integer[] treeBlacklistIDs;

    public Integer[] getTreeBlacklistIDs() {
        return treeBlacklistIDs;
    }

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
        if (this.getString("config-version") == null || Integer.valueOf(this.getString("config-version")) < configVersion) {
            System.out.println("Converting to Config Version: " + configVersion);
            convertToNewConfig();
        }
        //Main
        generateConfigOption("config-version", 2);
        //Setting
        generateConfigOption("settings.allow-graceful-uuids", true);
        generateConfigOption("settings.delete-duplicate-uuids", false);
        generateConfigOption("settings.save-playerdata-by-uuid", true);
        generateConfigOption("settings.per-day-logfile", false);
        generateConfigOption("settings.fetch-uuids-from", "https://api.mojang.com/profiles/minecraft");
        generateConfigOption("settings.enable-watchdog", true);
        generateConfigOption("settings.remove-join-leave-debug", true);
        generateConfigOption("settings.enable-tpc-nodelay", false);
        generateConfigOption("settings.use-get-for-uuids.enabled", false);
        generateConfigOption("settings.use-get-for-uuids.info", "This setting causes the server to use the GET method for Username to UUID conversion. This is useful incase the POST method goes offline.");
        //Packet Events
        generateConfigOption("settings.packet-events.enabled", false);
        generateConfigOption("settings.packet-events.info", "This setting causes the server to fire a Bukkit event for each packet received and sent to a player once they have finished the initial login process. This only needs to be enabled if you have a plugin that uses this specific feature.");
        //Statistics
        generateConfigOption("settings.statistics.key", UUID.randomUUID().toString());
        generateConfigOption("settings.statistics.enabled", true);
        //Word Settings
        generateConfigOption("world-settings.optimized-explosions", false);
        generateConfigOption("world-settings.randomize-spawn", true);
        generateConfigOption("world-settings.teleport-to-highest-safe-block", true);
        generateConfigOption("world-settings.use-modern-fence-bounding-boxes", false);
        //TODO: Actually implement the tree growth functionality stuff
        generateConfigOption("world.settings.block-tree-growth.enabled", true);
        generateConfigOption("world.settings.block-tree-growth.list", "54,63,68");
        generateConfigOption("world.settings.block-tree-growth.info", "This setting allows for server owners to easily block trees growing from automatically destroying certain blocks. The list must be a string with numerical item ids separated by commas.");
        generateConfigOption("world.settings.block-pistons-pushing-furnaces.info", "This workaround prevents pistons from pushing furnaces which prevents a malicious server crash.");
        generateConfigOption("world.settings.block-pistons-pushing-furnaces.enabled", true);
        //Release2Beta Settings
        generateConfigOption("settings.release2beta.enable-ip-pass-through", false);
        generateConfigOption("settings.release2beta.proxy-ip", "127.0.0.1");
        //Modded Jar Support
        generateConfigOption("settings.support.modloader.enable", false);
        generateConfigOption("settings.support.modloader.info", "EXPERIMENTAL support for ModloaderMP.");
        //Offline Username Check
        generateConfigOption("settings.check-username-validity.enabled", true);
        generateConfigOption("settings.check-username-validity.info", "If enabled, verifies the validity of a usernames of cracked players.");
        generateConfigOption("settings.check-username-validity.regex", "[a-zA-Z0-9_?]*");
        generateConfigOption("settings.check-username-validity.max-length", 16);
        generateConfigOption("settings.check-username-validity.min-length", 3);


        //Tree Leave Destroy Blacklist

        if (Boolean.valueOf(String.valueOf(getConfigOption("world.settings.block-tree-growth.enabled", true)))) {
            if (String.valueOf(this.getConfigOption("world.settings.block-tree-growth.list", "")).trim().isEmpty()) {
                //Empty Blacklist
            } else {
                String[] rawBlacklist = String.valueOf(this.getConfigOption("world.settings.block-tree-growth.list", "")).trim().split(",");
                int blackListCount = 0;
                for (String stringID : rawBlacklist) {
                    if (Pattern.compile("-?[0-9]+").matcher(stringID).matches()) {
                        blackListCount = blackListCount + 1;
                    } else {
                        System.out.println("The ID " + stringID + " for leaf decay blocker has been detected as invalid, and won't be used.");
                    }
                }
                //Loop a second time to get correct array length. I know this is horrible code, but it works and only runs on server startup.
                treeBlacklistIDs = new Integer[blackListCount];
                int i = 0;
                for (String stringID : rawBlacklist) {
                    if (Pattern.compile("-?[0-9]+").matcher(stringID).matches()) {
                        treeBlacklistIDs[i] = Integer.valueOf(stringID);
                        i = i + 1;
                    }
                }
                System.out.println("Leaf blocks can't replace the following block IDs: " + Arrays.toString(treeBlacklistIDs));
            }
        } else {
            treeBlacklistIDs = new Integer[0];
        }

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
        convertToNewAddress("settings.statistics.enabled", "settings.enable-statistics");
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
