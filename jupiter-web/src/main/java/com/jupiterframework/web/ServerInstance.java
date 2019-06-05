package com.jupiterframework.web;

public class ServerInstance {

    private String ip;
    private int port;

    private String hostAndPort;


    public ServerInstance(String ip, int port) {
        super();
        this.ip = ip;
        this.port = port;
        this.hostAndPort = String.format("%s:%s", ip, port);
    }


    public String getIp() {
        return ip;
    }


    public int getPort() {
        return port;
    }


    public String getHostAndPort() {
        return hostAndPort;
    }

}
