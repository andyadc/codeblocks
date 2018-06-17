package com.andyadc.codeblocks.kit.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author andaicheng
 * @version 2017/1/8
 */
public final class IpUtil {

    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

    private static final String LOCAL_IP;

    static {
        LOCAL_IP = getLocalIpAddress();
    }

    private IpUtil() {
    }

    public static String getLocalIp() {
        return LOCAL_IP;
    }

    /**
     * 获取请求IP地址
     */
    public static String getReqIp(HttpServletRequest request) {
        String ip = "";
        //匹配大小写，保证无论Nginx如何配置代理参数，系统都能正常获取代理IP
        Enumeration<?> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String paraName = (String) enumeration.nextElement();
            if ("x-forward-for".equalsIgnoreCase(paraName) || "x-forwarded-for".equalsIgnoreCase(paraName)) {
                ip = request.getHeader(paraName);
                break;
            }
        }
        final String localIP = "127.0.0.1";
        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取本机 IP
     * IPV4
     */
    private static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    if (address instanceof Inet4Address && !"127.0.0.1".equals(address.getHostAddress())) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Get server ip error", e);
        }
        return null;
    }

}
