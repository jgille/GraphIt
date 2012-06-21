package org.graphit.common.procedures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.graphit.common.predicates.Predicate;

/**
 * A {@link Procedure} that collects processed elements in a list.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the processed elements.
 */
public class CollectorProcedure<T> implements Procedure<T> {

    private final Predicate<T> predicate;
    private final List<T> elements = new ArrayList<T>();

    /**
     * Creates a new procedure.
     */
    public CollectorProcedure() {
        this(new Predicate<T>() {

            @Override
            public boolean accepts(T element) {
                return true;
            }
        });
    }

    /**
     * Creates a new filtered procedure.
     * 
     * @param predicate
     *            A predicate used to decide whether or not to collect an
     *            element.
     */
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

    /**
     * Gets all collected elements.
     * 
     */
    public List<T> getElements() {
        return Collections.unmodifiableList(elements);
    }
}
