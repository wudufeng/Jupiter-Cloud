package com.jupiterframework.channel.pojo;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private String channel;
    private String service;
    private Authorization auth;
    private Map<String, Object> requestParams;


    public void addParameter(String key, Object value) {
        if (requestParams == null) {
            requestParams = new HashMap<>();
        }
        requestParams.put(key, value);
    }
}
