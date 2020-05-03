package org.bukkit.event.packet;

import net.minecraft.server.Packet;
import net.minecraft.server.Packet0KeepAlive;
import net.minecraft.server.Packet100OpenWindow;
import net.minecraft.server.Packet101CloseWindow;
import net.minecraft.server.Packet102WindowClick;
import net.minecraft.server.Packet103SetSlot;
import net.minecraft.server.Packet104WindowItems;
import net.minecraft.server.Packet105CraftProgressBar;
import net.minecraft.server.Packet106Transaction;
import net.minecraft.server.Packet10Flying;
import net.minecraft.server.Packet11PlayerPosition;
import net.minecraft.server.Packet12PlayerLook;
import net.minecraft.server.Packet130UpdateSign;
import net.minecraft.server.Packet13PlayerLookMove;
import net.minecraft.server.Packet14BlockDig;
import net.minecraft.server.Packet15Place;
import net.minecraft.server.Packet16BlockItemSwitch;
import net.minecraft.server.Packet18ArmAnimation;
import net.minecraft.server.Packet19EntityAction;
import net.minecraft.server.Packet1Login;
import net.minecraft.server.Packet200Statistic;
import net.minecraft.server.Packet20NamedEntitySpawn;
import net.minecraft.server.Packet21PickupSpawn;
import net.minecraft.server.Packet22Collect;
import net.minecraft.server.Packet23VehicleSpawn;
import net.minecraft.server.Packet24MobSpawn;
import net.minecraft.server.Packet255KickDisconnect;
import net.minecraft.server.Packet25EntityPainting;
import net.minecraft.server.Packet28EntityVelocity;
import net.minecraft.server.Packet29DestroyEntity;
import net.minecraft.server.Packet2Handshake;
import net.minecraft.server.Packet30Entity;
import net.minecraft.server.Packet31RelEntityMove;
import net.minecraft.server.Packet32EntityLook;
import net.minecraft.server.Packet33RelEntityMoveLook;
import net.minecraft.server.Packet34EntityTeleport;
import net.minecraft.server.Packet38EntityStatus;
import net.minecraft.server.Packet39AttachEntity;
import net.minecraft.server.Packet3Chat;
import net.minecraft.server.Packet40EntityMetadata;
import net.minecraft.server.Packet4UpdateTime;
import net.minecraft.server.Packet50PreChunk;
import net.minecraft.server.Packet51MapChunk;
import net.minecraft.server.Packet52MultiBlockChange;
import net.minecraft.server.Packet53BlockChange;
import net.minecraft.server.Packet54PlayNoteBlock;
import net.minecraft.server.Packet5EntityEquipment;
import net.minecraft.server.Packet60Explosion;
import net.minecraft.server.Packet6SpawnPosition;
import net.minecraft.server.Packet70Bed;
import net.minecraft.server.Packet71Weather;
import net.minecraft.server.Packet7UseEntity;
import net.minecraft.server.Packet8UpdateHealth;
import net.minecraft.server.Packet9Respawn;

public enum PacketType
{
	KEEP_ALIVE(Packet0KeepAlive.class),
	OPEN_WINDOW(Packet100OpenWindow.class),
	CLOSE_WINDOW(Packet101CloseWindow.class),
	WINDOW_CLICK(Packet102WindowClick.class),
	SET_SLOT(Packet103SetSlot.class),
	WINDOW_ITEMS(Packet104WindowItems.class),
	CRAFT_PROGRESS_BAR(Packet105CraftProgressBar.class),
	TRANSACTION(Packet106Transaction.class),
	FLYING(Packet10Flying.class),
	PLAYER_POSITION(Packet11PlayerPosition.class),
	PLAYER_LOOK(Packet12PlayerLook.class),
	UPDATE_SIGN(Packet130UpdateSign.class),
	PLAYER_LOOK_MOVE(Packet13PlayerLookMove.class),
	BLOCK_DIG(Packet14BlockDig.class),
	PLACE(Packet15Place.class),
	BLOCK_ITEM_SWITCH(Packet16BlockItemSwitch.class),
	ARM_ANIMATION(Packet18ArmAnimation.class),
	ENTITY_ACTION(Packet19EntityAction.class),
	LOGIN(Packet1Login.class),
	STATISTIC(Packet200Statistic.class),
	NAMED_ENTITY_SPAWN(Packet20NamedEntitySpawn.class),
	PICKUP_SPAWN(Packet21PickupSpawn.class),
	COLLECT(Packet22Collect.class),
	VEHICLE_SPAWN(Packet23VehicleSpawn.class),
	MOB_SPAWN(Packet24MobSpawn.class),
	KICK_DISCONNECT(Packet255KickDisconnect.class),
	ENTITY_PAINTING(Packet25EntityPainting.class),
	ENTITY_VELOCITY(Packet28EntityVelocity.class),
	DESTROY_ENTITY(Packet29DestroyEntity.class),
	HANDSHAKE(Packet2Handshake.class),
	ENTITY(Packet30Entity.class),
	REL_ENTITY_MOVE(Packet31RelEntityMove.class),
	ENTITY_LOOK(Packet32EntityLook.class),
	REL_ENTITY_MOVE_LOOK(Packet33RelEntityMoveLook.class),
	ENTITY_TELEPORT(Packet34EntityTeleport.class),
	ENTITY_STATUS(Packet38EntityStatus.class),
	ATTACH_ENTITY(Packet39AttachEntity.class),
	CHAT(Packet3Chat.class),
	ENTITY_METADATA(Packet40EntityMetadata.class),
	UPDATE_TIME(Packet4UpdateTime.class),
	PRE_CHUNK(Packet50PreChunk.class),
	MAP_CHUNK(Packet51MapChunk.class),
	MULTI_BLOCK_CHANGE(Packet52MultiBlockChange.class),
	BLOCK_CHANGE(Packet53BlockChange.class),
	PLAY_NOTE_BLOCK(Packet54PlayNoteBlock.class),
	ENTITY_EQUIPMENT(Packet5EntityEquipment.class),
	EXPLOSION(Packet60Explosion.class),
	SPAWN_POSITION(Packet6SpawnPosition.class),
	BED(Packet70Bed.class),
	WEATHER(Packet71Weather.class),
	USE_ENTITY(Packet7UseEntity.class),
	UPDATE_HEALTH(Packet8UpdateHealth.class),
	RESPAWN(Packet9Respawn.class),
	UNKNOWN(Packet.class);
	
	private Class<? extends Packet> packetClass;
	
	PacketType(Class<? extends Packet> packetClass)
	{
		this.packetClass = packetClass;
	}
	
	public static PacketType valueOf(Class<? extends Packet> packetClass)
	{
		for (PacketType type : values())
			if (type.getPacketClass().equals(packetClass))
				return type;
		return PacketType.UNKNOWN;
	}
	
	public Class<? extends Packet> getPacketClass()
	{
		return packetClass;
	}
}
