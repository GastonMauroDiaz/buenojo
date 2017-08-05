package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.ImageCompletionExercise;
import com.ciis.buenojo.domain.TagCircle;
import com.ciis.buenojo.domain.parsers.TagCircleCSVParser;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.repository.ImageCompletionExerciseRepository;
import com.ciis.buenojo.repository.TagCircleRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TagCircle.
 */
@RestController
@RequestMapping("/api")
public class TagCircleResource {

    private final Logger log = LoggerFactory.getLogger(TagCircleResource.class);

    @Inject
    private TagCircleRepository tagCircleRepository;
    
    @Inject
    private ImageCompletionExerciseRepository exerciseRepository;

    /**
     * POST  /tagCircles -> Create a new tagCircle.
     */
    @RequestMapping(value = "/tagCircles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagCircle> createTagCircle(@Valid @RequestBody TagCircle tagCircle) throws URISyntaxException {
        log.debug("REST request to save TagCircle : {}", tagCircle);
        if (tagCircle.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new tagCircle cannot already have an ID").body(null);
        }
        TagCircle result = tagCircleRepository.save(tagCircle);
        return ResponseEntity.created(new URI("/api/tagCircles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tagCircle", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tagCircles -> Updates an existing tagCircle.
     */
    @RequestMapping(value = "/tagCircles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagCircle> updateTagCircle(@Valid @RequestBody TagCircle tagCircle) throws URISyntaxException {
        log.debug("REST request to update TagCircle : {}", tagCircle);
        if (tagCircle.getId() == null) {
            return createTagCircle(tagCircle);
        }
        TagCircle result = tagCircleRepository.save(tagCircle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tagCircle", tagCircle.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tagCircles -> get all the tagCircles.
     */
    @RequestMapping(value = "/tagCircles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TagCircle> getAllTagCircles() {
        log.debug("REST request to get all TagCircles");
        return tagCircleRepository.findAll();
    }

    /**
     * GET  /tagCircles/:id -> get the "id" tagCircle.
     */
    @RequestMapping(value = "/tagCircles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagCircle> getTagCircle(@PathVariable Long id) {
        log.debug("REST request to get TagCircle : {}", id);
        return Optional.ofNullable(tagCircleRepository.findOne(id))
            .map(tagCircle -> new ResponseEntity<>(
                tagCircle,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tagCircles/:id -> delete the "id" tagCircle.
     */
    @RequestMapping(value = "/tagCircles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTagCircle(@PathVariable Long id) {
        log.debug("REST request to delete TagCircle : {}", id);
        tagCircleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tagCircle", id.toString())).build();
    }
    
    /**
     * POST  /tagCircles -> Create a new tagCircle.
     * @throws BuenOjoCSVParserException 
     */
    @RequestMapping(value = "/tagCircle/upload/{exercise_id}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> uploadTagCircles(@PathVariable("exercise_id")Long id, @RequestParam("file") MultipartFile csvMetadataFile) throws URISyntaxException, IOException, BuenOjoCSVParserException{
    	if (id == null)
    		return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("falta el parámetro ID de ejercicio")).body(null);
    	if (csvMetadataFile == null)
    		return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("falta el parámetro CSV")).body(null);
    	
    	TagCircleCSVParser parser = new TagCircleCSVParser(csvMetadataFile.getInputStream());
    	
    	List<TagCircle> circles = parser.parse();
    	
    	ImageCompletionExercise exercise =  exerciseRepository.findOne(id);
    	
    	if (exercise == null) {
    		return ResponseEntity.badRequest().headers(HeaderUtil.createBadRequestHeaderAlert("No existe el ejercicio "+id)).body(null);
    		
    	}
    	for (TagCircle tagCircle : circles) {
			tagCircle.setImageCompletionExercise(exercise);
		}
    	tagCircleRepository.save(circles);
    	exercise.setTagCircles(new HashSet<TagCircle>(circles));
    	
    	exerciseRepository.saveAndFlush(exercise);

    	return ResponseEntity.ok().headers(HeaderUtil.createEntitiesCreationAlert("tagCircle", new Integer(circles.size()).toString())).build(); 
    }
}
