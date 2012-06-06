package org.opengraph.properties.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapProperties implements Properties {

    private final Map<String, Object> properties;

    public MapProperties() {
        this(10);
    }

    public MapProperties(int capacity) {
        this.properties = new HashMap<String, Object>(capacity);
    }

    @Override
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    @Override
    public Object removeProperty(String key) {
        return properties.remove(key);
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.containsKey(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return properties.keySet();
    }

    @Override
    public Map<String, Object> asPropertyMap() {
        return new HashMap<String, Object>(properties);
    }

    @Override
    public String toString() {
        return "MapProperties [properties=" + properties + "]";
    }
}
