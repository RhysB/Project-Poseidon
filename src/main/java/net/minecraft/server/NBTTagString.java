package net.minecraft.server;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {

    public String a;

    public NBTTagString() {}

    public NBTTagString(String s) {
        this.a = s;
        if (s == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    void a(DataOutput dataoutput) throws IOException {
        dataoutput.writeUTF(this.a);
    }

    void a(DataInput datainput) throws IOException {
        this.a = datainput.readUTF();
    }

    public byte a() {
        return (byte) 8;
    }

    public String toString() {
        return "" + this.a;
    }
}
