package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Comparator;

public class PluginsCommand extends Command {
    public PluginsCommand(String name) {
        super(name);
        this.description = "Gets a list of plugins running on the server";
        this.usageMessage = "/plugins";
        this.setPermission("bukkit.command.plugins");
        this.setAliases(Arrays.asList("pl"));
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        
        sender.sendMessage("Plugins" + getPluginList());
        return true;
    }

    private String getPluginList() {
        StringBuilder pluginList = new StringBuilder();
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        int enabled = 0;
        int pluginCount = 0;

        Arrays.sort(plugins, Comparator.comparing(o -> o.getDescription().getFullName()));

        for (Plugin plugin : plugins) {
            if (!plugin.getDescription().isVisible() && plugin.isEnabled()) {
                continue;
            }
            pluginCount = pluginCount + 1;

            if (pluginList.length() > 0) {
                pluginList.append(ChatColor.WHITE);
                pluginList.append(", ");
            }

            pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
            if (plugin.isEnabled())
                enabled++;
            pluginList.append(plugin.getDescription().getName());
        }

        return " (" + enabled + "/" + pluginCount + "): " + pluginList.toString();
    }
}
