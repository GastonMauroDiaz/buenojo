package com.ciis.buenojo.customLoaders.helpers;

import org.apache.commons.csv.CSVFormat;

public class CSVFormatHelper {
	public static CSVFormat getDefaultCSVFormat() {
		final CSVFormat output;
		output = CSVFormat.RFC4180.withHeader().withDelimiter(',').withEscape('\\').withAllowMissingColumnNames(true);
		return output;
	}
}
