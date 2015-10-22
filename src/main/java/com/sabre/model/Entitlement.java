package com.sabre.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Entitlement implements Serializable
{
    private String flightNumber;
    private Date validFrom;
    private Date validTo;
    private String destination;
    private String origin;
    private int usedCount;
    private int quantity;

    public String getDiscount()
    {
        return discount;
    }

    public void setDiscount(String discount)
    {
        this.discount = discount;
    }

    private String discount;

    public Long getOfferingId()
    {
        return offeringId;
    }

    public void setOfferingId(Long offeringId)
    {
        this.offeringId = offeringId;
    }

    private Long offeringId;


    public int getUsedCount()
    {
        return usedCount;
    }

    public void setUsedCount(int usedCount)
    {
        this.usedCount = usedCount;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public String getFlightNumber()
    {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber)
    {
        this.flightNumber = flightNumber;
    }

    public Date getValidFrom()
    {
        return validFrom;
    }

    public void setValidFrom(Date validFrom)
    {
        this.validFrom = validFrom;
    }

    public Date getValidTo()
    {
        return validTo;
    }

    public void setValidTo(Date validTo)
    {
        this.validTo = validTo;
    }
}
