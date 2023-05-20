package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet10Flying extends Packet {

    public double x;
    public double y;
    public double z;
    public double stance;
    public float yaw;
    public float pitch;
    public boolean g;
    public boolean h;
    public boolean hasLook;

    public Packet10Flying() {}

    public void a(NetHandler nethandler) {
        nethandler.a(this);
    }

    public void a(DataInputStream datainputstream) throws IOException {
        this.g = datainputstream.read() != 0;
    }

    public void a(DataOutputStream dataoutputstream) throws IOException {
        dataoutputstream.write(this.g ? 1 : 0);
    }

    public int a() {
        return 1;
    }
}
