package com.legacyminecraft.poseidon.util;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Release2Beta {

    public static long serializeAddress(SocketAddress address) {
        String str = address.toString();
        String[] parts = str.split(":");

        String realAddress = parts[0].substring(1); // remove '/'
        return ipToLong(realAddress);
    }

    public static InetSocketAddress deserializeAddress(long spoofedAddress) {
        String realAddress = longToIp(spoofedAddress);
        return new InetSocketAddress(realAddress, 0);
    }

    //Credit: https://mkyong.com/java/java-convert-ip-address-to-decimal-number/
    public static long ipToLong(String ipAddress) {
        // ipAddressInArray[0] = 192
        String[] ipAddressInArray = ipAddress.split("\\.");

        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {

            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);

            // 1. 192 * 256^3
            // 2. 168 * 256^2
            // 3. 1 * 256^1
            // 4. 2 * 256^0
            result += ip * Math.pow(256, power);

        }

        return result;

    }

    //Credit: https://mkyong.com/java/java-convert-ip-address-to-decimal-number/
    private static String longToIp(long i) {

        return ((i >> 24) & 0xFF) +
                "." + ((i >> 16) & 0xFF) +
                "." + ((i >> 8) & 0xFF) +
                "." + (i & 0xFF);

    }
}
