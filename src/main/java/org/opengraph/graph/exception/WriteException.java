package org.opengraph.graph.exception;

/**
 * A checked exception that might be thrown while performing write operations.
 * 
 * @author jon
 * 
 */
public class WriteException extends Exception {

    private static final long serialVersionUID = -7737110817574468607L;

    public WriteException(Exception cause) {
        super(cause);
    }

    public WriteException(String message, Exception cause) {
        super(message, cause);
    }

}
