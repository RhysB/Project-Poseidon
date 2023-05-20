package org.bukkit.event.player;

import net.minecraft.server.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Holds information for player teleport events
 */
public class PlayerTeleportEvent extends PlayerMoveEvent {
    public PlayerTeleportEvent(Player player, Location from, Location to) {
        super(Type.PLAYER_TELEPORT, player, from, to);
        blockCrossDimensionDupe(); //Poseidon
    }

    public PlayerTeleportEvent(final Event.Type type, Player player, Location from, Location to) {
        super(type, player, from, to);
        blockCrossDimensionDupe(); //Poseidon
    }

    //Poseidon - Start
    private void blockCrossDimensionDupe() {
        if (this.getFrom().getWorld() != this.getTo().getWorld()) {
            EntityPlayer entity = ((CraftPlayer) this.getPlayer()).getHandle();
            if (entity.activeContainer == entity.defaultContainer)
                return;
            System.out.println("[Poseidon] Force closing " + player.getName() + "'s inventory as they have teleported to a different world. This is to prevent a dupe bug.");
            entity.y();
        }
    }
    //Poseidon - End
}
