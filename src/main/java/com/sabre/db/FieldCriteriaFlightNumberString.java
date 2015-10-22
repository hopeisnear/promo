/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;

public class FieldCriteriaFlightNumberString extends FieldCriteriaString implements Serializable
{
    public FieldCriteriaFlightNumberString()
    {
    }

    public FieldCriteriaFlightNumberString(FieldCriteriaString fieldCriteriaString)
    {
        setIncludeValues(fieldCriteriaString.getIncludes());
        setExcludeValues(fieldCriteriaString.getExcludes());
        setFieldState(fieldCriteriaString.getFieldState());
        setWildCardMatchAllowed(fieldCriteriaString.isWildCardMatchAllowed());
        setParamAttributesMustAllBeMatched(fieldCriteriaString.isParamAttributesMustAllBeMatched());
    }

    @Override
    protected boolean checkMatches(String value, String paramObject)
    {
        FlightNumberCriteria flightNumberCriteria = new FlightNumberCriteria(value);
        return flightNumberCriteria.evaluate(paramObject);
    }
}
