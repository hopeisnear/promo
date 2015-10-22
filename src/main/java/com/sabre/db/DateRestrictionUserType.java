/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateRestrictionUserType implements CompositeUserType
{
    /**
     * Array of all possible date restrictions. Note that order is important here, as the index into this array is used as a discrimiator in the database. So changing the order could result in an incorrect type being
     * loaded from the DB!
     */
    private static Class[] dateRestrictionTypes = { OnOrAfterDateRestriction.class, OnOrBeforeDateRestriction.class,
            AfterDateRestriction.class, BeforeDateRestriction.class, OnOrBeforeDateTimeRestriction.class,
            OnOrAfterDateTimeRestriction.class };

    @Override
    public Object deepCopy(Object value) throws HibernateException
    {
        // Simply return value as date restrictions are immutable
        return value;
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
    public int hashCode(Object value) throws HibernateException
    {
        return ((DateRestriction) value).hashCode();
    }

    @Override
    public String[] getPropertyNames()
    {
        return new String[] { "date", "timezone", "isRolling", "comparator" };
    }

    @Override
    public Type[] getPropertyTypes()
    {
        return new Type[] { StandardBasicTypes.CALENDAR, StandardBasicTypes.TIMEZONE, StandardBasicTypes.BOOLEAN, StandardBasicTypes.INTEGER };
    }

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException
    {
        DateRestriction restriction = (DateRestriction) component;
        if (property == 0)
        {
            return restriction.getDate();
        }
        else if (property == 1)
        {
            return restriction.getDate().getTimeZone().getID();
        }
        else if (property == 2)
        {
            if (restriction.getDate() instanceof RollingCalendar)
            {
                return Boolean.TRUE;
            }
            else
            {
                return Boolean.FALSE;
            }
        }
        else if (property == 3)
        {
            for (int i = 0; i < dateRestrictionTypes.length; i++)
            {
                if (restriction.getClass().equals(dateRestrictionTypes[i]))
                {
                    return i;
                }
            }
        }

        return null;
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException
    {
        throw new UnsupportedOperationException("DateRestriction classes are immutable");
    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    /**
     * During merge, replace the existing (target) value in the entity we are merging to with a new (original) value from the detached entity we are merging. For immutable objects, or null values, it is safe to simply
     * return the first parameter. For mutable objects, it is safe to return a copy of the first parameter. However, since composite user types often define component values, it might make sense to recursively replace
     * component values in the target object.
     * 
     * @see CompositeUserType#replace(Object, Object, SessionImplementor, Object)
     */
    @Override
    public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException
    {
        return original;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException
    {
        Object ret = null;

        Timestamp ts = rs.getTimestamp(names[0]);
        if (rs.wasNull())
        {
            return null;
        }

        // Use the default timezone if one has not been provided
        String timeZoneId = rs.getString(names[1]);
        TimeZone timeZone = timeZoneId != null && timeZoneId.trim().length() > 0 ? TimeZone.getTimeZone(timeZoneId)
                : TimeZone.getDefault();

        boolean isRolling = rs.getBoolean(names[2]);
        if (rs.wasNull())
        {
            return null;
        }

        GregorianCalendar date = isRolling ? new RollingCalendar(timeZone) : new GregorianCalendar(timeZone);
        // *** Set the date into the calendar object field by field to ensure
        // that it's in the desired timezone
        // *** - doing a setTime() will assume the given time is in GMT/UTC and
        // therefore apply conversion rules
        // *** this is not how we want it was we don't store the dates in the
        // database in GMT
        GregorianCalendar tmp = new GregorianCalendar();
        tmp.setTime(ts);
        date.set(tmp.get(Calendar.YEAR), tmp.get(Calendar.MONTH), tmp.get(Calendar.DAY_OF_MONTH),
                tmp.get(Calendar.HOUR_OF_DAY), tmp.get(Calendar.MINUTE), tmp.get(Calendar.SECOND));
        date.set(Calendar.MILLISECOND, tmp.get(Calendar.MILLISECOND));

        int restrictionType = rs.getInt(names[3]);
        if (rs.wasNull())
        {
            return null;
        }

        Class restrictionClass = dateRestrictionTypes[restrictionType];

        try
        {
            Constructor constructor = restrictionClass.getConstructor(new Class[] { Calendar.class });
            ret = constructor.newInstance(date);
        }
        catch (Exception e)
        {
            throw new HibernateException("Unable to create new " + restrictionClass.getName() + " instance", e);
        }

        return ret;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException
    {
        if (value == null)
        {
            st.setNull(index, Types.TIMESTAMP);
            st.setNull(index + 1, Types.VARCHAR);
            st.setNull(index + 2, Types.SMALLINT);
            st.setNull(index + 3, Types.INTEGER);
        }
        else
        {
            DateRestriction restriction = (DateRestriction) value;
            // *** Create a new calendar with the default timezone and set the
            // date and time into it.
            // *** This will ensure the desired date & time are stored in the
            // database.
            // *** If we don't do this then the time in the database will be
            // converted to local time by assuming that the time in the object
            // is GMT
            Calendar cal = new GregorianCalendar(restriction.getDate().get(Calendar.YEAR), restriction.getDate().get(
                    Calendar.MONTH), restriction.getDate().get(Calendar.DAY_OF_MONTH), restriction.getDate().get(
                    Calendar.HOUR_OF_DAY), restriction.getDate().get(Calendar.MINUTE), restriction.getDate().get(
                    Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, restriction.getDate().get(Calendar.MILLISECOND));

            st.setTimestamp(index, new Timestamp(cal.getTime().getTime()));
            st.setString(index + 1, restriction.getDate().getTimeZone().getID());
            st.setBoolean(index + 2, restriction.getDate() instanceof RollingCalendar);
            for (int i = 0; i < dateRestrictionTypes.length; i++)
            {
                if (restriction.getClass().equals(dateRestrictionTypes[i]))
                {
                    st.setInt(index + 3, i);
                }
            }
        }
    }

    @Override
    public Class returnedClass()
    {
        return DateRestriction.class;
    }
}
