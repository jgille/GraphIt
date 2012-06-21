package org.graphit.common.procedures;

/**
 * A generically typed procedure.
 *
 * @author jon
 *
 * @param <T>
 *            The generic type of the objects this procedure may be applied for,
 */
public interface Procedure<T> {

    /**
     * Applies the procedure.
     *
     * Returns a flag that among other things can be used to decide whether or
     * not to continue a forEach-loop or not.
     * 
     */
    boolean apply(T element);

}
