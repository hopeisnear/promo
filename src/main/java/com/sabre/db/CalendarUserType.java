/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CalendarUserType implements CompositeUserType
{
    @Override
    public Object deepCopy(Object value) throws HibernateException
    {
        // Return the value if not a supported type
        Object ret = value;

        if (value instanceof Calendar)
        {
            ret = ((Calendar) value).clone();
        }

        return ret;
    }

    @Override
    public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException
    {
        return deepCopy(cached);
    }

    @Override
    public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException
    {
        return (Serializable) deepCopy(value);
    }

    @Override
    public int hashCode(Object value) throws HibernateException
    {
        return ((Calendar) value).hashCode();
    }

    /**
     * During merge, replace the existing (target) value in the entity we are merging to with a new (original) value from the detached entity we are merging. For immutable objects, or null values, it is safe to simply
     * return the first parameter. For mutable objects, it is safe to return a copy of the first parameter. However, since composite user types often define component values, it might make sense to recursively replace
     * component values in the target object.
     */
    @Override
    public Object replace(Object original, Object target, SessionImplementor session,
            Object owner) throws HibernateException
    {
        if (!(original instanceof Calendar))
        {
            return null;
        }

        return ((Calendar) original).clone();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException
    {
        if (x == y)
        {
            return true;
        }
        if (x == null || y == null)
        {
            return false;
        }
        return x.equals(y);
    }

    @Override
    public String[] getPropertyNames()
    {
        return new String[] { "timeinmillis", "timezone" };
    }

    @Override
    public Type[] getPropertyTypes()
    {
        return new Type[] { StandardBasicTypes.LONG, StandardBasicTypes.TIMEZONE };
    }

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException
    {
        Calendar calendar = (Calendar) component;

        if (property == 0)
        {
            return calendar.getTimeInMillis();
        }
        else
        {
            return calendar.getTimeZone().getID();
        }
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException
    {
        Calendar calendar = (Calendar) component;

        if (property == 0)
        {
            calendar.setTimeInMillis((Long) value);
        }
        else if (property == 1)
        {
            TimeZone timeZone = TimeZone.getTimeZone((String) value);
            calendar.setTimeZone(timeZone);
        }
    }

    @Override
    public boolean isMutable()
    {
        return true;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException
    {
        Long timeInMillis = rs.getLong(names[0]);
        if (rs.wasNull())
        {
            return null;
        }

        // Use the default timezone if one has not been provided
        String timeZoneId = rs.getString(names[1]);
        TimeZone timeZone = timeZoneId != null && timeZoneId.trim().length() > 0
                ? TimeZone.getTimeZone(timeZoneId) : TimeZone.getDefault();

        // Create the calendar object with the correct timezone and set the time
        Calendar cal = new GregorianCalendar(timeZone);
        cal.setTimeInMillis(timeInMillis);

        return cal;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException
    {
        if (value == null)
        {
            st.setNull(index, Types.BIGINT);
            st.setNull(index + 1, Types.VARCHAR);
        }
        else
        {
            Calendar calendar = (Calendar) value;
            st.setLong(index, calendar.getTimeInMillis());
            st.setString(index + 1, calendar.getTimeZone().getID());
        }
    }

    @Override
    public Class returnedClass()
    {
        return Calendar.class;
    }
}
