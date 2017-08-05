package com.ciis.buenojo.domain.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.InputStreamSource;

import com.ciis.buenojo.domain.PhotoLocationKeyword;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;

public class PhotoLocationLandscapeLevelsCSVParser {

	private enum PhotoLocationLandscapeLevelsCSVColumns {
		number,
		levels,
		landscape
	}
	private enum PhotoLocationLevelComponents  {
		lowerLevel,
		higherLevel,
		count
		
	}
	private InputStreamSource inputStreamSource;

	public PhotoLocationLandscapeLevelsCSVParser(InputStreamSource inputStreamSource) {
		super();
		this.inputStreamSource = inputStreamSource;
	}
	
	public List<String> parseKeywords() throws IOException, BuenOjoCSVParserException {
		
		CSVRecord record = getRecords().get(0);
		
		String [] keywords = BuenOjoParserUtils.keywordStringListToArray(record.get(PhotoLocationLandscapeLevelsCSVColumns.landscape.ordinal()));
		
		return Arrays.asList(keywords);
	}
	
	public Integer[] parseLevels() throws BuenOjoCSVParserException, IOException{
		CSVRecord record = getRecords().get(0);
		String levels = record.get(PhotoLocationLandscapeLevelsCSVColumns.levels.ordinal());
		
		//Matcher m = Pattern.compile("(\\d*):(\\d*)").matcher(levels);
		String[] m = levels.split(":");
		if (m==null) {
			throw new BuenOjoCSVParserException("el formato del nivel es incorrecto: "+levels);
		}
		
		Integer[] n = new Integer[PhotoLocationLevelComponents.count.ordinal()];
		int lower = PhotoLocationLevelComponents.lowerLevel.ordinal();
		int higher = PhotoLocationLevelComponents.higherLevel.ordinal();
		n[lower] = new Integer(m[lower]);
		n[higher] = new Integer(m[higher]);
	
		return n;
		
	}
	
	private List<CSVRecord> getRecords () throws BuenOjoCSVParserException, IOException{
		CSVParser parser =  CSVFormat.RFC4180.withHeader().withDelimiter(',').withAllowMissingColumnNames(true).parse(new InputStreamReader(this.inputStreamSource.getInputStream()));
		
		List<CSVRecord> records = parser.getRecords();
		if (records.size()==0) {
			throw new BuenOjoCSVParserException("El archivo no contiene palabras clave");
		}
		if (records.size()>1) {
			throw new BuenOjoCSVParserException("El archivo contiene mas de un registro de palabras clave");
		}	
		return records;
	}
}
