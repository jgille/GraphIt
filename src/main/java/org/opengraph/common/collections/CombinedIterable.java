package org.opengraph.common.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CombinedIterable<T> implements Iterable<T> {

    private final List<Iterable<T>> iterables;

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

    private static class CombinedIterator<T> implements Iterator<T> {

        private final List<Iterator<T>> iterators;
        private int current = 0;

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
