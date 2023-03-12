package net.minecraft.server;

import com.legacyminecraft.poseidon.PoseidonConfig;
import java.io.IOException;

class NetworkWriterThread extends Thread {
    private boolean fast; // Poseidon
    final NetworkManager a;

    NetworkWriterThread(NetworkManager networkmanager, String s) {
        super(s);
        this.a = networkmanager;
        this.fast = PoseidonConfig.getInstance().getBoolean("settings.faster-packets.enabled", true); // Poseidon
    }

    public void run() {
        Object object = NetworkManager.a;

        synchronized (NetworkManager.a) {
            ++NetworkManager.c;
        }

        while (true) {
            boolean flag = false;

            try {
                flag = true;
                if (!NetworkManager.a(this.a)) {
                    flag = false;
                    break;
                }

                while (NetworkManager.d(this.a)) {
                    ;
                }

                if (!this.fast) { // Poseidon
                    try {
                        sleep(100L);
                    } catch (InterruptedException interruptedexception) {
                        ;
                    }
                }

                try {
                    if (NetworkManager.e(this.a) != null) {
                        NetworkManager.e(this.a).flush();
                    }
                } catch (IOException ioexception) {
                    if (!NetworkManager.f(this.a)) {
                        NetworkManager.a(this.a, (Exception) ioexception);
                    }

                    //ioexception.printStackTrace(); //Project Poseidon Remove - Credit to Notcz in Modification Station
                }
                
                if (this.fast) { // Poseidon
                    try {
                        sleep(2L);
                    } catch (InterruptedException interruptedexception) {
                        ;
                    }
                }
            } finally {
                if (flag) {
                    Object object1 = NetworkManager.a;

                    synchronized (NetworkManager.a) {
                        --NetworkManager.c;
                    }
                }
            }
        }

        object = NetworkManager.a;
        synchronized (NetworkManager.a) {
            --NetworkManager.c;
        }
    }
}
