package org.opengraph.common.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An {@link Iterable} that combines a collection of {@link Iterable}s, i.e. [1,
 * 2, 3] + [4, 5] -> [1, 2, 3, 4, 5].
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the elements in the {@link Iterable}.
 */
public class CombinedIterable<T> implements Iterable<T> {

    private final List<Iterable<T>> iterables;

    /**
     * Constructs a new instance combining the provided iterables.
     */
    public CombinedIterable(List<Iterable<T>> iterables) {
        this.iterables = iterables;
    }

    @Override
    public Iterator<T> iterator() {
        List<Iterator<T>> iterators = new ArrayList<Iterator<T>>(iterables.size());
        for (Iterable<T> it : iterables) {
            iterators.add(it.iterator());
        }
        return new CombinedIterator<T>(iterators);
    }

    private static final class CombinedIterator<T> implements Iterator<T> {

        private final List<Iterator<T>> iterators;
        private int current = 0;

        /**
         * Creates a new iterator instance.
         */
        public CombinedIterator(List<Iterator<T>> iterators) {
            this.iterators = iterators;
        }

        @Override
        public boolean hasNext() {
            if (current >= iterators.size()) {
                return false;
            }
            Iterator<T> it = iterators.get(current);
            if (it.hasNext()) {
                return true;
            }
            current++;
            return hasNext();
        }

        @Override
        public T next() {
            Iterator<T> it = iterators.get(current);
            return it.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported.");
        }

    }
}
