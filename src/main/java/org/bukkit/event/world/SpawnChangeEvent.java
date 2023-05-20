package org.bukkit.event.world;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * An event that is called when a world's spawn changes. The
 * world's previous spawn location is included.
 */
public class SpawnChangeEvent extends WorldEvent {
    private Location previousLocation;

    public SpawnChangeEvent(World world, Location previousLocation) {
        super(Type.SPAWN_CHANGE, world);
        this.previousLocation = previousLocation;
    }

    /**
     * Gets the previous spawn location
     *
     * @return Location that used to be spawn
     */
    public Location getPreviousLocation() {
        return previousLocation;
    }
}
