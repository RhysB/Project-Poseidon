package net.minecraft.server;

import javax.swing.*;
import java.util.Vector;

public class PlayerListBox extends JList implements IUpdatePlayerListBox {

    private MinecraftServer a;
    private int b = 0;

    public PlayerListBox(MinecraftServer minecraftserver) {
        this.a = minecraftserver;
        minecraftserver.a((IUpdatePlayerListBox) this);
    }

    public void a() {
        if (this.b++ % 20 == 0) {
            Vector vector = new Vector();

            for (int i = 0; i < this.a.serverConfigurationManager.players.size(); ++i) {
                vector.add(((EntityPlayer) this.a.serverConfigurationManager.players.get(i)).name);
            }

            this.setListData(vector);
        }
    }
}
