package com.ciis.buenojo.web.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.ciis.buenojo.domain.TagCircle;
import com.ciis.buenojo.domain.parsers.TagCircleCSVParser;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.repository.ImageCompletionExerciseRepository;
import com.ciis.buenojo.repository.TagCircleRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;

/**
 * Manages TagCircle CSV file upload
 * @author franciscogindre
 *
 */
@Controller
@RequestMapping("/api")
public class TagCircleUploadResource {
	
	@Inject
	ImageCompletionExerciseRepository imageCompletionExerciseRepository;
	
	@Inject 
	TagCircleRepository tagCircleRepository;
	
	@ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/upload/tagCircle/{exerciseId}",
    				method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file, @PathVariable Long exerciseId ) throws URISyntaxException, BuenOjoCSVParserException {
		
		if (exerciseId == null) return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("Exercise ID null or invalid")).build();
		
		List<TagCircle> circles = null;
		TagCircleCSVParser parser = null ;
		try {
			parser = new TagCircleCSVParser(file.getInputStream());
			circles = parser.parse();
			
			if (circles == null) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createBadCSVRequestAlert(file.getName())).build();
			}else if (circles.isEmpty()) {
				return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("File "+ file.getName()+" is empty")).build();
			}
			
		} catch (IOException e) {
	
			return ResponseEntity.badRequest().headers(HeaderUtil.createBadCSVRequestAlert(file.getName())).build();
			
		}
				
		return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert("TagCircle", new Integer(circles.size()).toString())).build();
		
		
	}
	

}
