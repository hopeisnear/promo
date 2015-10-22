/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;

/**
 * An entitlement offering is a particular "thing" that is made available to a user or account as part of an Entitlement. This "thing" could be things such as a fee, discount, access to fares, a frequent flyer bonus,
 * etc.
 */
public class Offering implements Serializable
{
    /**
     * The database identifier.
     */
    private Long id;

    public String getName()
    {
        return name;
    }

    /**
     * A user defined name to identify this offering.
     */
    private String name;

    /**
     * A way to rank the Offerings between one another
     */
    private int ranking;

    /**
     * Optional attribute to determine context of offering
     */
    private String context;

    private Collection<String> changeHistory = new ArrayList<String>();

    /**
     * These types of offerings are optionally chosen by the user.
     */
    public static final String CONTEXT_UNBUNDLED = "UNBUNDLED";
    /**
     * These types of offerings are standard automatically given to the user.
     */
    public static final String CONTEXT_DIFFERENTIATING = "DIFF";

    /**
     * Date where this offering was created.
     */
    private Calendar createDate;

    /**
     * dates where this offering is applicle
     * 
     */
    private Set<DateRangeRestriction> applicableDates;

    /**
     * If locked an Offering can not be edited
     */
    private boolean locked = false;

    /**
     * Date where this offering was last changed.
     */
    private Calendar lastChangeDate;

    /** Option coupon attributes of the offering */
    private OfferingCoupon offeringCoupon = null;

    /**
     * The newer style flatted out dedicated SB criteria
     */
    protected ShoppingBasketCriteria shoppingBasketCriteria2;

    public Offering()
    {
        super();
    }

    public Offering(String name)
    {
        super();
        setName(name);// use setter logic
    }

    /**
     * The database identifier.
     * 
     * @return id
     */
    public Long getId()
    {
        return id;
    }

    @SuppressWarnings({ "unused", "UnusedDeclaration" })
    private void setId(Long id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = (name != null) ? name.trim() : null;
    }

    /**
     * @return creation date
     */
    public Calendar getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Calendar createDate)
    {
        this.createDate = createDate;
    }

    /**
     * @return last change date
     */
    public Calendar getLastChangeDate()
    {
        return lastChangeDate;
    }

    public void setLastChangeDate(Calendar lastChangeDate)
    {
        this.lastChangeDate = lastChangeDate;
    }

    public void setRanking(int ranking)
    {
        this.ranking = ranking;
    }

    /**
     * @return applicable dates inverse="false" outer-join="false" cascade="save-update" batch-size="50" class="com.eb2.qtrip.model.util.DateRangeRestriction"
     */
    public Set<DateRangeRestriction> getApplicableDates()
    {
        return applicableDates;
    }

    public void setApplicableDates(Set<DateRangeRestriction> applicableDates)
    {
        this.applicableDates = applicableDates;
    }

    public boolean isApplicableNow()
    {
        if (applicableDates != null && !applicableDates.isEmpty())
        {
            Calendar now = Calendar.getInstance();
            for (DateRangeRestriction restriction : applicableDates)
            {
                if (!restriction.isValid(now))
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return true if locked
     */
    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }


    public void setContext(String context)
    {
        this.context = context;
    }

    /**
     * @return coupon class="com.eb2.qtrip.model.entitlement.OfferingCoupon" cascade="all"
     */
    public OfferingCoupon getOfferingCoupon()
    {
        return offeringCoupon;
    }

    public void setOfferingCoupon(OfferingCoupon offeringCoupon)
    {
        this.offeringCoupon = offeringCoupon;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((context == null) ? 0 : context.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Offering other = (Offering) obj;
        if (context == null)
        {
            if (other.context != null)
            {
                return false;
            }
        }
        else if (!context.equals(other.context))
        {
            return false;
        }
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns the flattened SB criteria
     * 
     * @return class="com.eb2.qtrip.model.criteria.ShoppingBasketCriteria" cascade="all"
     */
    public ShoppingBasketCriteria getShoppingBasketCriteria2()
    {
        return shoppingBasketCriteria2;
    }

    public void setShoppingBasketCriteria2(ShoppingBasketCriteria shoppingBasketCriteria2)
    {
        this.shoppingBasketCriteria2 = shoppingBasketCriteria2;
    }

    /**
     * type="com.eb2.qtrip.hibernate.type.ChangeHistoryCollectionUserType" not-null="false"
     */
    public Collection<String> getChangeHistory()
    {
        return changeHistory;
    }

    public void setChangeHistory(Collection<String> changeHistory)
    {
        this.changeHistory = changeHistory;
    }
}
