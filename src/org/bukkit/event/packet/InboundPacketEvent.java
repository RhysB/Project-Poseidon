package org.bukkit.event.packet;

import net.minecraft.server.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @author moderator_man
 */
public class InboundPacketEvent extends Event
{
    private static final long serialVersionUID = 1L;
    
    private Player player;
    private Packet packet;
    private PacketType type;
    private boolean cancelled;
    
    public InboundPacketEvent(Player player, Packet packet, PacketType type)
    {
        super(Type.PACKET_INBOUND);
        
        this.player = player;
        this.packet = packet;
        this.type = type;
    }
    
    /**
     * THIS CAN RETURN NULL
     */
    public Player getPlayer()
    {
        return player;
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
