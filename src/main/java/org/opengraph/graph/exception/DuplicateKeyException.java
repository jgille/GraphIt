package org.opengraph.graph.exception;

/**
 * This exception is thrown when an already existing entity is created.
 *
 * @author jon
 *
 */
public class DuplicateKeyException extends IllegalArgumentException {

    private static final long serialVersionUID = -6824895307499666274L;

    /**
     * Creates a new exception for the provided duplicated key.
     * 
     * @param key
     */
    public DuplicateKeyException(Object key) {
        super(String.format("Duplicate key encountered: %s", key.toString()));
    }

}
