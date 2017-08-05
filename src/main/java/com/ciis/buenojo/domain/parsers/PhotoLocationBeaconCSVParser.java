package com.ciis.buenojo.domain.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.InputStreamSource;

import com.ciis.buenojo.domain.PhotoLocationBeacon;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;

public class PhotoLocationBeaconCSVParser {

	private enum PhotoLocationBeaconCSVColumns {
		number, 
		col,
		row,
		tolerance
		
	}
	private InputStreamSource inputStreamSource ;
	public PhotoLocationBeaconCSVParser (InputStreamSource source) {
		this.inputStreamSource = source;
		
	}
	
	
	public PhotoLocationBeacon parse() throws IOException, BuenOjoCSVParserException {
		
		CSVParser parser =  CSVFormat.RFC4180.withHeader().withDelimiter(',').withAllowMissingColumnNames(true).parse(new InputStreamReader(this.inputStreamSource.getInputStream()));
		List<CSVRecord> records = parser.getRecords();
		if (records.size() > 1) {
			throw new BuenOjoCSVParserException("El archivo contiene más de un indicador");
		}
		if (records.size() == 0) {
			throw new BuenOjoCSVParserException("El archivo de indicador es inválido");
		}
		
		CSVRecord record = records.get(0);
		PhotoLocationBeacon beacon = new PhotoLocationBeacon();
		beacon.setX(new Integer(record.get(PhotoLocationBeaconCSVColumns.col.ordinal())));
		beacon.setY(new Integer(record.get(PhotoLocationBeaconCSVColumns.row.ordinal())));
		beacon.setTolerance(new Integer(record.get(PhotoLocationBeaconCSVColumns.tolerance.ordinal())));
		
		return beacon;
	}
}
