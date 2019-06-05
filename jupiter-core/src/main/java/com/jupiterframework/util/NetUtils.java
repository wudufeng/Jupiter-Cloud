package com.jupiterframework.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * IP地址工具类
 * 
 */
public abstract class NetUtils {
	// copy dubbo NetUtils
	private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

	private static final String LOCALHOST = "127.0.0.1";

	private static final String ANYHOST = "0.0.0.0";

	private NetUtils() {
	}

	private static final Pattern ADDRESS_PATTERN = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}\\:\\d{1,5}$");

	public static boolean isValidAddress(String address) {
		return ADDRESS_PATTERN.matcher(address).matches();
	}

	private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

	public static boolean isLocalHost(String host) {
		return host != null && (LOCAL_IP_PATTERN.matcher(host).matches() || host.equalsIgnoreCase("localhost"));
	}

	public static boolean isAnyHost(String host) {
		return "0.0.0.0".equals(host);
	}

	public static boolean isInvalidLocalHost(String host) {
		return host == null || host.length() == 0 || host.equalsIgnoreCase("localhost") || host.equals("0.0.0.0")
				|| (LOCAL_IP_PATTERN.matcher(host).matches());
	}

	public static boolean isValidLocalHost(String host) {
		return !isInvalidLocalHost(host);
	}

	public static InetSocketAddress getLocalSocketAddress(String host, int port) {
		return isInvalidLocalHost(host) ? new InetSocketAddress(port) : new InetSocketAddress(host, port);
	}

	private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

	private static boolean isValidAddress(InetAddress address) {
		if (address == null || address.isLoopbackAddress())
			return false;
		String name = address.getHostAddress();
		return (name != null && !ANYHOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN.matcher(name).matches());
	}

	public static String getLocalHost() {
		InetAddress address = getLocalAddress();
		return address == null ? LOCALHOST : address.getHostAddress();
	}

	private static volatile InetAddress LOCAL_ADDRESS = null;

	/**
	 * 遍历本地网卡，返回第一个合理的IP。
	 * 
	 * @return 本地网卡IP
	 */
	public static InetAddress getLocalAddress() {
		if (LOCAL_ADDRESS != null)
			return LOCAL_ADDRESS;
		InetAddress localAddress = getLocalAddress0();
		LOCAL_ADDRESS = localAddress;
		return localAddress;
	}

	private static InetAddress getLocalAddress0() {
		InetAddress localAddress = null;
		try {
			localAddress = InetAddress.getLocalHost();
			if (isValidAddress(localAddress)) {
				return localAddress;
			}
		} catch (Throwable e) {
			logger.warn("Failed to retriving ip address, {}", e.getMessage(), e);
		}
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces != null) {
				while (interfaces.hasMoreElements()) {
					try {
						NetworkInterface network = interfaces.nextElement();
						Enumeration<InetAddress> addresses = network.getInetAddresses();
						if (addresses != null) {
							while (addresses.hasMoreElements()) {
								try {
									InetAddress address = addresses.nextElement();
									if (isValidAddress(address)) {
										return address;
									}
								} catch (Throwable e) {
									logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
								}
							}
						}
					} catch (Throwable e) {
						logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
					}
				}
			}
		} catch (Throwable e) {
			logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
		}
		logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
		return localAddress;
	}

	private static final Map<String, String> hostNameCache = new HashMap<>(1000);

	public static String getHostName(String address) {
		try {
			int i = address.indexOf(':');
			if (i > -1) {
				address = address.substring(0, i);
			}
			String hostname = hostNameCache.get(address);
			if (hostname != null && hostname.length() > 0) {
				return hostname;
			}
			InetAddress inetAddress = InetAddress.getByName(address);
			if (inetAddress != null) {
				hostname = inetAddress.getHostName();
				hostNameCache.put(address, hostname);
				return hostname;
			}
		} catch (Throwable e) {
			// ignore
		}
		return address;
	}

	/**
	 * @param hostName
	 * @return ip address or hostName if UnknownHostException
	 */
	public static String getIpByHost(String hostName) {
		try {
			return InetAddress.getByName(hostName).getHostAddress();
		} catch (UnknownHostException e) {
			return hostName;
		}
	}

	public static String toAddressString(InetSocketAddress address) {
		return address.getAddress().getHostAddress() + ":" + address.getPort();
	}

	public static String getRemoteAddr(javax.servlet.http.HttpServletRequest request) {
		String ip = request.getHeader("x-real-ip");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("x-forwarded-for");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}
}
