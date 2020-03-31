package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class PoseidonCommand extends Command {

    public PoseidonCommand(String name) {
        super(name);
        this.description = "Show data regarding the servers version of Project Poseidon";
        this.usageMessage = "/poseidon";
        this.setAliases(Arrays.asList("projectposeidon"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        sender.sendMessage(ChatColor.GRAY + "This server is running " + ChatColor.AQUA + "Project Poseidon" + ChatColor.GRAY + " Version: " + ChatColor.RED + Bukkit.getServer().getPoseidonVersion());
        return true;

    }

}
