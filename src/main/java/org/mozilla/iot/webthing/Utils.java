/**
 * Utility functions.
 */
package org.mozilla.iot.webthing;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.time.Instant;
import java.util.Enumeration;

public class Utils {
    /**
     * Get the default local IP address.
     *
     * @return The IP address, or null if not found.
     */
    public static String getIP() {
        try {
            final InetAddress address = Inet4Address.getLocalHost();
            if (isValidAddress(address)) {
                return address.getHostAddress();
            }
        } catch (UnknownHostException e) {
            // fall through
        }
        try {
            final Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                final NetworkInterface iface =
                        (NetworkInterface)interfaces.nextElement();
                final Enumeration addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    final InetAddress address = (InetAddress)addresses.nextElement();
                    if (!isValidAddress(address)) {
                        continue;
                    }
                    return address.getHostAddress();
                }
            }
        } catch (SocketException e) {
            // return null
        }
        return null;
    }

    /**
     * Get the current time.
     *
     * @return The current time in the form YYYY-mm-ddTHH:MM:SS+00.00
     */
    public static String timestamp() {
        String now = Instant.now().toString().split("\\.")[0];
        return now + "+00:00";
    }

    private static boolean isValidAddress(InetAddress address) {
        return !address.isLoopbackAddress() && !address.isMulticastAddress();
    }
}