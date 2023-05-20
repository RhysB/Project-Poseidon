package org.bukkit.command.defaults;

import org.bukkit.command.Command;

import java.util.List;

public abstract class VanillaCommand extends Command {
    protected VanillaCommand(String name) {
        super(name);
    }

    protected VanillaCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    public abstract boolean matches(String input);
}
