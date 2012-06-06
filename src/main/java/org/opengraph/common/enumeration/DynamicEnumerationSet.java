package org.opengraph.common.enumeration;

import java.util.Collection;

public interface DynamicEnumerationSet<T extends DynamicEnumerableElement> {

    T valueOf(String name);

    Collection<T> elements();

    int size();

}
