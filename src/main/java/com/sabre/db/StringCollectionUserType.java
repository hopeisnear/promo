/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class StringCollectionUserType implements UserType
{
    private static final int[] SQL_TYPES = { Types.VARCHAR };

    @Override
    public int[] sqlTypes()
    {
        return SQL_TYPES;
    }

    public static final String VALUE_SEPARATOR = ",";

    @Override
    public Object deepCopy(Object value) throws HibernateException
    {
        return value;
    }

    /**
     * During merge, replace the existing (target) value in the entity we are merging to with a new (original) value from the detached entity we are merging. For immutable objects, or null values, it is safe to simply
     * return the first parameter. For mutable objects, it is safe to return a copy of the first parameter. However, since composite user types often define component values, it might make sense to recursively replace
     * component values in the target object.
     */
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException
    {
        return original;
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
        return ((ArrayList) value).hashCode();
    }

    @Override
    public boolean isMutable()
    {
        return false;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException
    {
        return cached;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException
    {
        return (Serializable) value;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException
    {
        String field = resultSet.getString(names[0]);
        if (resultSet.wasNull() || StringUtils.isEmpty(field))
        {
            return new ArrayList<String>();
        }

        return new ArrayList<String>(Arrays.asList(field.split(VALUE_SEPARATOR)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void nullSafeSet(PreparedStatement statement, Object value, int index, SessionImplementor session) throws HibernateException, SQLException
    {
        if (value == null)
        {
            statement.setNull(index, Types.VARCHAR);
        }
        else
        {
            Collection<String> list = (Collection<String>) value;
            StringBuilder sb = new StringBuilder();
            for (String s : list)
            {
                if (sb.length() > 0)
                {
                    sb.append(VALUE_SEPARATOR);
                }
                sb.append(s);
            }
            final String string = sb.toString();

            statement.setString(index, string);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class returnedClass()
    {
        return ArrayList.class;
    }
}
