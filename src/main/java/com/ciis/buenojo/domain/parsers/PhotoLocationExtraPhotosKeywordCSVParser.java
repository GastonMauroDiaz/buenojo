package com.ciis.buenojo.domain.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.InputStreamSource;
import com.ciis.buenojo.domain.PhotoLocationKeyword;

public class PhotoLocationExtraPhotosKeywordCSVParser {
	private enum PhotoLocationExtraPhotosKeywordColumns{
		number,
		imageFileName,
		keywordString
	}	
	private InputStreamSource inputStreamSource;
	public PhotoLocationExtraPhotosKeywordCSVParser(InputStreamSource source) {
		this.inputStreamSource = source;
	}
	public Map<String,List<String>> parse() throws IOException{
		
		CSVParser parser = CSVFormat.RFC4180.withHeader().
												withDelimiter(',')
												.withAllowMissingColumnNames(true)
												.parse(new InputStreamReader(this.inputStreamSource.getInputStream()));
		Map<String,List<String>> map = new HashMap<>();
		
		for (CSVRecord csvRecord : parser) {
			String fileName = csvRecord.get(PhotoLocationExtraPhotosKeywordColumns.imageFileName.ordinal());
			
			String[] keywords = BuenOjoParserUtils.keywordStringListToArray(csvRecord.get(PhotoLocationExtraPhotosKeywordColumns.keywordString.ordinal()));
						
			map.put(FilenameUtils.removeExtension(fileName), Arrays.asList(keywords));
			
			
		}
		return map;
		
	}
	
	public static List<PhotoLocationKeyword> photoLocationKeywordList(String [] a) {
		
		ArrayList<PhotoLocationKeyword> plKeywords = new ArrayList<>(a.length);
		
		for (int i = 0; i < a.length; i++) {
			String s = a[i];
			PhotoLocationKeyword plk = new PhotoLocationKeyword();
			plk.setName(s);
			plk.setDescription(s);
			plKeywords.add(plk);
		}
		return plKeywords;
	}
	
}
