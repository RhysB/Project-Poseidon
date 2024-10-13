package com.legacyminecraft.poseidon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowIPCommand extends Command {

    public ShowIPCommand(String name) {
        super(name);
        this.description = "Shows your current IP address as seen by the server.";
        this.usageMessage = "/showip";
        this.setPermission("poseidon.command.showip");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String ipAddress = player.getAddress().getAddress().getHostAddress();

            player.sendMessage("§bYour IP address is: §e" + ipAddress);
        } else {
            sender.sendMessage("§cOnly players can use this command.");
        }

        return true;
    }
}
