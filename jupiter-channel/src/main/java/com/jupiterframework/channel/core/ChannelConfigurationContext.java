package com.jupiterframework.channel.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jupiterframework.channel.config.Channel;
import com.jupiterframework.channel.config.ChannelProperties;
import com.jupiterframework.channel.config.Response;
import com.jupiterframework.channel.config.Service;
import com.jupiterframework.channel.config.Response.Field;
import com.jupiterframework.channel.config.Response.Field.ValueMapping;
import com.jupiterframework.channel.util.XmlUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class ChannelConfigurationContext implements ApplicationContextAware {

    private ChannelProperties<Channel> channelProperties;

    private Map<String /* name */, Map<String, Response>> responses = new HashMap<>();

    private static ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("reload-channel-reponse-xml-%d").build());


    public Map<String, Channel> getChannelConfigurations() {
        return channelProperties.getChannelConfigurations();
    }


    public Channel getChannelConfiguration(String name) {
        return channelProperties.getChannelConfiguration(name);
    }


    @PostConstruct
    public void init() throws IOException {
        for (Entry<String, Channel> chnl : channelProperties.getChannelConfigurations().entrySet()) {
            chnl.getValue().setName(chnl.getKey());

            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] files = resolver.getResources(String.format("%s/%s/*.ftl", channelProperties.getPath(), chnl.getKey()));

            for (Resource res : files) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("<request ")) {

                            // 截断dynamicPath配置
                            int index = line.indexOf("dynamic-path");
                            if (index != -1) {
                                line = line.substring(0, index) + line.substring(line.indexOf("\" ", index) + 1);
                            }
                            String filename = res.getFilename();
                            Service svc = XmlUtils.parse(line + "</request>", Service.class);
                            svc.setChannel(chnl.getValue());
                            svc.setName(filename.substring(0, filename.length() - 4));
                            if (svc.getRequestMethod() == null) {
                                svc.setRequestMethod(chnl.getValue().getRequestMethod());
                            }
                            svc.setConnectTimeout(svc.getConnectTimeout() == null ? chnl.getValue().getConnectTimeout() : svc.getConnectTimeout());
                            svc.setSocketTimeout(svc.getSocketTimeout() == null ? chnl.getValue().getSocketTimeout() : svc.getSocketTimeout());

                            Response r = this.parseResponse(svc.getChannel().getName(), svc.getName());
                            svc.setResponse(r);

                            chnl.getValue().getServices().put(svc.getName(), svc);
                            log.info("registry service {} {} {}", chnl.getValue().getName(), svc.getName(), svc.getDescription());

                            break;
                        }
                    }
                }

            }

        }

        ReloadResponseConfiguration reload = new ReloadResponseConfiguration();
        executor.scheduleAtFixedRate(reload, 30, 10, TimeUnit.SECONDS);

    }


    public Response parseResponse(String channel, String serviceName) {
        Response r;
        try {
            Resource res = this.getResponseFilePath(channel, serviceName);
            r = XmlUtils.parse(res.getInputStream(), Response.class);
            r.setLastModified(res.lastModified());
        } catch (JAXBException | IOException e) {
            throw new IllegalArgumentException(String.format("解析xml文件%s/%s错误!", channel, serviceName), e);
        }

        this.validate(r.getFields(), serviceName);

        synchronized (responses) {
            responses.computeIfAbsent(channel, x -> new HashMap<>());
            if (responses.get(channel).containsKey(serviceName)) {
                BeanUtils.copyProperties(r, responses.get(channel).get(serviceName));
            } else {
                responses.get(channel).put(serviceName, r);
            }
        }

        return r;
    }


    private void validate(List<Field> fields, String fileName) {
        for (Field f : fields) {

            if (f.getValueMapping() != null) {
                f.setValueMappings(new HashMap<>());
                for (ValueMapping vm : f.getValueMapping()) {
                    f.getValueMappings().put(vm.getOutbound(), vm.getInbound());
                }
            }

            if (f.getFields() != null) {
                if (f.getType() == null || String.class.isAssignableFrom(f.getType())) {
                    throw new IllegalArgumentException(fileName + ".xml配置错误，请设置[" + f.getName() + "]的类型java.util.List or java.util.Map");
                }
                validate(f.getFields(), fileName);
            }
        }
    }


    private Resource getResponseFilePath(String channel, String serviceName) throws IOException {
        String path = channelProperties.getPath() + File.separatorChar + channel + File.separatorChar + serviceName + ".xml";
        return new PathMatchingResourcePatternResolver().getResources(path)[0];

    }

    private class ReloadResponseConfiguration implements Runnable {
        @Override
        public void run() {

            responses.entrySet().forEach(entry -> {

                entry.getValue().entrySet().forEach(svc -> {
                    try {
                        Resource res = getResponseFilePath(entry.getKey(), svc.getKey());
                        if (ResourceUtils.isJarFileURL(res.getURL())) {
                            return;
                        }
                        if (res.lastModified() > svc.getValue().getLastModified()) {
                            parseResponse(entry.getKey(), svc.getKey());
                            log.debug("reload {}/{}.xml ", entry.getKey(), svc.getKey());
                        }
                    } catch (Exception e) {
                        log.warn("", e);
                    }
                });

            });
        }

    }


    @SuppressWarnings("unchecked")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.channelProperties = applicationContext.getBean(ChannelProperties.class);
    }

}
