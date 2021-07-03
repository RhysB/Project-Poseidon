package org.bukkit.command.defaults;

import com.legacyminecraft.poseidon.PoseidonPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

public class StopCommand extends VanillaCommand {
    public StopCommand() {
        super("stop");
        this.description = "Stops the server";
        this.usageMessage = "/stop";
        this.setPermission("bukkit.command.stop");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        Command.broadcastCommandMessage(sender, "Starting Server Shutdown, Saving Data.");

        ((CraftServer) Bukkit.getServer()).setShuttingdown(true);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.saveData();
            player.kickPlayer(ChatColor.RED + "Server is shutting down, please rejoin later.");
        }
        for (World world : Bukkit.getWorlds()) {
            world.save();
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(new PoseidonPlugin(), () -> {
            Command.broadcastCommandMessage(sender, "Stopping the server..");
            Bukkit.shutdown();
        }, 100);

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.startsWith("stop");
    }
}
