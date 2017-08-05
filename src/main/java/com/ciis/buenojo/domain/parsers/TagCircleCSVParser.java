package com.ciis.buenojo.domain.parsers;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.ciis.buenojo.domain.TagCircle;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;



public class TagCircleCSVParser {
	private final static Integer MAX_CIRCLES = 9;
	private InputStream inputStream;
	public TagCircleCSVParser (InputStream in) {
		if (in == null) throw new IllegalArgumentException("[TagCircleCSVParser]InputStream can't be null");
		this.inputStream = in;


	}

	public List<TagCircle> parse() throws IOException, BuenOjoCSVParserException {

		ArrayList<TagCircle> list = new ArrayList<>(MAX_CIRCLES);

		CSVParser parser = CSVFormat.RFC4180.withHeader().withDelimiter(',').withAllowMissingColumnNames(false).parse(new InputStreamReader(this.inputStream));
		
		for (CSVRecord record : parser ){
			TagCircle circle = new TagCircle();
			circle.setNumber(new Integer(record.get("id")));
			circle.setX(new Integer(record.get("col")));
			circle.setY(new Integer(record.get("row")));
			circle.setRadioPx(new Float(record.get("radioPx")));
			list.add(circle);

		}
		if (list.size()>MAX_CIRCLES){
			throw new BuenOjoCSVParserException("el archivo contiene mas de "+MAX_CIRCLES+ "áreas circulares");
		}
		return list;

	}

}
