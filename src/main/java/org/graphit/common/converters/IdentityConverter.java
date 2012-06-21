package org.graphit.common.converters;

import org.springframework.core.convert.converter.Converter;

/**
 * A converter that does nothing, i.e. return the source itself.
 * 
 * @author jon
 * 
 * @param <T>
 *            The generic type of the objects that are converted.
 */
public class IdentityConverter<T> implements Converter<T, T> {

    @Override
    public T convert(T source) {
        return source;
    }

}
