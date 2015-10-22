/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import java.io.Serializable;
import java.util.Map;

public class UserCriteria implements Criteria<Object>, Serializable
{
    private FieldCriteriaString username = new FieldCriteriaString();
    private FieldCriteriaString userFirstName = new FieldCriteriaString();
    private FieldCriteriaString userLastName = new FieldCriteriaString();
    private FieldCriteriaString userPermissionGroup = new FieldCriteriaString();
    private FieldCriteriaString userPermission = new FieldCriteriaString();

    private FieldCriteriaString accountName = new FieldCriteriaString();
    private FieldCriteriaString accountId = new FieldCriteriaString();
    /**
     * Account primary address country
     */
    private FieldCriteriaString accountAddressLocation = new FieldCriteriaString();
    private FieldCriteriaString accountTypes = new FieldCriteriaString();


    public boolean anyFieldsSet()
    {
        return (username != null && username.getFieldsCount() > 0) ||
                (userFirstName != null && userFirstName.getFieldsCount() > 0) ||
                (userLastName != null && userLastName.getFieldsCount() > 0) ||
                (userPermissionGroup != null && userPermissionGroup.getFieldsCount() > 0) ||
                (userPermission != null && userPermission.getFieldsCount() > 0) ||
                (accountAddressLocation != null && accountAddressLocation.getFieldsCount() > 0) ||
                (accountId != null && accountId.getFieldsCount() > 0) ||
                (accountName != null && accountName.getFieldsCount() > 0) ||
                (accountTypes != null && accountTypes.getFieldsCount() > 0);
    }

    @Override public boolean evaluate(Object o)
    {
        return false;
    }

    @Override
    public boolean evaluate(Object o, Map<String, Object> args)
    {
        return evaluate(o);
    }

    /**
     * @return field
     */
    public FieldCriteriaString getAccountAddressLocation()
    {
        return accountAddressLocation;
    }

    public void setAccountAddressLocation(FieldCriteriaString accountAddressLocation)
    {
        this.accountAddressLocation = accountAddressLocation;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getAccountId()
    {
        return accountId;
    }

    public void setAccountId(FieldCriteriaString accountId)
    {
        this.accountId = accountId;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getAccountName()
    {
        return accountName;
    }

    public void setAccountName(FieldCriteriaString accountName)
    {
        this.accountName = accountName;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getAccountTypes()
    {
        return accountTypes;
    }

    public void setAccountTypes(FieldCriteriaString accountTypes)
    {
        this.accountTypes = accountTypes;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getUserFirstName()
    {
        return userFirstName;
    }

    public void setUserFirstName(FieldCriteriaString userFirstName)
    {
        this.userFirstName = userFirstName;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getUserLastName()
    {
        return userLastName;
    }

    public void setUserLastName(FieldCriteriaString userLastName)
    {
        this.userLastName = userLastName;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getUsername()
    {
        return username;
    }

    public void setUsername(FieldCriteriaString username)
    {
        this.username = username;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getUserPermissionGroup()
    {
        return userPermissionGroup;
    }

    public void setUserPermissionGroup(FieldCriteriaString userPermissionGroup)
    {
        this.userPermissionGroup = userPermissionGroup;
    }

    /**
     * @return field
     */
    public FieldCriteriaString getUserPermission()
    {
        return userPermission;
    }

    public void setUserPermission(FieldCriteriaString userPermission)
    {
        this.userPermission = userPermission;
    }
}
