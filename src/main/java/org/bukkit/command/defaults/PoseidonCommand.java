package org.bukkit.command.defaults;

import com.projectposeidon.api.PoseidonUUID;
import com.projectposeidon.api.UUIDType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.UUID;

public class PoseidonCommand extends Command {

    public PoseidonCommand(String name) {
        super(name);
        this.description = "Show data regarding the servers version of Project Poseidon";
        this.usageMessage = "/poseidon";
        this.setAliases(Arrays.asList("projectposeidon"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GRAY + "This server is running " + ChatColor.AQUA + "Project Poseidon" + ChatColor.GRAY + " Version: " + ChatColor.RED + Bukkit.getServer().getPoseidonVersion());
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("uuid")) {
                sender.sendMessage(ChatColor.GRAY + "Please specify a user /poseidon uuid (username)");
            } else {
                sender.sendMessage(ChatColor.GRAY + "Unknown sub command.");
            }
        } else {
            if (!args[0].equalsIgnoreCase("uuid")) {
                sender.sendMessage(ChatColor.GRAY + "Unknown sub command.");
            } else {
                UUID uuid = PoseidonUUID.getPlayerUUIDFromCache(args[1], true);
                if (uuid == null) {
                    uuid = PoseidonUUID.getPlayerUUIDFromCache(args[1], false);
                }

                if (uuid == null) {
                    sender.sendMessage(ChatColor.GRAY + "Unable to locate the UUID of the player called: " + ChatColor.WHITE + args[1] + ChatColor.GRAY + ". Please remember usernames are cap sensitive");
                } else {
                    sender.sendMessage(ChatColor.GRAY + "Username: " + args[1]);
                    sender.sendMessage(ChatColor.GRAY + "UUID: " + uuid.toString());
                    UUIDType uuidType = PoseidonUUID.getPlayerUUIDCacheStatus(args[1]);
                    if (uuidType.equals(UUIDType.ONLINE)) {
                        sender.sendMessage(ChatColor.GRAY + "UUID Type: " + ChatColor.GREEN + "Online");
                    } else if (uuidType.equals(UUIDType.OFFLINE)) {
                        sender.sendMessage(ChatColor.GRAY + "UUID Type: " + ChatColor.RED + "Offline");
                    } else {
                        sender.sendMessage(ChatColor.GRAY + "UUID Type: " + ChatColor.DARK_RED + "UNKNOWN");
                    }
                }
            }
        }

        return true;

    }

}
