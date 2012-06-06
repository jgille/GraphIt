package org.opengraph.common.procedures;

public interface Procedure<T> {

    /**
     * Applies the procedure.
     * 
     */
    boolean apply(T element);

}
