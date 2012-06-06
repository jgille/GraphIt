package org.opengraph.properties.domain;

import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

public abstract class PropertiesProxy implements Properties {

    private final boolean mutable;
    private final Properties properties;

    public PropertiesProxy(Properties properties, boolean mutable) {
        Assert.notNull(properties);
        this.properties = properties;
        this.mutable = true;
    }

    protected Properties getProperties() {
        return properties;
    }

    @Override
    public Object getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        Assert.isTrue(mutable, "This proxy is immutable");
        properties.setProperty(key, value);
    }

    @Override
    public Object removeProperty(String key) {
        Assert.isTrue(mutable, "This proxy is immutable");
        return properties.removeProperty(key);
    }

    @Override
    public boolean containsProperty(String key) {
        return properties.containsProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return properties.getPropertyKeys();
    }

    @Override
    public Map<String, Object> asPropertyMap() {
        return properties.asPropertyMap();
    }

}
