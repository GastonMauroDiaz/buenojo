package com.ciis.buenojo.web.rest;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ciis.buenojo.domain.Course;
import com.ciis.buenojo.domain.Tag;
import com.ciis.buenojo.domain.TagPool;
import com.ciis.buenojo.domain.enumeration.TagPoolColumn;
import com.ciis.buenojo.domain.parsers.TagPoolCSVParser;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.repository.CourseRepository;
import com.ciis.buenojo.repository.TagPoolRepository;
import com.ciis.buenojo.repository.TagRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;

/**
 * Create all the imagecompletion exercise tags from a tag similarity matrix on CSV file
 * Columns correspond to {@code TagPool}
 * @author franciscogindre
 *
 */
@Controller
@RequestMapping("/api")
public class TagUploadResource {
	private final Logger log = LoggerFactory.getLogger(TagResource.class);

    @Inject
    private TagRepository tagRepository;
    @Inject
    private TagPoolRepository tagPoolRepository;
    @Inject
    private CourseRepository courseRespository;
    

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/upload/tagPool/{courseId}",
    				method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file, @PathVariable Long courseId ) throws IOException, URISyntaxException, BuenOjoCSVParserException {

    	
    	if (courseId == null){
    		return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("Course ID null or invalid")).build();
    	}
    	Course course = courseRespository.getOne(courseId);
    	
    	
    	
        if (!file.isEmpty()) {

            //store file in storage
             TagPoolCSVParser parser = new TagPoolCSVParser(file);
             
             List<Tag> result = parser.parse(course);
             
             
             
            List<TagPool> tagPool =  parser.parseTagPool();
            tagRepository.save(result);
        	tagRepository.flush();
          	tagPoolRepository.save(tagPool);
        	tagPoolRepository.flush();
        	
            
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntitiesCreationAlert("tags", new Integer(result.size()).toString())).build();

        }
        return ResponseEntity.badRequest().headers(HeaderUtil.createBadCSVRequestAlert(file.getName())).build();

    }
   
}
