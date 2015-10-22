/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;
import java.util.Map;

/**
 * An interface defining a criteria model. A criteria evaluates a condition. The conditions are part of the Criteria instance. placed on an object and returns a boolean result.
 */
public interface Criteria<T> extends Serializable
{
    /**
     * The method evaluates a condition on the object. If the object satisfies the condition true is returned. Otherwise the method returns false indicating the object does not satisfy the condition.
     * 
     * @param o
     *            The object to check if it satisfies a condition.
     * @return true if the criteria is satisfied by the object o
     */
    public boolean evaluate(T o);

    /**
     * @param o
     *            object to check
     * @param args
     *            context info
     * @return true if the criteria is satisfied by the object o and the context info
     */
    public boolean evaluate(T o, Map<String, Object> args);
}
