package com.jupiterframework.channel.unpack.support;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.HierarchicalReloadableConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.springframework.stereotype.Component;

import com.jupiterframework.channel.config.Response;
import com.jupiterframework.channel.config.Response.Field;
import com.jupiterframework.channel.unpack.UnpackHandler;
import com.jupiterframework.channel.util.XmlUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(value = "XML")
public class XMLUnpackHandler extends UnpackHandler<AbstractConfiguration> {

    @Override
    protected AbstractConfiguration parseObj(byte[] respData) {
        return XmlUtils.readxml(respData);
    }

    @Override
    protected String getPath(String parentPath, Field f) {
        return f.getPath();
    }

    @Override
    protected void handleList(AbstractConfiguration xmlcfg, Field f, Map<String, Object> result, String path) {
        List<HierarchicalConfiguration> cfgList = null;
        try {
            cfgList = xmlcfg instanceof XMLConfiguration ? ((XMLConfiguration) xmlcfg).configurationsAt(f.getPath())
                    : ((HierarchicalReloadableConfiguration) xmlcfg).configurationsAt(f.getPath());
        } catch (IllegalArgumentException e) {
            log.warn("找不到合适的xpath[{}]!", f.getPath());
            return;
        }
        List<Map<String, Object>> items = new ArrayList<>(cfgList.size());
        for (HierarchicalConfiguration cfg : cfgList) {
            Map<String, Object> map = new HashMap<>();
            items.add(map);

            if (f.isPayload()) {
                StringWriter buf = new StringWriter();
                try {
                    new XMLConfiguration(cfg).save(buf);
                } catch (ConfigurationException e) {
                    buf.append("read Error !").append(e.getMessage());
                }
                map.put(Response.PAYLOAD_KEY, buf.toString());
            }

            for (Field subField : f.getFields()) {
                transform(cfg, f.getPath(), subField, map);
            }
        }
        result.put(f.getName(), items);
    }

    @Override
    protected void handleMap(AbstractConfiguration xmlcfg, Field f, Map<String, Object> result, String path) {
        SubnodeConfiguration sub = null;
        try {
            sub = xmlcfg instanceof SubnodeConfiguration ? ((SubnodeConfiguration) xmlcfg).configurationAt(f.getPath()) : ((XMLConfiguration) xmlcfg).configurationAt(f.getPath());
        } catch (IllegalArgumentException e) {
            log.warn("找不到合适的xpath[{}]!", f.getPath());
            return;
        }
        Map<String, Object> map = new HashMap<>();
        result.put(f.getName(), map);
        for (Field sf : f.getFields()) {
            this.transform(sub, f.getPath(), sf, map);
        }
    }

    @Override
    protected String readPathValue(AbstractConfiguration xmlcfg, String path) {
        return xmlcfg.getString(path);
    }

}
