package com.ciis.buenojo.domain.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.InputStreamSource;

import com.ciis.buenojo.domain.PhotoLocationKeyword;

public class PhotoLocationKeywordsParser {

	private enum PhotoLocationKeywordColumns{
		name,
		description

	}	
	private InputStreamSource inputStreamSource;
	public PhotoLocationKeywordsParser(InputStreamSource source) {
		this.inputStreamSource = source;
	}
	
	public List<PhotoLocationKeyword> parse() throws IOException{
		CSVParser parser = CSVFormat.RFC4180.withHeader().
				withDelimiter(',')
				.withAllowMissingColumnNames(true)
				.parse(new InputStreamReader(this.inputStreamSource.getInputStream()));
		
		List<PhotoLocationKeyword> keywords = new ArrayList<>();		
		parser.forEach((record) -> {
			keywords.add(parseRecord(record));
		});		
		return keywords;
	}
	
	private PhotoLocationKeyword parseRecord(CSVRecord record){
		PhotoLocationKeyword keyword = new PhotoLocationKeyword();
		String description = record.get(PhotoLocationKeywordColumns.description.ordinal());
		keyword.setDescription(description);
		String name = record.get(PhotoLocationKeywordColumns.name.ordinal());
		keyword.setName(name);
		return keyword;
		
	}
}
