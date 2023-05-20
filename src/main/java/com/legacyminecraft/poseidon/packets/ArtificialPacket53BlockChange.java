package com.legacyminecraft.poseidon.packets;

import net.minecraft.server.NetHandler;
import net.minecraft.server.Packet;
import net.minecraft.server.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ArtificialPacket53BlockChange extends Packet {

    public int a;
    public int b;
    public int c;
    public int material;
    public int data;

    public ArtificialPacket53BlockChange() {
        this.k = true;
    }

    public ArtificialPacket53BlockChange(int i, int j, int k, World world) {
        this.k = true;
        this.a = i;
        this.b = j;
        this.c = k;
        this.material = world.getTypeId(i, j, k);
        this.data = world.getData(i, j, k);
    }

    public ArtificialPacket53BlockChange(int i, int j, int k, int materialType, int data) {
        this.k = true;
        this.a = i;
        this.b = j;
        this.c = k;
        this.material = materialType;
        this.data = data;
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.a = datainputstream.readInt();
        this.b = datainputstream.read();
        this.c = datainputstream.readInt();
        this.material = datainputstream.read();
        this.data = datainputstream.read();
    }

    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeInt(this.a);
        dataoutputstream.write(this.b);
        dataoutputstream.writeInt(this.c);
        dataoutputstream.write(this.material);
        dataoutputstream.write(this.data);
    }

    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 11;
    }
}