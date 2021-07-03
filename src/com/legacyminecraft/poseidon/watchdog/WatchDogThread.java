package com.legacyminecraft.poseidon.watchdog;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class WatchDogThread extends Thread {
    private int count = 0;
    private Long lastTick = System.currentTimeMillis() / 1000L;
    private volatile AtomicBoolean tickOccurred = new AtomicBoolean(true);
    private Thread serverThread;

    public WatchDogThread(Thread thread) {
        serverThread = thread;
    }

    public void run() {
        boolean running = true;
        while (true && !this.isInterrupted() && running) {
            try {
                if (tickOccurred.get()) {
                    lastTick = System.currentTimeMillis() / 1000L;
                    tickOccurred.set(false);
                } else {
                    if ((lastTick + 300) < (System.currentTimeMillis() / 1000L)) {
                        System.out.println("[Poseidon-Watchdog] Server has hanged, killing process.");
                        System.out.println("--------------------[Stacktrace For Developers]--------------------");
                        Arrays.asList(serverThread.getStackTrace()).forEach(System.out::println);
                        System.out.println("-------------------------------------------------------------------");
                        Runtime.getRuntime().halt(0);

                    } else {
                        System.out.println("[Poseidon-Watchdog] A server tick hasn't occurred in " + ((int) ((System.currentTimeMillis() / 1000L) - lastTick)) + " seconds.");
                    }
                }
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                System.out.println("[Poseidon-Watchdog] The watchdog has been interrupted.");
//                e.printStackTrace();
                running = false;
            }
        }
    }

    public void tickUpdate() {
        tickOccurred.set(true);
    }

    public boolean isHangDetected() {
        return tickOccurred.get();
    }
}
