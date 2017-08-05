package com.ciis.buenojo.domain.parsers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamSource;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.TagPair;
import com.ciis.buenojo.web.rest.TagResource;
import com.ciis.buenojo.domain.ImageCompletionSolution;

public class ImageCompletionSolutionCSVParser {
	private static final Integer AVG_ITEMS = 8;
	private InputStreamSource inputStreamSource;
	private List <Tag> tagList;
	
	private final Logger log = LoggerFactory.getLogger(ImageCompletionSolutionCSVParser.class);
	
	
	public ImageCompletionSolutionCSVParser (InputStreamSource source, List <Tag> tagList){
		this.inputStreamSource = source;
		this.tagList = tagList;
	}
	
	private Predicate<Tag> isEqualToTagNumber(Integer tagNumber) {
		return p -> p.getNumber().equals(tagNumber);
	}
	
	public List<TagPair> parse() throws IOException {
		
		CSVParser parser = CSVFormat.RFC4180.withHeader().withDelimiter(',').withAllowMissingColumnNames(true).parse(new InputStreamReader(this.inputStreamSource.getInputStream()));
		
		
		ArrayList<TagPair> tagPairs = new ArrayList<>(AVG_ITEMS);
		for (CSVRecord record : parser ){
			
			TagPair pair = new TagPair();
			Integer tagSlotId = new Integer(record.get("id"));
			Integer tagNumber = new Integer(record.get("etiqueta"));
			
			pair.setTagSlotId(tagSlotId);
			Optional<Tag> optionalTag = tagList.stream().filter(isEqualToTagNumber(tagNumber)).findFirst();
			if (optionalTag.isPresent()){
				Tag tag = optionalTag.get(); 
				pair.setTag(tag);
				tagPairs.add(pair);			
			}else {
				log.debug("Attempt to get invalid tag with number: "+tagNumber);
			}
		}
		
		return tagPairs;
	}
}

