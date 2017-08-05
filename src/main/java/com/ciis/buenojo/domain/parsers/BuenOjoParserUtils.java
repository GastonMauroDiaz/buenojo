package com.ciis.buenojo.domain.parsers;



public class BuenOjoParserUtils {
	
	public static String[] keywordStringListToArray (String listString){
		
		
		String[] rawList = listString.split(",");
		String[] list = new String[rawList.length];
		for (int i = 0; i < rawList.length; i++) {
			String rawKeyword = rawList[i];
			list[i] = rawKeyword.trim();

		}
		return list;
	}
}
