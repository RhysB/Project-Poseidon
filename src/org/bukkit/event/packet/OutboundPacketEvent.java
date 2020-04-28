package org.bukkit.event.packet;

import org.bukkit.event.Event;

import net.minecraft.server.Packet;

/**
 * @author moderator_man
 */
public class OutboundPacketEvent extends Event
{
    private static final long serialVersionUID = 1L;
    
    private String username;
    private Packet packet;
    private boolean cancelled;
    
    public OutboundPacketEvent(String username, Packet packet)
    {
        super(Type.PACKET_INBOUND);
        
        this.username = username;
        this.packet = packet;
    }
    
    /**
     * THIS CAN RETURN NULL
     */
    public String getPlayer()
    {
        return username;
    }
    
    public Packet getPacket()
    {
        return packet;
    }
    
    public boolean isCancelled()
    {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }
}
