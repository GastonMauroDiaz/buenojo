package com.ciis.buenojo.customLoaders.helpers;

import java.util.Map;

import org.slf4j.Logger;

public class FieldValidationHelper {
public static final String getSubString(final String str, final Integer maxLength)
{
	final String output;
	output= str.substring(0, Math.min(str.length(), maxLength));
	return output.trim();
}

public static final Boolean isLengthUpperLimitValid(final String str, final Integer maxLength)
{
	final Boolean output;
	output= (str.trim().length()<=maxLength);
	return output;
	

}

public static final Boolean isLengthLowerLimitValid(final String str, final Integer minLength)
{
	final Boolean output;
	output= (str.trim().length()>=minLength);
	return output;
}


public static void testValidationUpperLimit(String element, String fieldName, Integer maxLength, Logger log)
{
	if (!FieldValidationHelper.isLengthUpperLimitValid(element, 100))
	{
		log.warn(String.format("%s excedida %d/100", fieldName, element.trim().length()));
		log.warn(String.format("%s completa: %s", fieldName, element));
		log.warn(String.format("%s acotada: %s", fieldName, FieldValidationHelper.getSubString(element, 100)));
	}

}

public static void showField(String value, String fieldName, Logger log)
{
	log.warn(String.format("%s: [%s]", fieldName, value));
}

public static void showAvailableFields(Map<String, String> element, Logger log)
{
	for (String key : element.keySet())
		log.info(key);
	for (String values : element.values())
		log.info(values);
}



}
