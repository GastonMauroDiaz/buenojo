package com.ciis.buenojo.customLoaders.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParserHelper {
	
	private static Logger log = LoggerFactory.getLogger(ParserHelper.class);
	
	public static ArrayList<Map<String,String>> parse(boolean isFromGameResourceInput, String... fileResourcePathComponents)  {

		final ArrayList<Map<String,String>> list;
		final CSVParser parser;
		final URL resource;
		final File file;
		final CSVFormat csvFormat;
		final Charset charset;
		list = new ArrayList<Map<String,String>> ();
		try {
			resource =	ResourceHelper.getResource(isFromGameResourceInput, fileResourcePathComponents);
			charset=FileEncodingDetectorHelper.getCharsetFromURL(resource);
			csvFormat = CSVFormatHelper.getDefaultCSVFormat();
			parser = CSVParser.parse(resource, charset, csvFormat);
			for (CSVRecord record : parser )
				list.add(record.toMap());
			
			
		} catch (FileNotFoundException e) {
			log.error(String.format("File not found %s", ResourceHelper.getResourceAddress(fileResourcePathComponents)), e);
		}
		catch (IOException e) {
			log.error(String.format("IOError %s", ResourceHelper.getResourceAddress(fileResourcePathComponents)), e);
		}
		
		
		return list;
		
	}

}
