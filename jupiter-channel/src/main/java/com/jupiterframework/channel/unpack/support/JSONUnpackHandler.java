package com.jupiterframework.channel.unpack.support;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import com.jupiterframework.channel.config.Response;
import com.jupiterframework.channel.config.Response.Field;
import com.jupiterframework.channel.unpack.UnpackHandler;


@Component(value = "JSON")
public class JSONUnpackHandler extends UnpackHandler<ReadContext> {

    private com.jayway.jsonpath.Configuration config = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL).build();


    @Override
    protected ReadContext parseObj(byte[] respData) {
        return JsonPath.parse(new ByteArrayInputStream(respData), config);
    }


    @Override
    protected String getPath(String parentPath, Field f) {
        if (parentPath == null)
            parentPath = "$";
        String format = "".equals(f.getPath()) ? "%s%s" : "%s.%s";
        return f.getPath().startsWith("$.") ? f.getPath() : String.format(format, parentPath, f.getPath());
    }


    @Override
    protected <E> String readPathValue(ReadContext obj, String path, Class<E> clazz) {
        if (clazz == String.class)
            return obj.read(path, String.class);

        return JSON.toJSONString(obj.read(path));
    }


    @Override
    protected void handleMap(ReadContext obj, Field f, Map<String, Object> result, String path) {
        Map<String, Object> map = new HashMap<>();
        result.put(f.getName(), map);
        for (Field sf : f.getFields()) {
            super.transform(obj, path, sf, map);
        }
    }


    @Override
    protected void handleList(ReadContext obj, Field f, Map<String, Object> result, String path) {
        Integer len = obj.read(path + ".length()", Integer.class);
        if (len == null) {
            result.put(f.getName(), null);
            return;
        }

        List<Map<String, Object>> items = new ArrayList<>();

        for (int i = 0; i < len; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(LIST_ITEM_INDEX_KEY_STRING, i);
            items.add(map);
            String parentPath = String.format("%s[%d]", path, i);
            if (StringUtils.isNotBlank(f.getPayload())) {

                map.put(Response.PAYLOAD_KEY,
                    "true".equals(f.getPayload()) ? JSON.toJSONString(obj.read(parentPath)) : JSON.toJSONString(obj.read(parentPath + "." + f.getPayload())));
            }
            for (Field sf : f.getFields()) {
                this.transform(obj, parentPath, sf, map);
            }
        }
        result.put(f.getName(), items);
    }

}
