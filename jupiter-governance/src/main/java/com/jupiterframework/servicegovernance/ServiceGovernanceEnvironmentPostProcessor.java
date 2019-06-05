package com.jupiterframework.servicegovernance;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import com.jupiterframework.servicegovernance.constant.ServiceGovernanceConstants;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ServiceGovernanceEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PROPERTY_SOURCE_NAME = "defaultProperties";


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> map = new HashMap<>();

        if (environment.containsProperty(ServiceGovernanceConstants.SERVER_PORT_NAME)) {

            System.setProperty(ServiceGovernanceConstants.SERVER_PORT_NAME, environment.getProperty(ServiceGovernanceConstants.SERVER_PORT_NAME));
        }

        String ip = environment.getProperty("server.address", environment.getProperty(ServiceGovernanceConstants.ADDRESS_NAME, getLocalAddress0().getHostAddress()));
        if (ip != null) {
            // 如果直接将本机ip设置为"server.address",127.0.0.1将无法访问
            System.setProperty(ServiceGovernanceConstants.ADDRESS_NAME, ip);
        }

        addOrReplace(environment.getPropertySources(), map);
    }


    private void addOrReplace(MutablePropertySources propertySources, Map<String, Object> map) {
        MapPropertySource target = null;
        if (propertySources.contains(PROPERTY_SOURCE_NAME)) {
            PropertySource<?> source = propertySources.get(PROPERTY_SOURCE_NAME);
            if (source instanceof MapPropertySource) {
                target = (MapPropertySource) source;
                for (String key : map.keySet()) {
                    if (!target.containsProperty(key)) {
                        target.getSource().put(key, map.get(key));
                    }
                }
            }
        }
        if (target == null) {
            target = new MapPropertySource(PROPERTY_SOURCE_NAME, map);
        }
        if (!propertySources.contains(PROPERTY_SOURCE_NAME)) {
            propertySources.addLast(target);
        }
    }


    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            log.warn("Failed to retriving ip address, {}", e.getMessage(), e);
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
                                    log.warn("Failed to retriving ip address, " + e.getMessage(), e);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        log.warn("Failed to retriving ip address, " + e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            log.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        log.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    private static final String LOCALHOST = "127.0.0.1";

    private static final String ANYHOST = "0.0.0.0";

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");


    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null && !ANYHOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN.matcher(name).matches());
    }
}
