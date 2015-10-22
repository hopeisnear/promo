/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;
import java.util.Date;

public class DatePair implements Serializable
{
    private Date start;
    private Date end;
    private boolean inside = true;

    protected DatePair()
    {
    }

    public DatePair(Date start, Date end)
    {
        this.start = start;
        this.end = end;
    }

    public boolean contains(Date date)
    {
        if (beforeOrSameAs(date) && afterOrSameAs(date))
        {
            return inside;
        }
        else
        {
            return !inside;
        }
    }

    public boolean afterOrSameAs(Date date)
    {
        return end.after(date) || end.getTime() == date.getTime();
    }

    public boolean beforeOrSameAs(Date date)
    {
        return start.before(date) || start.getTime() == date.getTime();
    }

    public Date getEnd()
    {
        return end;
    }

    public void setEnd(Date end)
    {
        this.end = end;
    }

    public Date getStart()
    {
        return start;
    }

    public void setStart(Date start)
    {
        this.start = start;
    }

    public boolean isInside()
    {
        return inside;
    }

    public void setInside(boolean inside)
    {
        this.inside = inside;
    }
}
