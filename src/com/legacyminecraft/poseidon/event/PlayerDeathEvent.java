package com.legacyminecraft.poseidon.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerDeathEvent extends EntityDeathEvent {
    private String deathMessage = "";
    private boolean keepInventory = false;

    public PlayerDeathEvent(Entity what, List<ItemStack> drops) {
        super(what, drops);
    }

    /**
     * Set the death message that will appear to everyone on the server.
     *
     * @param deathMessage Message to appear to other players on the server.
     */
    public void setDeathMessage(String deathMessage) {
        this.deathMessage = deathMessage;
    }

    /**
     * Get the death message that will appear to everyone on the server.
     *
     * @return Message to appear to other players on the server.
     */
    public String getDeathMessage() {
        return deathMessage;
    }

    /**
     * Sets if the Player keeps inventory on death.
     *
     * @param keepInventory True to keep the inventory
     */
    public void setKeepInventory(boolean keepInventory) {
        this.keepInventory = keepInventory;
    }

    /**
     * Gets if the Player keeps inventory on death.
     *
     * @return True if the player keeps inventory on death
     */
    public boolean getKeepInventory() {
        return keepInventory;
    }


}
