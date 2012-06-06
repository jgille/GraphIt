package org.opengraph.graph.blueprints;

import java.util.Set;

import org.opengraph.properties.domain.Properties;

import com.tinkerpop.blueprints.Element;

public abstract class AbstractElement<K> implements Element {

    private final K id;
    private final Properties properties;

    protected AbstractElement(K id, Properties properties) {
        this.id = id;
        this.properties = properties;
    }

    @Override
    public Object getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public Set<String> getPropertyKeys() {
        return properties.getPropertyKeys();
    }

    @Override
    public void setProperty(String key, Object value) {
        properties.setProperty(key, value);
    }

    @Override
    public Object removeProperty(String key) {
        return properties.removeProperty(key);
    }

    @Override
    public K getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractElement<?> other = (AbstractElement<?>) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
