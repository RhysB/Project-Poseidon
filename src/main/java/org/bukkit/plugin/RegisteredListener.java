package org.bukkit.plugin;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

/**
 * Stores relevant information for plugin listeners
 */
public class RegisteredListener {
    private final Listener listener;
    private final Event.Priority priority;
    private final Plugin plugin;
    private final EventExecutor executor;
    private final boolean ignoreCancelled;

    public RegisteredListener(final Listener pluginListener, final EventExecutor eventExecutor, final Event.Priority eventPriority, final Plugin registeredPlugin) {
        this(pluginListener, eventExecutor, eventPriority, registeredPlugin, false);
    }

    public RegisteredListener(final Listener pluginListener, final EventExecutor eventExecutor, final Event.Priority eventPriority, final Plugin registeredPlugin, final boolean ignoreCancelled) {
        this.listener = pluginListener;
        this.priority = eventPriority;
        this.plugin = registeredPlugin;
        this.executor = eventExecutor;
        this.ignoreCancelled = ignoreCancelled;
    }

    public RegisteredListener(final Listener pluginListener, final Event.Priority eventPriority, final Plugin registeredPlugin, Event.Type type) {
        this(pluginListener, registeredPlugin.getPluginLoader().createExecutor(type, pluginListener), eventPriority, registeredPlugin, false);
    }

    public void registerAll() {

    }

    /**
     * Gets the listener for this registration
     * @return Registered Listener
     */
    public Listener getListener() {
        return listener;
    }

    /**
     * Gets the plugin for this registration
     * @return Registered Plugin
     */
    public Plugin getPlugin() {
        return plugin;
    }

    /**
     * Gets the priority for this registration
     * @return Registered Priority
     */
    public Event.Priority getPriority() {
        return priority;
    }

    /**
     * Calls the event executor
     * @return Registered Priority
     */
    public void callEvent(Event event) {
        if(event instanceof Cancellable) {
            if(((Cancellable) event).isCancelled() && ignoreCancelled) {
                return;
            }
        }
        executor.execute(listener, event);
    }
}
