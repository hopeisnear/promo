package com.sabre.db;

public class PromoCodes
{
    int id;
    String offeringName;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getOfferingName()
    {
        return offeringName;
    }

    public void setOfferingName(String offeringName)
    {
        this.offeringName = offeringName;
    }

    public String getPromoCode()
    {
        return promoCode;
    }

    public void setPromoCode(String promoCode)
    {
        this.promoCode = promoCode;
    }

    String promoCode;



}
