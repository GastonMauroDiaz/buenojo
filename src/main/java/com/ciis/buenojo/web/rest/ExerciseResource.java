package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.Exercise;
import com.ciis.buenojo.repository.ExerciseRepository;
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
 * REST controller for managing Exercise.
 */
@RestController
@RequestMapping("/api")
public class ExerciseResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseResource.class);

    @Inject
    private ExerciseRepository exerciseRepository;

    /**
     * POST  /exercises -> Create a new exercise.
     */
    @RequestMapping(value = "/exercises",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Exercise> createExercise(@Valid @RequestBody Exercise exercise) throws URISyntaxException {
        log.debug("REST request to save Exercise : {}", exercise);
        if (exercise.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new exercise cannot already have an ID").body(null);
        }
        Exercise result = exerciseRepository.save(exercise);
        return ResponseEntity.created(new URI("/api/exercises/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("exercise", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /exercises -> Updates an existing exercise.
     */
    @RequestMapping(value = "/exercises",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Exercise> updateExercise(@Valid @RequestBody Exercise exercise) throws URISyntaxException {
        log.debug("REST request to update Exercise : {}", exercise);
        if (exercise.getId() == null) {
            return createExercise(exercise);
        }
        Exercise result = exerciseRepository.save(exercise);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("exercise", exercise.getId().toString()))
                .body(result);
    }

    /**
     * GET  /exercises -> get all the exercises.
     */
    @RequestMapping(value = "/exercises",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Exercise> getAllExercises() {
        log.debug("REST request to get all Exercises");
        return exerciseRepository.findAll();
    }

    /**
     * GET  /exercises/:id -> get the "id" exercise.
     */
    @RequestMapping(value = "/exercises/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Exercise> getExercise(@PathVariable Long id) {
        log.debug("REST request to get Exercise : {}", id);
        return Optional.ofNullable(exerciseRepository.findOne(id))
            .map(exercise -> new ResponseEntity<>(
                exercise,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /exercises/:id -> delete the "id" exercise.
     */
    @RequestMapping(value = "/exercises/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        log.debug("REST request to delete Exercise : {}", id);
        exerciseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("exercise", id.toString())).build();
    }
}
