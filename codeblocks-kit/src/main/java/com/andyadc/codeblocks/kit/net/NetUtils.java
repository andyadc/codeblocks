package com.andyadc.codeblocks.kit.net;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 关于网络的工具类.
 *
 * @author andaicheng
 * @version 2017/1/8
 */
public final class NetUtils {

    private static final int PORT_RANGE_MIN = 1024;
    private static final int PORT_RANGE_MAX = 65535;

    /**
     * Pre-loaded local address
     */
    private static InetAddress localAddress;

    static {
        try {
            localAddress = getLocalInetAddress();
        } catch (SocketException e) {
            throw new RuntimeException("Fail to get local ip.", e);
        }
    }

    private NetUtils() {
    }

    /**
     * Retrieve local address
     *
     * @return the string local address
     */
    public static String getLocalAddress() {
        return localAddress.getHostAddress();
    }

    /**
     * Retrieve the first validated local ip address(the Public and LAN ip addresses are validated).
     *
     * @return the local address
     * @throws SocketException the socket exception
     */
    public static InetAddress getLocalInetAddress() throws SocketException {
        Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();

        while (enu.hasMoreElements()) {
            NetworkInterface ni = enu.nextElement();
            if (ni.isLoopback()) {
                continue;
            }

            Enumeration<InetAddress> addressEnu = ni.getInetAddresses();
            while (addressEnu.hasMoreElements()) {
                InetAddress address = addressEnu.nextElement();

                // ignores all invalidated addresses
                if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
                    continue;
                }

                return address;
            }
        }

        throw new RuntimeException("No validated local address!");
    }

    /**
     * 测试端口是否空闲可用, from Spring SocketUtils
     */
    public static boolean isPortAvailable(int port) {
        try {
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(port, 1,
                    InetAddress.getByName("localhost"));
            serverSocket.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 随机找一个空闲端口,from Spring SocketUtils
     */
    public static int findRandomAvailablePort() {
        return findRandomAvailablePort(PORT_RANGE_MIN, PORT_RANGE_MAX);
    }

    /**
     * 在一个范围里随机找一个空闲端口,from Spring SocketUtils
     */
    public static int findRandomAvailablePort(int minPort, int maxPort) {
        int portRange = maxPort - minPort;
        int candidatePort;
        int searchCounter = 0;

        do {
            if (++searchCounter > portRange) {
                throw new IllegalStateException(
                        String.format("Could not find an available tcp port in the range [%d, %d] after %d attempts",
                                minPort, maxPort, searchCounter));
            }
            candidatePort = minPort + ThreadLocalRandom.current().nextInt(portRange + 1);
        } while (!isPortAvailable(candidatePort));

        return candidatePort;
    }

    /**
     * 从某个端口开始，递增找一个空闲端口.
     */
    public static int findAvailablePortFrom(int minPort) {
        for (int port = minPort; port < PORT_RANGE_MAX; minPort++) {
            if (isPortAvailable(port)) {
                return port;
            }
        }

        throw new IllegalStateException(
                String.format("Could not find an available tcp port in the range [%d, %d]", minPort, PORT_RANGE_MAX));
    }
}
