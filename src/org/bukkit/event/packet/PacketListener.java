package org.bukkit.event.packet;

import org.bukkit.event.Listener;

/**
 * @author moderator_man
 */
public class PacketListener implements Listener
{
    public void onPacketReceived(InboundPacketEvent event) {}
    public void onPacketDispatched(OutboundPacketEvent event) {}
}
