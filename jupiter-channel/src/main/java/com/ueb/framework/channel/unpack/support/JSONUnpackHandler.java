package com.ueb.framework.channel.unpack.support;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ReadContext;
import com.ueb.framework.channel.config.Response;
import com.ueb.framework.channel.config.Response.Field;
import com.ueb.framework.channel.unpack.UnpackHandler;


@Component(value = "JSON")
public class JSONUnpackHandler extends UnpackHandler {

    com.jayway.jsonpath.Configuration config = Configuration.builder().options(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL).build();


    @Override
    public Map<String, Object> handle(byte[] respData, Response response) {
        ReadContext ctx = JsonPath.parse(new ByteArrayInputStream(respData), config);

        List<Field> field = response.getFields();
        Map<String, Object> result = new HashMap<>(field.size());

        for (Field f : field) {
            this.transform(ctx, "$", f, result);
        }

        return result;
    }


    private void transform(ReadContext obj, String parentPath, Field f, Map<String, Object> result) {
        String path = f.getPath().startsWith("$.") ? f.getPath() : String.format("%s.%s", parentPath, f.getPath());

        if (List.class.isAssignableFrom(f.getType())) {
            Integer len = obj.read(path + ".length()", Integer.class);
            if (len == null)
                return;

            List<Map<String, Object>> items = new ArrayList<>();

            for (int i = 0; i < len; i++) {
                Map<String, Object> map = new HashMap<>();
                items.add(map);
                for (Field sf : f.getFields()) {
                    this.transform(obj, String.format("%s[%d]", path, i), sf, map);
                }
            }
            result.put(f.getName(), items);

        } else if (Map.class.isAssignableFrom(f.getType())) {
            Map<String, Object> map = new HashMap<>();
            result.put(f.getName(), map);
            for (Field sf : f.getFields()) {
                this.transform(obj, path, sf, map);
            }
        } else {
            String val = obj.read(path, String.class);
            this.convertValue(val, f, result);
        }
    }

}
