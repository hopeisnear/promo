/* Copyright 2012 Sabre Holdings */
package com.sabre.db;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

public final class StringUtil
{
    private StringUtil()
    {
        //
    }

    /**
     * flattens a collection of strings into HTML lines (separated by <br>
     */
    public static String flattenForHtml(final Collection<String> strings)
    {
        StringBuilder sb = new StringBuilder();
        for (String string : strings)
        {
            sb.append(escape(string)).append("<br/>");
        }
        return sb.toString();
    }

    public static boolean matchesWildcard(String string, String mask)
    {
        return matchesWildcard(string, mask, false);
    }

    /**
     * Wild-cards can be used to match characters. The system is *not* case sensitive.
     * <p/>
     * The following characters are used: <br/>
     * ? - Matches any single character.<br/>
     * - Matches any number of any characters, until the next character after the * is found or the string ends.<br/>
     * [afe] - Matches any single character from the characters given.<br/>
     * [a-z] - Matches any single character inside the range of characters given.
     * <p/>
     * Examples: <br/>
     * - Matches any string. ??? - Matches any string with 3 characters. [abc]![a-z]!* - Matches "a!f!blah", but not "g!6!abc".
     * <p/>
     * See the test cases in CriteriaBuilderTest for more examples.
     * 
     * @param string
     *            the string to match
     * @param mask
     *            the mask to match against
     */
    public static boolean matchesWildcard(String string, String mask, boolean matchCase)
    {
        boolean result = true;

        int maskLocation = 0, minimumLength = 0, stringLocation = 0;

        if (string != null & mask != null)
        {
            if (!matchCase)
            {
                mask = mask.toLowerCase();
                string = string.toLowerCase();
            }

            for (stringLocation = 0; stringLocation < string.length(); stringLocation++)
            {
                // If we reach the end of the mask first, string does not match
                if (maskLocation >= mask.length())
                {
                    result = false;
                    break;
                }

                char currentMaskChar = mask.charAt(maskLocation);
                char currentStringChar = string.charAt(stringLocation);

                switch (currentMaskChar)
                {
                    case '?': // ? characters can be any single character
                        maskLocation++;
                        minimumLength++;
                        break;

                    case '*': // * characters can be any number of any type of characters
                        if (maskLocation >= mask.length() - 1)
                        {
                            result = true;

                            stringLocation = string.length();
                            maskLocation = mask.length();

                            return true;
                        }
                        else if (maskLocation < mask.length() - 1)
                        {
                            for (int j = stringLocation; j < string.length(); j++)
                            {
                                if (matchesWildcard(string.substring(j), mask.substring(maskLocation + 1)))
                                {
                                    return true;
                                }
                            }

                            return false;
                        }

                        break;

                    case '[': // [ characters mark the start of a range of chars
                        minimumLength++;

                        int currentMaskLocationTemp = maskLocation + 1;
                        boolean matchFound = false;
                        boolean cont = true;

                        while (cont)
                        {
                            char currentMaskCharTemp = mask.charAt(currentMaskLocationTemp);

                            switch (currentMaskCharTemp)
                            {
                                case ']': // ] characters mark the end of the range
                                    if (!matchFound)
                                    {
                                        result = false;
                                        stringLocation = string.length();
                                    }

                                    cont = false;

                                    break;

                                case '-':
                                    char startChar = mask.charAt(currentMaskLocationTemp - 1);
                                    char endChar = mask.charAt(currentMaskLocationTemp + 1);

                                    for (int i = startChar; i < endChar; i++)
                                    {
                                        char c = (char) i;

                                        if (c == currentStringChar)
                                        {
                                            matchFound = true;
                                        }
                                    }

                                    break;

                                default:
                                    if (currentMaskCharTemp == currentStringChar)
                                    {
                                        matchFound = true;
                                        result = true;
                                    }
                                    break;
                            }

                            currentMaskLocationTemp++;

                            // if we reach end of mask early, there is a problem and strings do not match
                            if (currentMaskLocationTemp >= mask.length())
                            {
                                cont = false;
                            }
                        }

                        maskLocation += currentMaskLocationTemp - maskLocation;

                        break;

                    default:
                        minimumLength++;

                        // All other characters must be the same, or Strings to
                        // not match
                        if (currentStringChar != currentMaskChar)
                        {
                            result = false;

                            stringLocation = string.length();
                            break;
                        }

                        maskLocation++;
                }
            }

            // if we didn't make it to the end of either string, an error occurred
            if (stringLocation < string.length() || maskLocation < mask.length())
            {
                // the only exception is if the last mask char is a *
                if (mask.charAt(mask.length() - 1) != '*')
                {
                    result = false;
                }
            }
        }

        return result;
    }

    public static boolean containsWildcard(String mask)
    {
        return mask.contains("*") || mask.contains("?");
    }

    public static String escape(String text)
    {
        return text.replace("\n", "\\n").replace("\r", "").replace("'", "\\'");
    }

    public static String nullifyIfBlankString(String input)
    {
        if (StringUtils.isNotBlank(input))
        {
            return input;
        }
        else
        {
            return null;
        }
    }

    public static String getCauseStackTrace(Throwable e)
    {
        StringWriter writer = new StringWriter();
        Throwable exception = e;
        while (exception.getCause() != null)
        {
            exception = exception.getCause();
        }
        exception.printStackTrace(new PrintWriter(writer));

        return writer.toString();
    }

    public static String stripWhiteSpaceCharacters(String input)
    {
        if(StringUtils.isNotBlank(input))
        {
            return input.replaceAll("\\s","");
        }

        return input;
    }
}
