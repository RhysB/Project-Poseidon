package net.minecraft.server;

import com.legacyminecraft.poseidon.PoseidonConfig;

class NetworkReaderThread extends Thread {
    private boolean fast; // Poseidon
    final NetworkManager a;

    NetworkReaderThread(NetworkManager networkmanager, String s) {
        super(s);
        this.a = networkmanager;
        this.fast = PoseidonConfig.getInstance().getBoolean("settings.faster-packets.enabled", true); // Poseidon
    }

    public void run() {
        Object object = NetworkManager.a;

        synchronized (NetworkManager.a) {
            ++NetworkManager.b;
        }

        while (true) {
            boolean flag = false;

            try {
                flag = true;
                if (!NetworkManager.a(this.a)) {
                    flag = false;
                    break;
                }

                if (NetworkManager.b(this.a)) {
                    flag = false;
                    break;
                }

                while (NetworkManager.c(this.a)) {
                    ;
                }

                try {
                    sleep(this.fast ? 2L : 100L);
                } catch (InterruptedException interruptedexception) {
                    ;
                }
            } finally {
                if (flag) {
                    Object object1 = NetworkManager.a;

                    synchronized (NetworkManager.a) {
                        --NetworkManager.b;
                    }
                }
            }
        }

        object = NetworkManager.a;
        synchronized (NetworkManager.a) {
            --NetworkManager.b;
        }
    }
}
