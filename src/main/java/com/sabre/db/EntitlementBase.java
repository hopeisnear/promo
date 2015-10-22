/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public abstract class EntitlementBase implements Serializable
{
    private Long id;

    /**
     * User defined name for this entitlement.
     */
    private String name;

    /**
     * Ranking used to order application of entitlements
     */
    private int ranking;

    /**
     * Context
     */
    private String context;

    public static final String CONTEXT_BOOKING_FEE = "booking_fees";
    public static final String CONTEXT_TICKET_COLLECTION = "ticket_collection";
    public static final String CONTEXT_PROMOTION = "promotion";
    public static final String CONTEXT_PRODUCT = "product";

    public static final String[] CONTEXTS = { CONTEXT_BOOKING_FEE, CONTEXT_TICKET_COLLECTION, CONTEXT_PROMOTION };

    /**
     * Defines a criteria which identifies which user(s) or which account(s) receive this entitlement. We can use the same criteria object to identify either, as the criteria object is not typed.
     */
    private Criteria userCriteria;

    /**
     * Fare criteria that defines when this entitlement is valid
     */
    private ShoppingBasketCriteria fareCriteria;

    /**
     * Shopping basket criteria that defines when this entitlement is valid
     */
    private ShoppingBasketCriteria shoppingBasketCriteria;

    /**
     * Defines what dates the entitlement should be applied on
     */
    private Set<DateRangeRestriction> applicableDates = new HashSet<DateRangeRestriction>();

    private Collection<String> changeHistory = new ArrayList<String>();

    /**
     * The offerings that are made available as part of this entitlement.
     */
    private Set<Offering> offerings = new HashSet<Offering>();

    private Calendar sellFromDate;

    private Calendar sellUntilDate;

    /**
     * If locked an entitlement can not be edited
     */
    private boolean locked = false;

    public Long getId()
    {
        return id;
    }

    protected void setId(Long id)
    {
        this.id = id;
    }


    public void setName(String name)
    {
        this.name = (name != null) ? name.trim() : null;
    }

    /**
     * cascade="save-update" outer-join="false" class="com.eb2.qtrip.model.entitlement.Offering" outer-join="false" column="offeringId"
     */
    public Set<Offering> getOfferings()
    {
        return offerings;
    }

    public Set<Offering> getOfferings(String context)
    {
        Set<Offering> contextOfferings = new HashSet<Offering>();

//        for (Offering offering : offerings)
//        {
//            if (offering.getContext() != null && offering.getContext().equals(context))
//            {
//                contextOfferings.add(offering);
//            }
//        }

        return contextOfferings;
    }

    protected AtomicReference<List<Offering>> sortedOfferings = new AtomicReference<List<Offering>>();


    public void setOfferings(Set<Offering> offerings)
    {
        this.offerings = offerings;
        sortedOfferings.set(null);
    }

    public void addOffering(Offering offering)
    {
        this.offerings.add(offering);
        sortedOfferings.set(null);
    }

    /**
     * class="com.eb2.qtrip.model.criteria.CriteriaBase" cascade="save-update"
     */
    public Criteria getUserCriteria()
    {
        return userCriteria;
    }

    public void setUserCriteria(Criteria userCriteria)
    {
        this.userCriteria = userCriteria;
    }

    /**
     * inverse="false" cascade="save-update" outer-join="false" batch-size="50" class="com.eb2.qtrip.model.util.DateRangeRestriction"
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
        if (CollectionUtils.isEmpty(applicableDates))
        {
            return true;
        }
        Calendar now = Calendar.getInstance();
        boolean isIncluded = false;
        boolean isExcluded = false;
        for (DateRangeRestriction restriction : applicableDates)
        {
            if (!restriction.isAllowedRange() && !restriction.isValid(now))
            {
                isExcluded = true;
            }
            if (restriction.isAllowedRange() && restriction.isValid(now))
            {
                isIncluded = true;
            }
        }
        return !isExcluded && isIncluded;
    }

    public Calendar getSellFromDate()
    {
        return sellFromDate;
    }

    public void setSellFromDate(Calendar sellFromDate)
    {
        this.sellFromDate = sellFromDate;
    }

    public Calendar getSellUntilDate()
    {
        return sellUntilDate;
    }

    public void setSellUntilDate(Calendar sellUntilDate)
    {
        this.sellUntilDate = sellUntilDate;
    }


    public void setContext(String context)
    {
        this.context = context;
    }



    public void setRanking(int ranking)
    {
        this.ranking = ranking;
    }





    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

    @Override
    public String toString()
    {
        return name;
    }

    /**
     * Returns the validity critiera
     * 
     * @return class="com.eb2.qtrip.model.criteria.ShoppingBasketCriteria" cascade="all"
     */
    public ShoppingBasketCriteria getShoppingBasketCriteria()
    {
        return shoppingBasketCriteria;
    }

    public void setShoppingBasketCriteria(ShoppingBasketCriteria shoppingBasketCriteria)
    {
        this.shoppingBasketCriteria = shoppingBasketCriteria;
    }

    /**
     * Returns the fare criteria
     * 
     * @return class="com.eb2.qtrip.model.criteria.ShoppingBasketCriteria" cascade="all"
     */
    public ShoppingBasketCriteria getFareCriteria()
    {
        return fareCriteria;
    }

    public void setFareCriteria(ShoppingBasketCriteria fareCriteria)
    {
        this.fareCriteria = fareCriteria;
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
