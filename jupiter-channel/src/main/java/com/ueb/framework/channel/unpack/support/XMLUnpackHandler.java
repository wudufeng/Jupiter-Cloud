package com.ueb.framework.channel.unpack.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.HierarchicalReloadableConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.springframework.stereotype.Component;

import com.ueb.framework.channel.config.Response;
import com.ueb.framework.channel.config.Response.Field;
import com.ueb.framework.channel.unpack.UnpackHandler;
import com.ueb.framework.channel.util.XmlUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component(value = "XML")
public class XMLUnpackHandler extends UnpackHandler {

    @Override
    public Map<String, Object> handle(byte[] respData, Response response) {

        XMLConfiguration xmlcfg = XmlUtils.readxml(respData);

        List<Field> field = response.getFields();
        Map<String, Object> result = new HashMap<>(field.size());

        for (Field f : field) {
            this.transform(xmlcfg, f, result);
        }

        return result;
    }


    private void transform(AbstractConfiguration xmlcfg, Field f, Map<String, Object> result) {

        if (List.class.isAssignableFrom(f.getType())) {
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
                for (Field sf : f.getFields()) {
                    this.transform(cfg, sf, map);
                }
            }
            result.put(f.getName(), items);

        } else if (Map.class.isAssignableFrom(f.getType())) {
            SubnodeConfiguration sub = null;
            try {
                sub = xmlcfg instanceof SubnodeConfiguration ? ((SubnodeConfiguration) xmlcfg).configurationAt(f.getPath())
                        : ((XMLConfiguration) xmlcfg).configurationAt(f.getPath());
            } catch (IllegalArgumentException e) {
                log.warn("找不到合适的xpath[{}]!", f.getPath());
                return;
            }
            Map<String, Object> map = new HashMap<>();
            result.put(f.getName(), map);
            for (Field sf : f.getFields()) {
                this.transform(sub, sf, map);
            }
        } else {
            this.convertValue(xmlcfg.getString(f.getPath()), f, result);
        }
    }

}
