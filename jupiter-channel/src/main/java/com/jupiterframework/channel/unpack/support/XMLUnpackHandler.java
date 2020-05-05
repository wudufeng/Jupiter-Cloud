package com.jupiterframework.channel.unpack.support;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.configuration2.tree.InMemoryNodeModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.jupiterframework.channel.config.Response;
import com.jupiterframework.channel.config.Response.Field;
import com.jupiterframework.channel.unpack.UnpackHandler;
import com.jupiterframework.channel.util.XmlUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component(value = "XML")
public class XMLUnpackHandler extends UnpackHandler<HierarchicalConfiguration<ImmutableNode>> {

    @Override
    protected HierarchicalConfiguration<ImmutableNode> parseObj(byte[] respData) {
        return XmlUtils.readxml(respData);
    }


    @Override
    protected String getPath(String parentPath, Field f) {
        return f.getPath();
    }


    @Override
    protected void handleList(HierarchicalConfiguration<ImmutableNode> xmlcfg, Field f, Map<String, Object> result, String path) {
        List<HierarchicalConfiguration<ImmutableNode>> cfgList = null;
        try {
            cfgList = xmlcfg.configurationsAt(f.getPath());
        } catch (IllegalArgumentException e) {
            log.warn("找不到合适的xpath[{}]!", f.getPath());
            return;
        }
        List<Map<String, Object>> items = new ArrayList<>(cfgList.size());
        int i = 0;
        for (HierarchicalConfiguration<ImmutableNode> cfg : cfgList) {
            Map<String, Object> map = new HashMap<>();
            map.put(LIST_ITEM_INDEX_KEY_STRING, i++);
            items.add(map);

            if (StringUtils.isNotBlank(f.getPayload())) {
                StringWriter buf = new StringWriter();
                try {
                    new XMLConfiguration(cfg).write(buf);
                } catch (ConfigurationException | IOException e) {
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
    protected void handleMap(HierarchicalConfiguration<ImmutableNode> xmlcfg, Field f, Map<String, Object> result, String path) {
        HierarchicalConfiguration<ImmutableNode> sub = null;
        try {
            sub = xmlcfg.configurationAt(f.getPath());
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
    protected <E> String readPathValue(HierarchicalConfiguration<ImmutableNode> xmlcfg, String path, Class<E> clazz) {
        if (clazz == String.class)
            return xmlcfg.getString(path);

        try {
            HierarchicalConfiguration<ImmutableNode> node = xmlcfg.configurationAt(path);
            // 此方法会将表情字符转换 "&#55356;&#57218;"
            if (node.getNodeModel() instanceof InMemoryNodeModel) {
                StringBuilder content = new StringBuilder(xmlcfg.size());
                content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                toXMLString(content, ((InMemoryNodeModel) node.getNodeModel()).getRootNode());
                return content.toString();
            }
            StringWriter sw = new StringWriter();
            new XMLConfiguration(node).write(sw);
            return sw.toString();
        } catch (IllegalArgumentException e) {
            return "";
        } catch (ConfigurationException | IOException e) {
            throw new IllegalArgumentException(path + "解析错误!", e);
        }

    }


    public void toXMLString(StringBuilder content, ImmutableNode node) {

        content.append("<").append(node.getNodeName());
        if (node.getAttributes() != null && !node.getAttributes().isEmpty()) {
            for (Entry<String, Object> entry : node.getAttributes().entrySet()) {
                content.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
            }

        }
        content.append(">");

        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
            if (node.getValue() != null) {
                content.append("<").append(node.getValue());
                content.append(">");
            }
            for (ImmutableNode child : node.getChildren()) {
                toXMLString(content, child);
            }
        } else {
            content.append(node.getValue());
        }

        content.append("</").append(node.getNodeName()).append(">");
    }

}
