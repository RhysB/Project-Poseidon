package com.legacyminecraft.poseidon.event;

import net.minecraft.server.Packet;

public class PlayerSendPacketEvent extends PlayerPacketEvent {

    public PlayerSendPacketEvent(String username, Packet packet) {
        super(Type.PLAYER_SEND_PACKET, username, packet);
    }
}
