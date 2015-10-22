/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

public class FieldStateUserType extends EnumUserType<FieldCriteria.FieldState>
{
    public FieldStateUserType()
    {
        super(FieldCriteria.FieldState.class);
    }
}
