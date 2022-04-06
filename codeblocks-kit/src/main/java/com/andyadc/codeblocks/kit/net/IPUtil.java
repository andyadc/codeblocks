package com.andyadc.codeblocks.kit.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public final class IPUtil {

	private static final Logger logger = LoggerFactory.getLogger(IPUtil.class);

	private static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
	private static final Pattern ip_v4_pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");

	private static final String LOCAL_IP;
	private static final String[] HEADERS_TO_TRY = {
		"X-Forwarded-For",
		"Proxy-Client-IP",
		"WL-Proxy-Client-IP",
		"HTTP_X_FORWARDED_FOR",
		"HTTP_X_FORWARDED",
		"HTTP_X_CLUSTER_CLIENT_IP",
		"HTTP_CLIENT_IP",
		"HTTP_FORWARDED_FOR",
		"HTTP_FORWARDED",
		"HTTP_VIA",
		"REMOTE_ADDR"};

	static {
		LOCAL_IP = getHostAddress();
	}

	private IPUtil() {
	}

	public static String getLocalIp() {
		return LOCAL_IP;
	}

	public static void main(String[] args) {
		System.out.println(ipV4ToLong("114.141.191.195"));//1921892291
		System.out.println(longToIpV4(1L));
		System.out.println(getLocalIp());
	}

	public static String longToIpV4(long longIp) {
		int octet3 = (int) ((longIp >> 24) % 256);
		int octet2 = (int) ((longIp >> 16) % 256);
		int octet1 = (int) ((longIp >> 8) % 256);
		int octet0 = (int) ((longIp) % 256);
		return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
	}

	public static long ipV4ToLong(String ip) {
		if (ip == null || ip.length() == 0) {
			return -1;
		}

		String[] octets = ip.split("\\.");
		return (Long.parseLong(octets[0]) << 24)
			+ (Long.parseLong(octets[1]) << 16)
			+ (Long.parseLong(octets[2]) << 8)
			+ Long.parseLong(octets[3]);
	}

	public static boolean isIPv4Private(String ip) {
		long longIp = ipV4ToLong(ip);
		return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
			|| (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
			|| longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
	}

	public static boolean isIPv4Valid(String ip) {
		return ip_v4_pattern.matcher(ip).matches();
	}

	public static String getIpFromRequest(HttpServletRequest request) {
		String ip;
		boolean found = false;
		if ((ip = request.getHeader("x-forwarded-for")) != null) {
			StringTokenizer tokenizer = new StringTokenizer(ip, ",");
			while (tokenizer.hasMoreTokens()) {
				ip = tokenizer.nextToken().trim();
				if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getReqIp(HttpServletRequest request) {
		String ip = null;
		Enumeration<?> enumeration = request.getHeaderNames();
		while (enumeration.hasMoreElements()) {
			String paraName = (String) enumeration.nextElement();
			if ("x-forward-for".equalsIgnoreCase(paraName)
				|| "x-forwarded-for".equalsIgnoreCase(paraName)) {
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
	 */
	private static String getHostAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface ni = interfaces.nextElement();
				if (ni.isLoopback()) {
					continue;
				}

				Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress address = inetAddresses.nextElement();
					// ignores all invalidated addresses
					if (address.isLinkLocalAddress() || address.isLoopbackAddress() || address.isAnyLocalAddress()) {
						continue;
					}
					return address.getHostAddress();
				}
			}
		} catch (Exception e) {
			logger.error("Get host address error", e);
		}
		return null;
	}

	public String getClientIpAddress(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}
}
