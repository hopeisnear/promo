/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class FieldCriteriaString extends FieldCriteria<String, String> implements Serializable
{
    private Collection<String> includeValues = new ArrayList<String>();
    private Collection<String> excludeValues = new ArrayList<String>();

    private boolean wildCardMatchAllowed = true;
    private boolean matchCase = false;

    @Override
    protected boolean checkMatches(String value, String paramObject)
    {
        if (wildCardMatchAllowed)
        {
            return StringUtil.matchesWildcard(paramObject, value, matchCase);
        }

        if (!matchCase)
        {
            value = value.toUpperCase();
            paramObject = paramObject.toUpperCase();
        }

        return value.equals(paramObject);
    }

    @Override
    protected boolean checkFieldStateOk(String o)
    {
        if (getFieldState() != null)
        {
            if (getFieldState().equals(FieldState.NOT_NULL) && StringUtils.isEmpty(o))
            {
                return false;
            }

            if (getFieldState().equals(FieldState.NULL) && StringUtils.isNotEmpty(o))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    protected boolean checkExcludesIncludes(Collection<String> o, Collection<String> excludes, Collection<String> includes)
    {
        if (o != null && o.size() == 1 && getFieldState() != null)
        {
            String val = o.iterator().next();
            if (StringUtils.isEmpty(val))
            {
                return checkFieldStateOk(val);
            }
        }

        return super.checkExcludesIncludes(o, excludes, includes);
    }

    @Override
    public Collection<String> getIncludes()
    {
        return getIncludeValues();
    }

    @Override
    public Collection<String> getExcludes()
    {
        return getExcludeValues();
    }

    @Override
    public void addExcludeField(String excludeField)
    {
        getExcludes().add(excludeField);
        validateFieldLength(getExcludes().toString());
        checkCriteriaState();
    }

    @Override
    public void addIncludeField(String includeField)
    {
        getIncludes().add(includeField);
        validateFieldLength(getIncludes().toString());
        checkCriteriaState();
    }

    @Override
    public void clearFields()
    {
        includeValues.clear();
        excludeValues.clear();
    }

    public boolean isMatchCase()
    {
        return matchCase;
    }

    public void setMatchCase(boolean matchCase)
    {
        this.matchCase = matchCase;
    }

    public boolean isWildCardMatchAllowed()
    {
        return wildCardMatchAllowed;
    }

    public void setWildCardMatchAllowed(boolean wildCardMatchAllowed)
    {
        this.wildCardMatchAllowed = wildCardMatchAllowed;
    }

    /**
     * @return
     * 
     */
    protected Collection<String> getIncludeValues()
    {
        return includeValues;
    }

    protected void setIncludeValues(Collection<String> includeValues)
    {
        this.includeValues = includeValues;
    }

    /**
     * @return
     * 
     */
    protected Collection<String> getExcludeValues()
    {
        return excludeValues;
    }

    protected void setExcludeValues(Collection<String> excludeValues)
    {
        this.excludeValues = excludeValues;
    }
}
