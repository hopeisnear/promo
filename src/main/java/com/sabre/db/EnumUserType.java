/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Base class for user types that store JDK 1.5 enums. To create a class that persists your very own enum, just subclass this and supply the appropriate type in the call the constructor.
 * <p/>
 * Enums are persisted to a VARCHAR column using the enum name(). So if you rename an enum value, you need to be aware that the DB will no longer match up!
 * <p/>
 * Pinched from http://www.hibernate.org/265.html
 */
public class EnumUserType<E extends Enum<E>> implements UserType
{
    private Class<E> clazz = null;

    protected EnumUserType(Class<E> c)
    {
        this.clazz = c;
    }

    private static final int[] SQL_TYPES = { Types.VARCHAR };

    @Override
    public int[] sqlTypes()
    {
        return SQL_TYPES;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class returnedClass()
    {
        return clazz;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner) throws HibernateException,
            SQLException
    {
        String name = resultSet.getString(names[0]);
        E result = null;
        if (!resultSet.wasNull())
        {
            result = Enum.valueOf(clazz, name);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor session) throws HibernateException,
            SQLException
    {
        if (null == value)
        {
            preparedStatement.setNull(index, Types.VARCHAR);
        }
        else
        {
            preparedStatement.setString(index, ((Enum) value).name());
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException
    {
        return value;
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
    public Object replace(Object original, Object target, Object owner) throws HibernateException
    {
        return original;
    }

    @Override
    public int hashCode(Object x) throws HibernateException
    {
        return x.hashCode();
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException
    {
        if (x == y)
        {
            return true;
        }
        if (null == x || null == y)
        {
            return false;
        }
        return x.equals(y);
    }
}
