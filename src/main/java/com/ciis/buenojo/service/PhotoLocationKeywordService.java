package com.ciis.buenojo.service;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.PhotoLocationKeyword;
import com.ciis.buenojo.domain.parsers.PhotoLocationKeywordsParser;
import com.ciis.buenojo.repository.PhotoLocationKeywordRepository;

@Service
public class PhotoLocationKeywordService {

	@Inject
	PhotoLocationKeywordRepository keywordRepository;
	
	public List<PhotoLocationKeyword> keywordsFromFile(InputStreamSource source) throws IOException{
		
		PhotoLocationKeywordsParser parser = new PhotoLocationKeywordsParser(source);
		List<PhotoLocationKeyword> keywords = parser.parse();
		return keywords; 
	}
	
	public List<PhotoLocationKeyword> keywordsFromFile(InputStreamSource source, Course course) throws IOException{
		
		return keywordsFromFile(source).stream().map((keyword) -> {
			keyword.setCourse(course);
			return keyword;
		}).collect(Collectors.toList());
		
	}
	
	
}
