package com.jupiterframework.channel.communication;

import java.util.Arrays;
import java.util.List;

import com.jupiterframework.channel.config.RequestMethod;


/** 使用SDK扩展， Bean name需要设置为渠道名称 */
public interface SDKClientHandler extends ClientHandler {
    @Override
    default List<RequestMethod> getRequestMethod() {
        return Arrays.asList(RequestMethod.SDK);
    }

}
