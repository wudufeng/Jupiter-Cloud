package com.jupiterframework.channel.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.jupiterframework.channel.config.Response.Field;
import com.jupiterframework.channel.config.Response.Field.ValueMapping;
import com.jupiterframework.channel.util.XmlUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@lombok.Data
@Configuration
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = ChannelProperties.PREFIX)
public class ChannelProperties implements InitializingBean {
    public static final String PREFIX = "channel";

    private Map<String /* name */, Channel> channelConfigurations = new HashMap<>();


    public Channel getChannelConfiguration(String name) {
        return channelConfigurations.get(name);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        for (Entry<String, Channel> chnl : channelConfigurations.entrySet()) {
            chnl.getValue().setName(chnl.getKey());

            File[] files = new ClassPathResource(PREFIX + File.separatorChar + chnl.getKey()).getFile().listFiles((File dir, String name) -> name.endsWith(".ftl"));

            for (File f : files) {
                for (String line : Files.readAllLines(Paths.get(f.getAbsolutePath()))) {
                    if (line.startsWith("<request ")) {
                        Service svc = XmlUtils.parse(line + "</request>", Service.class);
                        svc.setChannel(chnl.getValue());
                        svc.setName(f.getName().substring(0, f.getName().length() - 4));
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

        Response r = XmlUtils.parse(new File(getResponseFilePath(svc)), Response.class);

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
