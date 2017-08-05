package com.ciis.buenojo.domain.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.InputStreamSource;

import com.ciis.buenojo.domain.PhotoLocationSightPair;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;

public class PhotoLocationSightPairCSVParser {

	private enum PhotoLocationSightPairCSVColumn {
		number, 
		id, 
		satCol,
		satRow,
		satTolerancia,
		terCol,
		terRow, 
		terTolerancia
	}
	private InputStreamSource inputStreamSource;
	
	public PhotoLocationSightPairCSVParser(InputStreamSource inputStreamSource) {
		super();
		this.inputStreamSource = inputStreamSource;
	}

	public List<PhotoLocationSightPair> parse () throws IOException, BuenOjoCSVParserException {
		CSVParser parser =  CSVFormat.RFC4180.withHeader().withDelimiter(',').withAllowMissingColumnNames(true).parse(new InputStreamReader(this.inputStreamSource.getInputStream()));
		
		
		List<CSVRecord> records = parser.getRecords();
		if (records.size() == 0 ) {
			throw new BuenOjoCSVParserException("El archivos de miras no contiene registros");
		}
		ArrayList<PhotoLocationSightPair> sightPairs = new ArrayList<>(records.size());
		for (CSVRecord record : records) {
			
			PhotoLocationSightPair sight = new PhotoLocationSightPair();
			sight.setNumber(new Integer(record.get(PhotoLocationSightPairCSVColumn.id)));
			sight.setSatelliteX(new Integer(record.get(PhotoLocationSightPairCSVColumn.satCol)));
			sight.setSatelliteY(new Integer(record.get(PhotoLocationSightPairCSVColumn.satRow)));
			sight.setSatelliteTolerance(new Integer(record.get(PhotoLocationSightPairCSVColumn.satTolerancia)));
			sight.setTerrainX(new Integer(record.get(PhotoLocationSightPairCSVColumn.terCol)));
			sight.setTerrainY(new Integer(record.get(PhotoLocationSightPairCSVColumn.terRow)));
			sight.setTerrainTolerance(new Integer(record.get(PhotoLocationSightPairCSVColumn.terTolerancia)));
			
			sightPairs.add(sight);
			
		}
		return sightPairs;
	}
	
}
