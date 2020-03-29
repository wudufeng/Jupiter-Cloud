package com.jupiterframework.channel.communication;

import java.io.IOException;
import java.util.List;

import com.jupiterframework.channel.config.Request;
import com.jupiterframework.channel.config.RequestMethod;
import com.jupiterframework.channel.config.Service;
import com.jupiterframework.channel.pojo.Authorization;


public interface ClientHandler {
    byte[] request(Service config, Authorization auth, Request req) throws IOException;


    List<RequestMethod> getRequestMethod();
}
