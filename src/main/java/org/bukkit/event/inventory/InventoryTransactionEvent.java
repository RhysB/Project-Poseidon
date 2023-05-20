package org.bukkit.event.inventory;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryTransactionEvent extends Event implements Cancellable
{
    private static final long serialVersionUID = 1L;
    
    private boolean cancelled;
    private InventoryTransactionType transactionType;
    private Inventory inventory;
    private ItemStack stack;
    
    public InventoryTransactionEvent(InventoryTransactionType transactionType, Inventory inventory, ItemStack stack)
    {
        super(Type.INVENTORY_TRANSACTION);
        
        this.transactionType = transactionType;
        this.inventory = inventory;
        this.stack = stack;
    }
    
    public InventoryTransactionType getTransactionType()
    {
        return transactionType;
    }
    
    public Inventory getInventory()
    {
        return inventory;
    }
    
    public ItemStack getStack()
    {
        return stack;
    }
    
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}
