package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.ImageCompletionExercise;
import com.ciis.buenojo.repository.ImageCompletionExerciseRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ImageCompletionExercise.
 */
@RestController
@RequestMapping("/api")
public class ImageCompletionExerciseResource {

    private final Logger log = LoggerFactory.getLogger(ImageCompletionExerciseResource.class);

    @Inject
    private ImageCompletionExerciseRepository imageCompletionExerciseRepository;

    /**
     * POST  /imageCompletionExercises -> Create a new imageCompletionExercise.
     */
    @RequestMapping(value = "/imageCompletionExercises",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageCompletionExercise> createImageCompletionExercise(@RequestBody ImageCompletionExercise imageCompletionExercise) throws URISyntaxException {
        log.debug("REST request to save ImageCompletionExercise : {}", imageCompletionExercise);
        if (imageCompletionExercise.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new imageCompletionExercise cannot already have an ID").body(null);
        }
        ImageCompletionExercise result = imageCompletionExerciseRepository.save(imageCompletionExercise);
        return ResponseEntity.created(new URI("/api/imageCompletionExercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("imageCompletionExercise", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /imageCompletionExercises -> Updates an existing imageCompletionExercise.
     */
    @RequestMapping(value = "/imageCompletionExercises",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageCompletionExercise> updateImageCompletionExercise(@RequestBody ImageCompletionExercise imageCompletionExercise) throws URISyntaxException {
        log.debug("REST request to update ImageCompletionExercise : {}", imageCompletionExercise);
        if (imageCompletionExercise.getId() == null) {
            return createImageCompletionExercise(imageCompletionExercise);
        }
        ImageCompletionExercise result = imageCompletionExerciseRepository.save(imageCompletionExercise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("imageCompletionExercise", imageCompletionExercise.getId().toString()))
            .body(result);
    }

    /**
     * GET  /imageCompletionExercises -> get all the imageCompletionExercises.
     */
    @RequestMapping(value = "/imageCompletionExercises",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ImageCompletionExercise> getAllImageCompletionExercises() {
        log.debug("REST request to get all ImageCompletionExercises");
        return imageCompletionExerciseRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /imageCompletionExercises/:id -> get the "id" imageCompletionExercise.
     */
    @RequestMapping(value = "/imageCompletionExercises/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImageCompletionExercise> getImageCompletionExercise(@PathVariable Long id) {
        log.debug("REST request to get ImageCompletionExercise : {}", id);
        return Optional.ofNullable(imageCompletionExerciseRepository.findOneWithEagerRelationships(id))
            .map(imageCompletionExercise -> new ResponseEntity<>(
                imageCompletionExercise,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /imageCompletionExercises/:id -> delete the "id" imageCompletionExercise.
     */
    @RequestMapping(value = "/imageCompletionExercises/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteImageCompletionExercise(@PathVariable Long id) {
        log.debug("REST request to delete ImageCompletionExercise : {}", id);
        imageCompletionExerciseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("imageCompletionExercise", id.toString())).build();
    }

}
