package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet70Bed extends Packet {

    public static final String[] a = new String[] { "tile.bed.notValid", null, null};
    public int b;

    public Packet70Bed() {}

    public Packet70Bed(int i) {
        this.b = i;
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.b = datainputstream.readByte();
    }

    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.writeByte(this.b);
    }

    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    public int a() {
        return 1;
    }
}
