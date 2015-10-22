package com.sabre.db;


public class PriceModifier
{
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    Integer id;

    public String getPercentageAmount()
    {
        return percentageAmount;
    }

    public void setPercentageAmount(String percentageAmount)
    {
        this.percentageAmount = percentageAmount;
    }

    String percentageAmount;


}
