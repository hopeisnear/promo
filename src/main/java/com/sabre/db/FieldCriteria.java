/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Base class for a field of a criteria implementation
 * 
 * X is the criteria attribute type Y is the collection type of the parameter to check against
 */
public abstract class FieldCriteria<X, Y> implements Serializable
{
    /** Due to DB limitations field length has to be shorter than 256 chars */
    private static final int MAX_FIELD_LENGTH = 255;

    // NOTE: in the case of string fields then null/not-null is equivalent to
    // empty / not-empty. That is "" is equivalent to a null string type.

    public enum FieldState
    {
        NOT_NULL, NULL
    }

    private FieldState fieldState = null;

    /**
     * If true all values must satisfy one rule to be valid If false any one value can be satisfied to be valid
     * 
     */
    private boolean paramAttributesMustAllBeMatched = false;

    private Boolean criteriaState = null;

    public boolean isDefined()
    {
        if (criteriaState == null)
        {
            checkCriteriaState();
        }
        return criteriaState;
    }

    protected void checkCriteriaState()
    {
        boolean result = fieldState != null || getExcludes().size() > 0 || getIncludes().size() > 0;
        criteriaState = result;
    }

    public boolean isValid(Y o)
    {
        if (!checkFieldStateOk(o))
        {
            return false;
        }

        Collection<X> excludes = getExcludes();
        Collection<X> includes = getIncludes();

        if (excludes.size() == 0 && includes.size() == 0)
        {
            return true;
        }

        return checkExcludesIncludes(Arrays.asList(o), excludes, includes);
    }

    protected boolean checkFieldStateOk(Y o)
    {
        if (fieldState != null)
        {
            if (fieldState.equals(FieldState.NOT_NULL) && o == null)
            {
                return false;
            }

            if (fieldState.equals(FieldState.NULL) && o != null)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Check if parameter is valid, Excluded attributes are AND'ed Included attributes are OR'ed
     * 
     * @param o
     *            must be a collection of type Y
     * @return
     */
    public boolean isValid(Collection<Y> o)
    {
        if (!isDefined())
        {
            return true;
        }
        if (fieldState != null)
        {
            if (fieldState.equals(FieldState.NOT_NULL) && (checkParamNull(o)))
            {
                return false;
            }

            if (fieldState.equals(FieldState.NULL) && (!checkParamNull(o)))
            {
                return false;
            }
        }

        Collection<X> excludes = getExcludes();
        Collection<X> includes = getIncludes();

        if (excludes.size() == 0 && includes.size() == 0)
        {
            return true;
        }

        return checkExcludesIncludes(o, excludes, includes);
    }

    protected boolean checkExcludesIncludes(Collection<Y> o, Collection<X> excludes, Collection<X> includes)
    {
        boolean everyExcludeMatches = true;
        boolean noExcludesMatch = true;

        if (excludes.size() > 0)
        {
            for (Y modelAttribute : o)
            {
                boolean matchesOne = checkModelValueMatchesOneLocalRule(modelAttribute, excludes);
                if (matchesOne)
                {
                    noExcludesMatch = false;
                }
                else
                {
                    everyExcludeMatches = false;
                }
            }

            if (paramAttributesMustAllBeMatched)
            {
                if (!noExcludesMatch)
                {
                    return false;
                }
            }

            if (!paramAttributesMustAllBeMatched)
            {
                if (everyExcludeMatches)
                {
                    return false;
                }
            }
        }

        if (includes.size() > 0)
        {
            boolean includeRuleSatisfied = false;

            for (Y modelAttribute : o)
            {
                if (checkModelValueMatchesOneLocalRule(modelAttribute, includes))
                {
                    // this model attribute has found a match
                    includeRuleSatisfied = true;
                }
                else if (paramAttributesMustAllBeMatched)
                {
                    return false;
                }
            }

            return includeRuleSatisfied;
        }

        // if no includes present & no excludes failed then pass
        return true;
    }

    protected boolean checkParamNull(Collection<Y> o)
    {
        if (o == null)
        {
            return true;
        }

        for (Y y : o)
        {
            if (y != null)
            {
                return false;
            }
        }

        return true;
    }

    protected Collection<X> removeNulls(Collection<X> list)
    {
        List<X> filteredList = new ArrayList<X>();

        for (X t : list)
        {
            if (t != null)
            {
                filteredList.add(t);
            }
        }

        return filteredList;
    }

    public abstract void clearFields();

    public abstract void addIncludeField(X includeField);

    public abstract void addExcludeField(X excludeField);

    /**
     * Return the not-null list of include attributes
     * 
     * Included attributes are OR'ed
     * 
     * @return include attributes
     */
    public abstract Collection<X> getIncludes();

    /**
     * Return the not-null list of sxclude attributes.
     * 
     * Excluded attributes are AND'ed
     * 
     * @return sxclude attributes
     */
    public abstract Collection<X> getExcludes();

    public int getFieldsCount()
    {
        if (!isDefined())
        {
            return 0;
        }
        return getIncludes().size() + getExcludes().size();
    }

    /**
     * Check whether param object matches value criteria
     * 
     * @param value
     * @param paramObject
     * @return
     */
    protected abstract boolean checkMatches(X value, Y paramObject);

    private boolean checkModelValueMatchesOneLocalRule(Y value, Collection<X> rules)
    {
        for (X include : rules)
        {
            if (checkMatches(include, value))
            {
                return true;
            }
        }

        return false;
    }

    public void setFieldNoSpecificState()
    {
        this.fieldState = null;
    }

    /** Set field to be not present */
    public void setFieldMustBeNull()
    {
        this.fieldState = FieldState.NULL;
    }

    /** Set field to be not null in all instances */
    public void setFieldMustNotBeNull()
    {
        this.fieldState = FieldState.NOT_NULL;
    }

    public boolean isFieldMustBeNull()
    {
        return fieldState != null && fieldState.equals(FieldState.NULL);
    }

    public boolean isFieldMustNotBeNull()
    {
        return fieldState != null && fieldState.equals(FieldState.NOT_NULL);
    }

    /**
     * @return type="com.eb2.qtrip.hibernate.type.FieldStateUserType"
     */
    protected FieldState getFieldState()
    {
        return fieldState;
    }

    protected void setFieldState(FieldState fieldState)
    {
        this.fieldState = fieldState;
    }

    /**
     * Due to DB limitations field length has to be shorter than 256 chars
     * 
     * @param field
     */
    protected void validateFieldLength(String field)
    {
        if (field != null && field.toCharArray().length > MAX_FIELD_LENGTH)
        {
            throw new IllegalArgumentException(String.format("Field %s length longer than allowed %s", field, MAX_FIELD_LENGTH));
        }
    }

    /**
     * 
     * @return
     */
    public boolean isParamAttributesMustAllBeMatched()
    {
        return paramAttributesMustAllBeMatched;
    }

    public void setParamAttributesMustAllBeMatched(boolean paramAttributesMustAllBeMatched)
    {
        this.paramAttributesMustAllBeMatched = paramAttributesMustAllBeMatched;
    }
}
