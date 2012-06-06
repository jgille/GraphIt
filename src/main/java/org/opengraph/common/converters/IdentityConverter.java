package org.opengraph.common.converters;

import org.springframework.core.convert.converter.Converter;

public class IdentityConverter<T> implements Converter<T, T> {

    @Override
    public T convert(T source) {
        return source;
    }

}
