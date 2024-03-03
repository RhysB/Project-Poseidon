package com.legacyminecraft.poseidon.commands;

import com.legacyminecraft.poseidon.Poseidon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.VanillaCommand;

import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.Map;

public class TPSCommand extends Command {

    private final LinkedHashMap<String, Integer> intervals = new LinkedHashMap<>();

    public TPSCommand(String name) {
        super(name);
        this.description = "Shows the server's TPS for various intervals";
        this.usageMessage = "/tps";
        this.setPermission("poseidon.command.tps");

        // Define the intervals for TPS calculation
        intervals.put("5s", 5);
        intervals.put("30s", 30);
        intervals.put("1m", 60);
        intervals.put("5m", 300);
        intervals.put("10m", 600);
        intervals.put("15m", 900);
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        LinkedList<Double> tpsRecords = Poseidon.getTpsRecords();
        StringBuilder message = new StringBuilder("§bServer TPS: ");

        // Calculate and format TPS for each interval dynamically
        for (Map.Entry<String, Integer> entry : intervals.entrySet()) {
            double averageTps = calculateAverage(tpsRecords, entry.getValue());
            message.append(formatTps(averageTps)).append(" (").append(entry.getKey()).append("), ");
        }

        // Remove the trailing comma and space
        if (message.length() > 0) {
            message.setLength(message.length() - 2);
        }

        sender.sendMessage(message.toString());
        return true;
    }

    private double calculateAverage(LinkedList<Double> records, int seconds) {
        int size = Math.min(records.size(), seconds);
        if (size == 0) return 20.0;

        double total = 0;
        for (int i = 0; i < size; i++) {
            total += records.get(i);
        }
        return total / size;
    }

    private String formatTps(double tps) {
        String colorCode;
        if (tps >= 19) {
            colorCode = "§a";
        } else if (tps >= 15) {
            colorCode = "§e";
        } else {
            colorCode = "§c";
        }
        return colorCode + String.format("%.2f", tps);
    }
}
