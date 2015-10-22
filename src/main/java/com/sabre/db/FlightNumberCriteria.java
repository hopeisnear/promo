/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents a passenger type criteria.
 */
public class FlightNumberCriteria implements Serializable
{
    private Long id;

    private boolean inclusive;
    private String airlineCode;
    private Integer flightNumberRangeStart;
    private Integer flightNumberRangeFinish;

    public FlightNumberCriteria()
    {
    }

    /**
     * eg SN3000-SN6000 - "SN flights in the range 3000-6000" !SN3000-SN6000 - "SN flights not in the range 3000-6000" SN3000 - "SN flight 3000"
     * 
     * @param flightNumberFormat
     */
    public FlightNumberCriteria(String flightNumberFormat)
    {
        if (!isValidFlightNumberFormat(flightNumberFormat))
        {
            throw new IllegalArgumentException("Invalid format " + flightNumberFormat);
        }

        flightNumberFormat = checkIsInclusive(flightNumberFormat);

        if (flightNumberFormat.contains("-"))
        {
            String[] formattedAirlineFlightNumbers = formatAirlineFlightNumbers(flightNumberFormat);

            if (formattedAirlineFlightNumbers.length != 2)
            {
                throw new IllegalStateException("invalid component length");
            }

            if (!(formattedAirlineFlightNumbers[0] == null || formattedAirlineFlightNumbers[0].length() < 3))
            {
                airlineCode = formattedAirlineFlightNumbers[0].substring(0, 2);
            }


            flightNumberRangeStart = getFlightNumber(formattedAirlineFlightNumbers[0]);
            flightNumberRangeFinish = getFlightNumber(formattedAirlineFlightNumbers[1]);
        }
        else
        {

            if (!(flightNumberFormat == null || flightNumberFormat.length() < 3))
            {
                airlineCode = flightNumberFormat.substring(0, 2);
            }

            flightNumberRangeStart = getFlightNumber(flightNumberFormat);
            flightNumberRangeFinish = getFlightNumber(flightNumberFormat);
        }
    }

    public static boolean isValidFlightNumberFormat(String flightNumberFormat)
    {
        if (StringUtils.isNotEmpty(flightNumberFormat))
        {
            if (flightNumberFormat.toCharArray()[0] == '!')
            {
                flightNumberFormat = flightNumberFormat.substring(1);
            }

            if (flightNumberFormat.contains("-"))
            {
                String[] formattedAirlineFlightNumbers = formatAirlineFlightNumbers(flightNumberFormat);

                if (formattedAirlineFlightNumbers.length != 2)
                {
                    return false;
                }

                if (!checkFirstTwoCharsAirlineCode(formattedAirlineFlightNumbers[0]))
                {
                    return false;
                }

                if (!checkFirstTwoCharsAirlineCode(formattedAirlineFlightNumbers[1]))
                {
                    return false;
                }

                try
                {
                    int numFirst = getFlightNumber(formattedAirlineFlightNumbers[0]);
                    int numSecond = getFlightNumber(formattedAirlineFlightNumbers[1]);

                    if (numSecond < numFirst)
                    {
                        return false;
                    }
                }
                catch (IllegalStateException e)
                {
                    return false;
                }
            }
            else
            {
                if (!checkFirstTwoCharsAirlineCode(flightNumberFormat))
                {
                    return false;
                }

                try
                {
                    getFlightNumber(flightNumberFormat);
                }
                catch (IllegalStateException e)
                {
                    return false;
                }
            }
        }

        return true;
    }

    private static String[] formatAirlineFlightNumbers(String flightNumberFormat)
    {
        int divider = flightNumberFormat.indexOf("-");

        return new String[] { flightNumberFormat.substring(0, divider).trim(), flightNumberFormat.substring(divider + 1, flightNumberFormat.length()).trim() };
    }

    /**
     * Reverse the formatted airline flight number method to extract the flight number.
     * 
     * @param formattedAirlineFlightNumber
     *            formatted airline and flight number
     * @return flight number
     */
    public static Integer getFlightNumber(String formattedAirlineFlightNumber)
    {
        if (StringUtils.isEmpty(formattedAirlineFlightNumber))
        {
            return null;
        }

        try
        {
            return Integer.valueOf(formattedAirlineFlightNumber.substring(2));
        }
        catch (NumberFormatException e)
        {
            throw new IllegalStateException("invalid formated flight number");
        }
    }

    protected static boolean checkFirstTwoCharsAirlineCode(String formattedAirlineFlightNumber)
    {
        if (formattedAirlineFlightNumber.length() > 2)
        {
            for (final char c : formattedAirlineFlightNumber.toCharArray())
            {
                if ((c >= 'a') && (c <= 'z'))
                {
                    continue;
                } // lowercase
                if ((c >= 'A') && (c <= 'Z'))
                {
                    continue;
                } // uppercase
                if ((c >= '0') && (c <= '9'))
                {
                    continue;
                } // numeric
                return false;
            }
            return true;
        }
        return false;
    }

    public String getAsFlightNumberFormat()
    {
        String flightNumberFormat = "";

        if (!inclusive)
        {
            flightNumberFormat = "!";
        }

        flightNumberFormat = flightNumberFormat + airlineCode;

        flightNumberFormat = flightNumberFormat + flightNumberRangeStart.toString();

        if (flightNumberRangeFinish != null && flightNumberRangeStart.intValue() != flightNumberRangeFinish.intValue())
        {
            flightNumberFormat = flightNumberFormat + "-" + airlineCode + flightNumberRangeFinish.toString();
        }

        return flightNumberFormat;
    }

    private String checkIsInclusive(String textComponent)
    {
        if (textComponent.startsWith("!"))
        {
            this.inclusive = false;
            textComponent = textComponent.substring(1);
        }
        else
        {
            this.inclusive = true;
        }
        return textComponent;
    }

    public FlightNumberCriteria(String airlineCode, Integer flightNumberRangeStart, Integer flightNumberRangeFinish, boolean inclusive)
    {
        this.airlineCode = airlineCode;
        this.flightNumberRangeFinish = flightNumberRangeFinish;
        this.flightNumberRangeStart = flightNumberRangeStart;
        this.inclusive = inclusive;
    }

    public boolean evaluate(Object formattedAirlineFlightNumberObj)
    {
        if (formattedAirlineFlightNumberObj == null)
        {
            return false;
        }

        String formattedAirlineFlightNumber = (String) formattedAirlineFlightNumberObj;
        if (airlineCode != null)
        {
            String testAirlineCode = null;
            if (!(formattedAirlineFlightNumber == null || formattedAirlineFlightNumber.length() < 3))
            {
                testAirlineCode = formattedAirlineFlightNumber.substring(0, 2);
            }
            if (!airlineCode.equals(testAirlineCode))
            {
                return false;
            }
        }

        int testFlightNum = getFlightNumber(formattedAirlineFlightNumber);
        if (testFlightNum >= flightNumberRangeStart && testFlightNum <= flightNumberRangeFinish)
        {
            return inclusive;
        }
        else
        {
            return !inclusive;
        }
    }

    /**
     * 
     * @return
     * 
     */
    public String getAirlineCode()
    {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode)
    {
        this.airlineCode = airlineCode;
    }

    /**
     * 
     * @return
     * 
     */
    public Integer getFlightNumberRangeFinish()
    {
        return flightNumberRangeFinish;
    }

    public void setFlightNumberRangeFinish(Integer flightNumberRangeFinish)
    {
        this.flightNumberRangeFinish = flightNumberRangeFinish;
    }

    /**
     * 
     * @return
     * 
     */
    public Integer getFlightNumberRangeStart()
    {
        return flightNumberRangeStart;
    }

    public void setFlightNumberRangeStart(Integer flightNumberRangeStart)
    {
        this.flightNumberRangeStart = flightNumberRangeStart;
    }

    /**
     * 
     * @return
     * 
     */
    public boolean isInclusive()
    {
        return inclusive;
    }

    public void setInclusive(boolean inclusive)
    {
        this.inclusive = inclusive;
    }



    public void setId(Long id)
    {
        this.id = id;
    }
}
