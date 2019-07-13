package com.ueb.framework.channel.communication;

import java.io.IOException;
import java.util.List;

import com.ueb.framework.channel.config.Request;
import com.ueb.framework.channel.config.RequestMethod;
import com.ueb.framework.channel.config.Service;
import com.ueb.framework.channel.pojo.Authorization;


public interface ClientHandler {
    byte[] request(Service config, Authorization auth, Request req) throws IOException;


    List<RequestMethod> getRequestMode();
}
