/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;
import java.util.Date;

/**
 * A single usage of a coupon is captured as a redemption.
 *
 *
 */
public class Redemption implements Serializable, CodeType
{
    /**
     * The database identifier.
     */
    private Long id;

    /**
     * Code required to be entered by the user to gain access to this redemption.
     */
    private String code = "";

    /** Booking reference associated with this redemption */
    private String pnr = null;

    /** If unused this will be null */
    private Date used = null;

    /** date created for admin purposed */
    private Date created;

    /** Offering name used to set the redemption in the correct offering */
    private Offering offering;

    protected Redemption()
    {
    }

    public Redemption(String code, Offering offering)
    {
        this.code = code;
        this.offering = offering;
        this.created = new Date();
    }

    // Used only for display purposes
    public Redemption(String code)
    {
        this.code = code;
    }

    /**
     * Is this redemption unused and available for the coupon code
     *
     * @param code
     * @return
     */
    public boolean isAvailable(String code)
    {
        return !isRedeemed() && this.code.equals(code);
    }

    /**
     * Has this redemption already been used
     *
     * @return
     */
    public boolean isRedeemed()
    {
        return used != null;
    }

    /**
     * @return pnr
     */
    public String getPnr()
    {
        return pnr;
    }

    public void setPnr(String pnr)
    {
        this.pnr = pnr;
    }

    /**
     * @return used
     */
    public Date getUsed()
    {
        return used;
    }

    public void setUsed(Date used)
    {
        this.used = used;
    }

    /**
     * @return code
     */
    @Override
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public Long getId()
    {
        return id;
    }

    protected void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return created
     */
    public Date getCreated()
    {
        return created;
    }

    public void setCreated(Date created)
    {
        this.created = created;
    }

    /**
     * @return offering
     */
    public Offering getOffering()
    {
        return offering;
    }

    public void setOffering(Offering offering)
    {
        this.offering = offering;
    }
}
