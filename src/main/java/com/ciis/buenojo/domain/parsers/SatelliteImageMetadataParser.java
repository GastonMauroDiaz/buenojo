package com.ciis.buenojo.domain.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.springframework.web.multipart.MultipartFile;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;

public class SatelliteImageMetadataParser {
	private MultipartFile inputStreamSource;
	
	public SatelliteImageMetadataParser(MultipartFile source) {
		this.inputStreamSource = source;
		
	}
	
	
	public List<Map<String,String>> parse() throws BuenOjoCSVParserException {
		List<Map<String,String>> list = new ArrayList<>();
		CSVParser parser = null;
		try {
			parser = CSVFormat.RFC4180.withHeader()
									.withDelimiter(',')
									.withAllowMissingColumnNames(true)
									.parse(new InputStreamReader(this.inputStreamSource.getInputStream()));
		} catch (IOException e) {
			throw new BuenOjoCSVParserException(e.getMessage());
		}
		
		for (CSVRecord record :parser) {
			Map<String,String> map = record.toMap();
			list.add(map);
		}
		
		return list;
		
	}
	
	
	
	
}
