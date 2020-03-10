package org.bukkit.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public class InventoryOpenedEvent extends Event implements Cancellable
{
    private boolean cancelled;
    private Player player;
    private Inventory inventory;
    
    public InventoryOpenedEvent(Player player, Inventory inventory)
    {
        super(Type.INVENTORY_OPEN);
        
        this.player = player;
        this.inventory = inventory;
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public Inventory getInventory()
    {
        return inventory;
    }
    
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}
