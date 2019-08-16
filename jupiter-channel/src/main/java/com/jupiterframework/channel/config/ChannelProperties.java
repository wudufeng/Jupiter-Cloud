package com.jupiterframework.channel.config;

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
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jupiterframework.channel.config.Response.Field;
import com.jupiterframework.channel.config.Response.Field.ValueMapping;
import com.jupiterframework.channel.util.XmlUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@lombok.Data
@Configuration
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "channel")
public class ChannelProperties implements InitializingBean {
    public static final String PREFIX = "classpath*:channel";

    private Map<String /* name */, Channel> channelConfigurations = new HashMap<>();


    public Channel getChannelConfiguration(String name) {
        return channelConfigurations.get(name);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        for (Entry<String, Channel> chnl : channelConfigurations.entrySet()) {
            chnl.getValue().setName(chnl.getKey());

            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] files = resolver.getResources(String.format("%s/%s/*.ftl", PREFIX, chnl.getKey()));

            for (Resource res : files) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));) {
                    String line = reader.readLine();

                    if (line.startsWith("<request ")) {
                        String filename = res.getFilename();
                        Service svc = XmlUtils.parse(line + "</request>", Service.class);
                        svc.setChannel(chnl.getValue());
                        svc.setName(filename.substring(0, filename.length() - 4));
                        if (svc.getRequestMethod() == null) {
                            svc.setRequestMethod(chnl.getValue().getRequestMethod());
                        }

                        this.parseResponseConfig(svc);

                        chnl.getValue().getServices().put(svc.getName(), svc);
                        log.info("registry service {} {} {}", chnl.getValue().getName(), svc.getName(), svc.getDescription());

                        break;
                    }
                }

            }

        }

        ReloadResponseConfiguration reload = new ReloadResponseConfiguration();
        Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setDaemon(true).setNameFormat("reload-channel-reponse-xml-%d").build()).scheduleAtFixedRate(reload,
            30, 10, TimeUnit.SECONDS);

    }


    private void parseResponseConfig(Service svc) {
        String path = this.getResponseFilePath(svc);
        Response r;
        try {
            r = XmlUtils.parse(new PathMatchingResourcePatternResolver().getResources(path)[0].getInputStream(), Response.class);
        } catch (JAXBException | IOException e) {
            throw new IllegalArgumentException("解析xml文件" + path + "错误!", e);
        }

        this.validate(r.getFields(), svc.getName());

        svc.setResponse(r);

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


    private String getResponseFilePath(Service svc) {
        return PREFIX + File.separatorChar + svc.getChannel().getName() + File.separatorChar + svc.getName() + ".xml";
    }

    private class ReloadResponseConfiguration implements Runnable {
        @Override
        public void run() {
            channelConfigurations.values().forEach(chnl -> {

                chnl.getServices().values().forEach(svc -> {
                    try {
                        String path = getResponseFilePath(svc);
                        ClassPathResource res = new ClassPathResource(path);

                        if (res.lastModified() > svc.getResponse().getLastModified()) {
                            parseResponseConfig(svc);
                            svc.getResponse().setLastModified(res.lastModified());
                        }
                    } catch (IOException e) {
                        log.warn("", e);
                    }
                });

            });
        }

    }
}
