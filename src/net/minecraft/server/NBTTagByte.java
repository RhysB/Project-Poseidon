package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte extends NBTBase {

    public byte a;

    public NBTTagByte() {}

    public NBTTagByte(byte b0) {
        this.a = b0;
    }

    void a(DataOutput dataoutput) throws IOException {
        dataoutput.writeByte(this.a);
    }

    void a(DataInput datainput) throws IOException {
        this.a = datainput.readByte();
    }

    public byte a() {
        return (byte) 1;
    }

    public String toString() {
        return "" + this.a;
    }
}
