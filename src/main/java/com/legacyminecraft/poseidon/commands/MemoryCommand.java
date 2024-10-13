package com.legacyminecraft.poseidon.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MemoryCommand extends Command {

    public MemoryCommand(String name) {
        super(name);
        this.description = "Displays the current memory usage of the server.";
        this.usageMessage = "/memory";
        this.setPermission("poseidon.command.memory");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;

        long totalMemoryMB = totalMemory / (1024 * 1024);
        long usedMemoryMB = usedMemory / (1024 * 1024);
        long freeMemoryMB = freeMemory / (1024 * 1024);

        String message = String.format("§bMemory Usage: §eTotal: %d MB, Used: %d MB, Free: %d MB",
                totalMemoryMB, usedMemoryMB, freeMemoryMB);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(message);
        } else {
            sender.sendMessage(message);
        }

        return true;
    }
}
