//package com.johnymuffin.poseidon;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class PlayerTracker {
//    private List<String> playerNames = new ArrayList<String>();
//    private static PlayerTracker singleton;
//
//    private PlayerTracker() {
//
//    }
//
//    public void addPlayer(String p) {
//        if(!playerNames.contains(p)) {
////            System.out.println("Adding Player: '" + p + "' to tracking list");
//            playerNames.add(p);
//        }
//    }
//
//    public void removePlayer(final String p) {
//        new java.util.Timer().schedule(
//                new java.util.TimerTask() {
//                    @Override
//                    public void run() {
//                        if(playerNames.contains(p)) {
//                            playerNames.remove(p);
////                            System.out.println("Removing Player: '" + p + "' to tracking list");
//                        }
//                    }
//                },
//                5000
//        );
//    }
//
//
//    public void clearList() {
//        playerNames = new ArrayList<String>();
//    }
//
//    public boolean isPlayerListed(String p) {
//        if (playerNames.contains(p)) {
////            System.out.println("Player: '" + p + "' Is Online");
//            return true;
//        }
////        System.out.println("Player: '" + p + "' Is Offline");
//        return false;
//    }
//
//    public static PlayerTracker getInstance() {
//        if (PlayerTracker.singleton == null) {
//            PlayerTracker.singleton = new PlayerTracker();
//        }
//        return PlayerTracker.singleton;
//    }
//
//
//}
