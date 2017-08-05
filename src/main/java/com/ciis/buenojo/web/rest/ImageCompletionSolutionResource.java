package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.ImageCompletionSolution;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.ImageCompletionSolutionRepository;
import com.ciis.buenojo.service.ImageCompletionSolutionService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing ImageCompletionSolution.
 */
@RestController
@RequestMapping("/api")
public class ImageCompletionSolutionResource {

    private final Logger log = LoggerFactory.getLogger(ImageCompletionSolutionResource.class);

    @Inject
    private ImageCompletionSolutionRepository imageCompletionSolutionRepository;
    
    @Inject 
    private ImageCompletionSolutionService imageCompletionSolutionService;
    /**
     * POST  /imageCompletionSolutions -> Create a new imageCompletionSolution.
     */
    @RequestMapping(value = "/imageCompletionSolutions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageCompletionSolution> createImageCompletionSolution(@RequestBody ImageCompletionSolution imageCompletionSolution) throws URISyntaxException {
        log.debug("REST request to save ImageCompletionSolution : {}", imageCompletionSolution);
        if (imageCompletionSolution.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new imageCompletionSolution cannot already have an ID").body(null);
        }
        ImageCompletionSolution result = imageCompletionSolutionRepository.save(imageCompletionSolution);
        return ResponseEntity.created(new URI("/api/imageCompletionSolutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("imageCompletionSolution", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /imageCompletionSolutions -> Updates an existing imageCompletionSolution.
     */
    @RequestMapping(value = "/imageCompletionSolutions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageCompletionSolution> updateImageCompletionSolution(@RequestBody ImageCompletionSolution imageCompletionSolution) throws URISyntaxException {
        log.debug("REST request to update ImageCompletionSolution : {}", imageCompletionSolution);
        if (imageCompletionSolution.getId() == null) {
            return createImageCompletionSolution(imageCompletionSolution);
        }
        ImageCompletionSolution result = imageCompletionSolutionRepository.save(imageCompletionSolution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("imageCompletionSolution", imageCompletionSolution.getId().toString()))
            .body(result);
    }

    /**
     * GET  /imageCompletionSolutions -> get all the imageCompletionSolutions.
     */
    @RequestMapping(value = "/imageCompletionSolutions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ImageCompletionSolution> getAllImageCompletionSolutions(@RequestParam(required = false) String filter) {
        if ("imagecompletionexercise-is-null".equals(filter)) {
            log.debug("REST request to get all ImageCompletionSolutions where imageCompletionExercise is null");
            return StreamSupport
                .stream(imageCompletionSolutionRepository.findAll().spliterator(), false)
                .filter(imageCompletionSolution -> imageCompletionSolution.getImageCompletionExercise() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all ImageCompletionSolutions");
        return imageCompletionSolutionRepository.findAll();
    }

    /**
     * GET  /imageCompletionSolutions/:id -> get the "id" imageCompletionSolution.
     */
    @RequestMapping(value = "/imageCompletionSolutions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageCompletionSolution> getImageCompletionSolution(@PathVariable Long id) {
        log.debug("REST request to get ImageCompletionSolution : {}", id);
        return Optional.ofNullable(imageCompletionSolutionRepository.findOne(id))
            .map(imageCompletionSolution -> new ResponseEntity<>(
                imageCompletionSolution,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /imageCompletionSolutions/:id -> delete the "id" imageCompletionSolution.
     */
    @RequestMapping(value = "/imageCompletionSolutions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteImageCompletionSolution(@PathVariable Long id) {
        log.debug("REST request to delete ImageCompletionSolution : {}", id);
        imageCompletionSolutionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("imageCompletionSolution", id.toString())).build();
    }
    
    /***
     * POST /imageCompletionSolutions/upload/:exerciseId ->upload a solution csv for the corresponding imageCompletionExercise
     * @throws BuenOjoCSVParserException if the CSV is not well formed
     * @throws BuenOjoInconsistencyException 
     */
    @RequestMapping(value = "/imageCompletionSolutions/upload/{exerciseId}",
    		method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageCompletionSolution>uploadSolution(@RequestParam("file") MultipartFile file, @PathVariable Long exerciseId ) throws IOException,URISyntaxException, BuenOjoCSVParserException, BuenOjoInconsistencyException {
    	ImageCompletionSolution imageCompletionSolution = imageCompletionSolutionService.createSolutionForExerciseIdFromCSVSource(exerciseId, file);
    	
    	return createImageCompletionSolution(imageCompletionSolution);
    }
}
