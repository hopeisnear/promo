/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

/**
 * Defines a basic "Converter" interface.
 * 
 * @param <T>
 *            Type of object to be converted from
 * @param <R>
 *            Object to be used as criteria for conversion
 */
public interface Converter<T, R>
{
    /**
     * The convert method performs a conversion of <tt>Class&lt;T&gt;</tt> entity into an object of the same entity.
     * 
     * @param context
     *            a <tt>Class&lt;R&gt;</tt> criteria for conversion
     * @param criteria
     *            the criteria
     * 
     * @return the T
     */
    public abstract T convert(T context, R criteria);
}
