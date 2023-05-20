package org.bukkit.event.packet;

import net.minecraft.server.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * @author moderator_man
 */
public class PacketReceivedEvent extends Event
{
    private static final long serialVersionUID = 1L;
    
    private Player player;
    private Packet packet;
    private boolean cancelled;
    
    public PacketReceivedEvent(Player player, Packet packet)
    {
        super(Type.PACKET_RECEIVED);
        
        this.player = player;
        this.packet = packet;
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
    
    public boolean isCancelled()
    {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }
}
