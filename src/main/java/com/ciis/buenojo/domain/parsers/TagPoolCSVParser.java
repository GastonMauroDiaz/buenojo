package com.ciis.buenojo.domain.parsers;

import java.io.IOException;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamSource;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.Tag;

import com.ciis.buenojo.domain.TagPool;
import com.ciis.buenojo.domain.enumeration.TagPoolColumn;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;

public class TagPoolCSVParser {

	private InputStreamSource inputStreamSource;
	private HashMap <String,Tag> tagMap;
	public TagPoolCSVParser (InputStreamSource file) {
		if (file == null) throw new IllegalArgumentException("[TagPoolCSVParser]InputStream can't be null");
		this.inputStreamSource = file;
		
		
	}
	
	public List<Tag> parse(Course course) throws IOException {

		CSVParser parser = CSVFormat.RFC4180.withHeader().withDelimiter(',').withAllowMissingColumnNames(true).parse(new InputStreamReader(this.inputStreamSource.getInputStream()));
        List <Tag> result = createTags(parser, course);
		return result;
		
	}
	public List<TagPool> parseTagPool() throws IOException, BuenOjoCSVParserException {
		if (tagMap == null || tagMap.isEmpty()){
			parse(null);
		}
		return createTagPool(CSVFormat.RFC4180.withHeader().withDelimiter(',').withAllowMissingColumnNames(true).parse(new InputStreamReader(this.inputStreamSource.getInputStream())));
	}
	
	private List <Tag>  createTags(CSVParser parser, Course course) {
	    	tagMap = new HashMap<>();
	    	ArrayList<Tag> tagList = new ArrayList<Tag>();
	    	for (CSVRecord csvRecord : parser) {
	    		String name = csvRecord.get(TagPoolColumn.TAG.ordinal()).toString();
	    		if (!tagMap.containsKey(name)){
	    			Tag tag = new Tag();
	        	    tag.setName(name);
	        	    tag.setCourse(course);
	        
	        	    tagMap.put(name, tag);
	        	    
	        	    tag.setNumber(tagMap.size());
	        	    tagList.add(tag);
	        	    
	    		}

	    	 }
	    	
	    	return tagList;
	    }
	 
	 private List<TagPool> createTagPool(CSVParser parser) throws BuenOjoCSVParserException{
	    	ArrayList<TagPool> list = new ArrayList<>();
	    	for (CSVRecord record : parser) {
	    		String name = record.get(TagPoolColumn.TAG.ordinal()).toString();

	   	     	Tag tag = tagMap.get(name);
	   	     	

	   	     	for (int i = TagPoolColumn.SIMILAR_1.ordinal(); (i <record.size()) && i <= TagPoolColumn.SIMILAR_3.ordinal(); i++) {
	   	     		String similarTagName = record.get(i);
	   	     		if (similarTagName != null && !StringUtils.isAnyEmpty(similarTagName)){
	   	     			Tag similarTag = tagMap.get(similarTagName);
	   	     			
	   	     			if (similarTag != null){
		   	     			TagPool tagPool = new TagPool();
		   	     			tagPool.setTag(tag);
		   	     			tagPool.setSimilarTag(similarTag);
		   	     			tagPool.setSimilarity(i);
		   	     			list.add(tagPool);
	   	     			} else {
	   	     				throw new BuenOjoCSVParserException("no se pudo obtener la etiqueta con nombre: '"+similarTagName+"'");
	   	     			}
	   	     		}
	   	     	}
	    	}
	    	return list;
	    	
	    }
}
