package com.legacyminecraft.poseidon.watchdog;

import com.legacyminecraft.poseidon.PoseidonConfig;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class WatchDogThread extends Thread {
    private int count = 0;
    private Long lastTick = System.currentTimeMillis() / 1000L;
    private volatile AtomicBoolean tickOccurred = new AtomicBoolean(true);
    private Thread serverThread;
    private final int killTimeout;
    //Debug Timeout
    private final boolean debugTimeoutEnabled;
    private final int debugTimeout;
    private boolean printedDebug;

    public WatchDogThread(Thread thread) {
        this.serverThread = thread;
        this.killTimeout = PoseidonConfig.getInstance().getConfigInteger("settings.watchdog.timeout.value");

        this.debugTimeoutEnabled = PoseidonConfig.getInstance().getConfigBoolean("settings.watchdog.debug-timeout.enabled");
        this.debugTimeout = PoseidonConfig.getInstance().getConfigInteger("settings.watchdog.debug-timeout.value");
    }

    public void run() {
        boolean running = true;
        while (true && !this.isInterrupted() && running) {
            try {
                if (tickOccurred.get()) {
                    lastTick = System.currentTimeMillis() / 1000L;
                    tickOccurred.set(false);
                    printedDebug = false; //Set back tp false so if server hangs again debug is printed.
                } else {
                    //Server timout check
                    if ((lastTick + killTimeout) < (System.currentTimeMillis() / 1000L)) {
                        System.out.println("[Poseidon-Watchdog] Server has hanged. Killing the process as a result of the watchdog timeout being exceeded.");
                        System.out.println("--------------------[Stacktrace For Developers]--------------------");
                        Arrays.asList(serverThread.getStackTrace()).forEach(System.out::println);
                        System.out.println("-------------------------------------------------------------------");
                        Runtime.getRuntime().halt(0);

                    } else {
                        System.out.println("[Poseidon-Watchdog] A server tick hasn't occurred in " + ((int) ((System.currentTimeMillis() / 1000L) - lastTick)) + " seconds.");
                        //Server debug timeout
                        if((lastTick + debugTimeout) < (System.currentTimeMillis() / 1000L) && debugTimeoutEnabled && !printedDebug) {
                            System.out.println("[Poseidon-Watchdog] Server hang detected. Printing debug as debug timeout has been exceeded.");
                            System.out.println("--------------------[Stacktrace For Developers]--------------------");
                            Arrays.asList(serverThread.getStackTrace()).forEach(System.out::println);
                            System.out.println("-------------------------------------------------------------------");
                            printedDebug = true; //This variable prevents debug from printing multiple times during a single tick.
                        }
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
