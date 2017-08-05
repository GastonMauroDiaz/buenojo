package com.ciis.buenojo.customLoaders.helpers;

import java.text.StringCharacterIterator;
import java.util.StringJoiner;

public class GlobFactoryHelper {
	private final static String globHeader= "glob:**.";
	
	private final static String getCaseInsensitiveExtension(final String extension)
	{
		final StringBuilder sbGlobExtensionCaseInsensitive;
		final StringCharacterIterator extensionIterator;
		sbGlobExtensionCaseInsensitive= new StringBuilder();
		extensionIterator= new StringCharacterIterator(extension);
		for (char c= extensionIterator.first(); c!=StringCharacterIterator.DONE; c= extensionIterator.next())
		{
			sbGlobExtensionCaseInsensitive.append(String.format("[%s,%s]", Character.toLowerCase(c), Character.toUpperCase(c)));
		}
		return sbGlobExtensionCaseInsensitive.toString();
	}

	public final static String getCaseInsensitiveExtensionGlob(final String... extensions)
	{
		final StringJoiner sj;
		sj = new StringJoiner(",", globHeader.concat( "{"), "}");
		for(String extension : extensions)
			sj.add(getCaseInsensitiveExtension(extension));
		return sj.toString();
	}

}
