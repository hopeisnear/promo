package com.sabre.model;

public class PromoCode
{
    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    String code;

    public boolean isValid()
    {
        return isValid;
    }

    public void setIsValid(boolean isValid)
    {
        this.isValid = isValid;
    }

    boolean isValid;
}
