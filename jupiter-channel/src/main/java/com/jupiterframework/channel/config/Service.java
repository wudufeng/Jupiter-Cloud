package com.jupiterframework.channel.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.ToString;


/**
 * 渠道下的单个接口配置
 *
 */
@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@ToString(exclude = "channel")
public class Service {
    /** 服务名，当前渠道下的唯一标识 */
    private String name;

    @JSONField(deserialize = false, serialize = false)
    private Channel channel;

    @XmlAttribute
    private String path = "";
    @XmlAttribute(name = "request-method")
    private RequestMethod requestMethod;
    @XmlAttribute
    private String description = "";
    @XmlAttribute(name = "connect-timeout")
    private int connectTimeout = 10000;
    @XmlAttribute(name = "socket-timeout")
    private int socketTimeout = 30000;

    private Request request;
    private Response response;

}
