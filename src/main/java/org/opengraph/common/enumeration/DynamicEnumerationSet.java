package org.opengraph.common.enumeration;

import java.util.Collection;
import java.util.EnumSet;

/**
 * Used to mimic an {@link EnumSet} with dynamic content.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the elements in this set.
 */
public interface DynamicEnumerationSet<T extends DynamicEnumerableElement> {

    /**
     * Gets the element with the provided name.
     */
    T valueOf(String name);

    /**
     * Gets all elements in this set.
     */
    Collection<T> elements();

    /**
     * Gets the size (number of elements) of this set.
     */
    int size();

}
