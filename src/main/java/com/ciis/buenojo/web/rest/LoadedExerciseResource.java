package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.LoadedExercise;
import com.ciis.buenojo.repository.LoadedExerciseRepository;
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
 * REST controller for managing LoadedExercise.
 */
@RestController
@RequestMapping("/api")
public class LoadedExerciseResource {

    private final Logger log = LoggerFactory.getLogger(LoadedExerciseResource.class);

    @Inject
    private LoadedExerciseRepository loadedExerciseRepository;

    /**
     * POST  /loadedExercises -> Create a new loadedExercise.
     */
    @RequestMapping(value = "/loadedExercises",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoadedExercise> createLoadedExercise(@Valid @RequestBody LoadedExercise loadedExercise) throws URISyntaxException {
        log.debug("REST request to save LoadedExercise : {}", loadedExercise);
        if (loadedExercise.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new loadedExercise cannot already have an ID").body(null);
        }
        LoadedExercise result = loadedExerciseRepository.save(loadedExercise);
        return ResponseEntity.created(new URI("/api/loadedExercises/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("loadedExercise", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /loadedExercises -> Updates an existing loadedExercise.
     */
    @RequestMapping(value = "/loadedExercises",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoadedExercise> updateLoadedExercise(@Valid @RequestBody LoadedExercise loadedExercise) throws URISyntaxException {
        log.debug("REST request to update LoadedExercise : {}", loadedExercise);
        if (loadedExercise.getId() == null) {
            return createLoadedExercise(loadedExercise);
        }
        LoadedExercise result = loadedExerciseRepository.save(loadedExercise);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("loadedExercise", loadedExercise.getId().toString()))
                .body(result);
    }

    /**
     * GET  /loadedExercises -> get all the loadedExercises.
     */
    @RequestMapping(value = "/loadedExercises",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LoadedExercise> getAllLoadedExercises() {
        log.debug("REST request to get all LoadedExercises");
        return loadedExerciseRepository.findAll();
    }

    /**
     * GET  /loadedExercises/:id -> get the "id" loadedExercise.
     */
    @RequestMapping(value = "/loadedExercises/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LoadedExercise> getLoadedExercise(@PathVariable Long id) {
        log.debug("REST request to get LoadedExercise : {}", id);
        return Optional.ofNullable(loadedExerciseRepository.findOne(id))
            .map(loadedExercise -> new ResponseEntity<>(
                loadedExercise,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /loadedExercises/:id -> delete the "id" loadedExercise.
     */
    @RequestMapping(value = "/loadedExercises/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLoadedExercise(@PathVariable Long id) {
        log.debug("REST request to delete LoadedExercise : {}", id);
        loadedExerciseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("loadedExercise", id.toString())).build();
    }
}
