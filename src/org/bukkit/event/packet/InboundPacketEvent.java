package org.bukkit.event.packet;

import org.bukkit.event.Event;

import net.minecraft.server.Packet;

/**
 * @author moderator_man
 */
public class InboundPacketEvent extends Event
{
    private static final long serialVersionUID = 1L;
    
    private String username;
    private Packet packet;
    private PacketType type;
    private boolean cancelled;
    
    public InboundPacketEvent(String username, Packet packet, PacketType type)
    {
        super(Type.PACKET_INBOUND);
        
        this.username = username;
        this.packet = packet;
        this.type = type;
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
    
    public PacketType getPacketType()
    {
    	return type;
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
