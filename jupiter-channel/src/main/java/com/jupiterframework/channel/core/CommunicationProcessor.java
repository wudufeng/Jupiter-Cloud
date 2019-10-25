package com.jupiterframework.channel.core;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mvel2.MVEL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.jupiterframework.channel.communication.ClientHandler;
import com.jupiterframework.channel.config.Channel;
import com.jupiterframework.channel.config.Request;
import com.jupiterframework.channel.config.RequestMethod;
import com.jupiterframework.channel.config.Service;
import com.jupiterframework.channel.config.Request.Parameter;
import com.jupiterframework.channel.pojo.Authorization;
import com.jupiterframework.channel.pojo.MessageRequest;
import com.jupiterframework.channel.pojo.MessageResponse;
import com.jupiterframework.channel.resolver.ValueResolverFactory;
import com.jupiterframework.channel.sign.SignatureFactory;
import com.jupiterframework.channel.unpack.UnpackHandlerFactory;
import com.jupiterframework.channel.util.FreemarkerUtils;
import com.jupiterframework.channel.util.XmlUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * 通讯核心处理类
 * 
 * @author wudf
 *
 */
@Slf4j
@Component
public class CommunicationProcessor implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Autowired
    private ChannelConfigurationContext configs;
    @Autowired
    private ValueResolverFactory valueResolverFactory;
    @Autowired
    private SignatureFactory signatureFactory;
    @Autowired
    private UnpackHandlerFactory unpackFactory;

    private Map<RequestMethod, ClientHandler> clients = new EnumMap<>(RequestMethod.class);


    @PostConstruct
    public void init() {
        applicationContext.getBeansOfType(ClientHandler.class).values().forEach(a -> a.getRequestMode().forEach(x -> clients.put(x, a)));
    }


    public MessageResponse request(MessageRequest request) {

        Authorization auth = request.getAuth();
        if (auth == null)
            throw new IllegalArgumentException("授权信息为空！");

        Channel chlcfg = configs.getChannelConfiguration(request.getChannel());
        Service svccfg = chlcfg.getServices().get(request.getService());

        if (svccfg == null) {
            throw new IllegalArgumentException(request.getChannel() + "找不到服务接口" + request.getService());
        }

        if (auth.getUrl() == null) {
            auth.setUrl(chlcfg.getUrl());
        }
        String url = auth.getUrl() + svccfg.getPath();
        log.debug(" request url {}", url);

        // 解析参数对象,ftl文件为xml格式的，将其转换为Request对象
        Request reqParam = this.pack(auth, request.getRequestParams(), chlcfg, svccfg);

        // 添加参数签名
        if (chlcfg.getAuthorized() != null) {
            Map<String, String> signParams = new HashMap<>();
            for (Parameter p : reqParam.getParameters()) {
                if (p.isSign()) {
                    signParams.put(p.getKey(), p.getValue());
                }
            }
            signParams.put("_DynamicPath_", reqParam.getDynamicPath());
            String sign = signatureFactory.create(chlcfg.getAuthorized().getSignatureAlgorith()).process(svccfg, auth, signParams);
            reqParam.setSignValue(sign);
        }

        byte[] respData = null;
        long startTime = System.currentTimeMillis();
        try {
            // 通讯调用
            respData = clients.get(svccfg.getRequestMethod()).request(svccfg, auth, reqParam);
        } catch (IOException e) {
            log.error("request url failure ! url[{}], parameter {}", url, request, e);

            return new MessageResponse("9999", ExceptionUtils.getRootCauseMessage(e));
        } finally {
            long elasped = System.currentTimeMillis() - startTime;
            String format = "{} 耗时{} ms";
            if (elasped > 5000)
                log.warn(format, url, elasped);
            else if (elasped > 3000)
                log.info(format, url, elasped);
            else
                log.debug(format, url, elasped);
        }

        // 解析报文
        Map<String, Object> data = unpackFactory.create(svccfg.getResponse().getFormat().name()).handle(respData, svccfg.getResponse());

        if (log.isDebugEnabled()) {
            log.debug("after transform : {} {}", System.lineSeparator(), JSON.toJSONString(data, true));
        }

        String retCode = (String) data.remove("retCode");
        String retMessage = (String) data.remove("retMessage");

        if ("9999".equals(retCode))
            throw new RemoteAccessException(retMessage);

        return new MessageResponse(retCode, retMessage, data);
    }


    private Request pack(Authorization auth, Map<String, Object> requestParams, Channel chlcfg, Service svccfg) {
        String templatFile = chlcfg.getName() + File.separatorChar + svccfg.getName() + ".ftl";
        Map<String, Object> temp = new HashMap<>(requestParams);
        temp.put("auth", auth);

        if (!temp.containsKey("timestamp"))
            temp.put("timestamp", System.currentTimeMillis());

        String template = FreemarkerUtils.createTemplate(templatFile, temp);
        log.debug("request parameters : {}", template);

        Request reqParam = XmlUtils.parse(template, Request.class);

        for (Parameter p : reqParam.getParameters()) {
            p.setValue(this.parseValue(p, temp));// 解析值,转换(如转换成md5)
        }

        return reqParam;
    }


    private String parseValue(Parameter p, Map<String, Object> vars) {
        if (StringUtils.hasText(p.getMvelExpression())) {
            Object val = MVEL.eval(p.getMvelExpression(), vars);
            log.debug("{} : mvel[{}]->[{}]", p.getKey(), p.getMvelExpression(), val);
            if (val == null)
                return "";
            if (val instanceof String)
                return (String) val;
            return String.valueOf(val);
        }

        if (StringUtils.hasText(p.getResolver()))
            return valueResolverFactory.create(p.getResolver()).resolve(null, p.getValue());
        return p.getValue();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
