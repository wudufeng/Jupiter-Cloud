package com.jupiterframework.context;

import java.util.HashMap;
import java.util.Map;


public class ThreadLocalContext {
    private ThreadLocalContext() {
    }

    private static final ThreadLocal<Map<String, Object>> CTX_HOLDER = ThreadLocal.withInitial(HashMap::new);


    public static final void put(String key, Object value) {
        CTX_HOLDER.get().put(key, value);
    }


    @SuppressWarnings("unchecked")
    public static final <T> T get(String key) {
        return (T) CTX_HOLDER.get().get(key);
    }


    public static final void remove(String key) {
        CTX_HOLDER.get().remove(key);
    }


    public static final boolean contains(String key) {
        return CTX_HOLDER.get().containsKey(key);
    }


    public static final void clear() {
        CTX_HOLDER.get().clear();
    }

}
