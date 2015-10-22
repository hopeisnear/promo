/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class FieldCriteriaLocation extends FieldCriteria<Location, Location> implements Serializable
{
    private Location includeValue1;
    private Location includeValue2;
    private Location includeValue3;
    private Location includeValue4;
    private Location includeValue5;

    private Location excludeValue1;
    private Location excludeValue2;
    private Location excludeValue3;
    private Location excludeValue4;
    private Location excludeValue5;

    private Collection<Location> includes = null;
    private Collection<Location> excludes = null;

    public static final int MAX_LOCATION_COUNT = 5;

    @Override
    protected boolean checkMatches(Location value, Location paramObject)
    {
        return value.hasSubLocation(paramObject);
    }

    @Override
    public Collection<Location> getIncludes()
    {
        if (includes == null)
        {
            final ArrayList<Location> includes = new ArrayList<Location>(5);

            if (includeValue1 != null)
            {
                includes.add(includeValue1);
            }

            if (includeValue2 != null)
            {
                includes.add(includeValue2);
            }

            if (includeValue3 != null)
            {
                includes.add(includeValue3);
            }

            if (includeValue4 != null)
            {
                includes.add(includeValue4);
            }

            if (includeValue5 != null)
            {
                includes.add(includeValue5);
            }

            this.includes = includes;
        }

        return includes;
    }

    @Override
    public Collection<Location> getExcludes()
    {
        if (excludes == null)
        {
            final ArrayList<Location> excludes = new ArrayList<Location>(5);

            if (excludeValue1 != null)
            {
                excludes.add(excludeValue1);
            }

            if (excludeValue2 != null)
            {
                excludes.add(excludeValue2);
            }

            if (excludeValue3 != null)
            {
                excludes.add(excludeValue3);
            }

            if (excludeValue4 != null)
            {
                excludes.add(excludeValue4);
            }

            if (excludeValue5 != null)
            {
                excludes.add(excludeValue5);
            }

            this.excludes = excludes;
        }

        return excludes;
    }

    @Override
    public void addExcludeField(Location excludeField)
    {
        excludes = null;

        if (excludeValue1 == null)
        {
            excludeValue1 = excludeField;
        }

        else if (excludeValue2 == null)
        {
            excludeValue2 = excludeField;
        }

        else if (excludeValue3 == null)
        {
            excludeValue3 = excludeField;
        }

        else if (excludeValue4 == null)
        {
            excludeValue4 = excludeField;
        }

        else if (excludeValue5 == null)
        {
            excludeValue5 = excludeField;
        }

        else
        {
            throw new IllegalArgumentException("too many fields");
        }
        checkCriteriaState();
    }

    @Override
    public void addIncludeField(Location includeField)
    {
        includes = null;
        if (includeValue1 == null)
        {
            includeValue1 = includeField;
        }

        else if (includeValue2 == null)
        {
            includeValue2 = includeField;
        }

        else if (includeValue3 == null)
        {
            includeValue3 = includeField;
        }

        else if (includeValue4 == null)
        {
            includeValue4 = includeField;
        }

        else if (includeValue5 == null)
        {
            includeValue5 = includeField;
        }

        else
        {
            throw new IllegalArgumentException("too many fields");
        }
        checkCriteriaState();
    }

    @Override
    public void clearFields()
    {
        excludes = null;
        includes = null;

        includeValue1 = null;
        includeValue2 = null;
        includeValue3 = null;
        includeValue4 = null;
        includeValue5 = null;

        excludeValue1 = null;
        excludeValue2 = null;
        excludeValue3 = null;
        excludeValue4 = null;
        excludeValue5 = null;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getExcludeValue1()
    {
        return excludeValue1;
    }

    protected void setExcludeValue1(Location excludeValue1)
    {
        excludes = null;
        this.excludeValue1 = excludeValue1;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getExcludeValue2()
    {
        return excludeValue2;
    }

    protected void setExcludeValue2(Location excludeValue2)
    {
        excludes = null;
        this.excludeValue2 = excludeValue2;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getExcludeValue3()
    {
        return excludeValue3;
    }

    protected void setExcludeValue3(Location excludeValue3)
    {
        excludes = null;
        this.excludeValue3 = excludeValue3;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getIncludeValue1()
    {
        return includeValue1;
    }

    protected void setIncludeValue1(Location includeValue1)
    {
        includes = null;
        this.includeValue1 = includeValue1;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getIncludeValue2()
    {
        return includeValue2;
    }

    protected void setIncludeValue2(Location includeValue2)
    {
        includes = null;
        this.includeValue2 = includeValue2;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getIncludeValue3()
    {
        return includeValue3;
    }

    protected void setIncludeValue3(Location includeValue3)
    {
        includes = null;
        this.includeValue3 = includeValue3;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getExcludeValue4()
    {
        return excludeValue4;
    }

    protected void setExcludeValue4(Location excludeValue4)
    {
        excludes = null;
        this.excludeValue4 = excludeValue4;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getExcludeValue5()
    {
        return excludeValue5;
    }

    protected void setExcludeValue5(Location excludeValue5)
    {
        excludes = null;
        this.excludeValue5 = excludeValue5;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getIncludeValue4()
    {
        return includeValue4;
    }

    protected void setIncludeValue4(Location includeValue4)
    {
        includes = null;
        this.includeValue4 = includeValue4;
    }

    /**
     * @return location
     * 
     *         class="com.eb2.qtrip.model.location.Location" cascade="none"
     */
    protected Location getIncludeValue5()
    {
        return includeValue5;
    }

    protected void setIncludeValue5(Location includeValue5)
    {
        includes = null;
        this.includeValue5 = includeValue5;
    }
}
