package org.graphit.graph.exception;

/**
 * An unchecked exception.
 *
 * @author jon
 *
 */
public class GraphException extends RuntimeException {

    private static final long serialVersionUID = 7830016416729135425L;

    /**
     * Creates a new exception.
     */
    public GraphException(String message, Throwable cause) {
        super(message, cause);
    }

}
