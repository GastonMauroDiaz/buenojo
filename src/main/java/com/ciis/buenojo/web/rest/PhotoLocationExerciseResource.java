package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.PhotoLocationExercise;
import com.ciis.buenojo.repository.PhotoLocationExerciseRepository;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PhotoLocationExercise.
 */
@RestController
@RequestMapping("/api")
public class PhotoLocationExerciseResource {

    private final Logger log = LoggerFactory.getLogger(PhotoLocationExerciseResource.class);

    @Inject
    private PhotoLocationExerciseRepository photoLocationExerciseRepository;

    /**
     * POST  /photoLocationExercises -> Create a new photoLocationExercise.
     */
    @RequestMapping(value = "/photoLocationExercises",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExercise> createPhotoLocationExercise(@Valid @RequestBody PhotoLocationExercise photoLocationExercise) throws URISyntaxException {
        log.debug("REST request to save PhotoLocationExercise : {}", photoLocationExercise);
        if (photoLocationExercise.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new photoLocationExercise cannot already have an ID").body(null);
        }
        PhotoLocationExercise result = photoLocationExerciseRepository.save(photoLocationExercise);
        return ResponseEntity.created(new URI("/api/photoLocationExercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("photoLocationExercise", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /photoLocationExercises -> Updates an existing photoLocationExercise.
     */
    @RequestMapping(value = "/photoLocationExercises",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExercise> updatePhotoLocationExercise(@Valid @RequestBody PhotoLocationExercise photoLocationExercise) throws URISyntaxException {
        log.debug("REST request to update PhotoLocationExercise : {}", photoLocationExercise);
        if (photoLocationExercise.getId() == null) {
            return createPhotoLocationExercise(photoLocationExercise);
        }
        PhotoLocationExercise result = photoLocationExerciseRepository.save(photoLocationExercise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("photoLocationExercise", photoLocationExercise.getId().toString()))
            .body(result);
    }

    /**
     * GET  /photoLocationExercises -> get all the photoLocationExercises.
     */
    @RequestMapping(value = "/photoLocationExercises",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PhotoLocationExercise> getAllPhotoLocationExercises() {
        log.debug("REST request to get all PhotoLocationExercises");
        return photoLocationExerciseRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /photoLocationExercises/:id -> get the "id" photoLocationExercise.
     */
    @RequestMapping(value = "/photoLocationExercises/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExercise> getPhotoLocationExercise(@PathVariable Long id) {
        log.debug("REST request to get PhotoLocationExercise : {}", id);
        return Optional.ofNullable(photoLocationExerciseRepository.findOneWithEagerRelationships(id))
            .map(photoLocationExercise -> new ResponseEntity<>(
                photoLocationExercise,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /photoLocationExercises/:id -> any the "id" photoLocationExercise.
     */
    @RequestMapping(value = "/photoLocationExercises/any",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExercise> getPhotoLocationExercise() {
        Long id = null;
        List<PhotoLocationExercise> all = photoLocationExerciseRepository.findAll();
        if (all == null || all.size() == 0){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
         id = all.get(0).getId();
        log.debug("REST request to get ANY PhotoLocationExercise  : {}", id);
        return Optional.ofNullable(photoLocationExerciseRepository.findOneWithEagerRelationships(id))
            .map(photoLocationExercise -> new ResponseEntity<>(
                photoLocationExercise,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /photoLocationExercises/:id -> delete the "id" photoLocationExercise.
     */
    @RequestMapping(value = "/photoLocationExercises/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhotoLocationExercise(@PathVariable Long id) {
        log.debug("REST request to delete PhotoLocationExercise : {}", id);
        photoLocationExerciseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("photoLocationExercise", id.toString())).build();
    }
}
