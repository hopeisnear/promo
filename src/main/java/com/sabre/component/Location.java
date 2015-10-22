/* Copyright 2012 Sabre Holdings */
package com.sabre.component;


import java.io.Serializable;
import java.util.Objects;

/**
 * A "Location" is a loosely defined concept - it may represent an individual airport, for example LHR, or it may represent an entire continent such as Europe.
 */
public abstract class Location implements Serializable, CodeType, CriteriaObject
{
    private Long id;
    private int version;

    private String code;
    private boolean highlight;

    /**
     * Cached hashCode for performance
     */
    private transient int cachedHashCode = 0;

    public Location()
    {
    }

    public Location(String code)
    {
        this.code = code;
    }

    /**
     * Gets the unique ID of this instance.
     * 
     * @return A Long that uniquely identifies this instance.
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Sets the unique ID of this instance. Use for testing only
     * 
     * @param id
     *            id
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * Gets the version indicator used for optimistic locking.
     * 
     * @return An int indicating the current "version" of this instance.
     */
    @SuppressWarnings("unused")
    private int getVersion()
    {
        return version;
    }

    /**
     * Sets the version indicator used for optimisitic locking.
     * 
     * @param version
     *            version
     */
    @SuppressWarnings("unused")
    private void setVersion(int version)
    {
        this.version = version;
    }

    /**
     * A code for this location. This may be a three letter IATA airport or city code.
     * 
     * @return code
     */
    @Override
    public String getCode()
    {
        return code;
    }

    /**
     * A code for this location. This may be a three letter IATA airport or city code.
     * 
     * @return code
     * 
     */

    /**
     * Sets the code for this location.
     * 
     * @param code
     *            location code
     */
    public void setCode(String code)
    {
        cachedHashCode = 0;
        this.code = code;
    }

    /**
     * Gets a key that can be used to look up a description for this location from a resource bundle.
     * 
     * @return a key that can be used to look up a description for this location from a resource bundle.
     */
    public String getDescriptionKey()
    {
        return "Location." + code;
    }

    /**
     * Gets the highlight attribute of Location
     * 
     * @return
     */
    public boolean isHighlight()
    {
        return highlight;
    }

    public void setHighlight(boolean highlight)
    {
        this.highlight = highlight;
    }

    /**
     * Tests if a location is part of this location or not. This could be used, for example to test if a particular airport is within a defined geographic region (assuming both had been defined as "locations".
     * <p/>
     * <p/>
     * Note that this operation is <i>not </i> symmetric. That is, <code>a.hasSubLocation(b)</code> may not yield the same result as <code>b.hasSubLocation(a)</code>. For example,
     * <code>belgium.hasSubLocation(brussels)</code> might return true, but <code>brussels.hasSubLocation(belgium)</code> might return false.
     * <p/>
     * <p/>
     * The default implementation simply uses "equals" to perform the test, so <code>A.hasSubLocation(A)</code> will always return true.
     * 
     * @param otherLocation
     *            The other location to test
     * @return True if this Location has a the given location as a sub-location.
     */
    public boolean hasSubLocation(Location otherLocation)
    {
        return this.equals(otherLocation);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Location other = (Location) o;
        return Objects.equals(code, other.getCode());
    }

    @Override
    public int hashCode()
    {
        int result = cachedHashCode;
        if (result == 0)
        {
            result = Objects.hashCode(code);
            result = 37 * result + getClass().getSimpleName().hashCode();
            cachedHashCode = result;
        }
        return result;
    }

    @Override
    public final String toString()
    {
        return code;
    }
}
