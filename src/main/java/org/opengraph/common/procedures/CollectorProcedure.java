package org.opengraph.common.procedures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opengraph.common.predicates.Predicate;

public class CollectorProcedure<T> implements Procedure<T> {

    private final Predicate<T> predicate;
    private final List<T> elements = new ArrayList<T>();

    public CollectorProcedure() {
        this(new Predicate<T>() {

            @Override
            public boolean accepts(T element) {
                return true;
            }
        });
    }

    public CollectorProcedure(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean apply(T element) {
        if (!predicate.accepts(element)) {
            return false;
        }
        elements.add(element);
        return true;
    }

    public List<T> getElements() {
        return Collections.unmodifiableList(elements);
    }
}
