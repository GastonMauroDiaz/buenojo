package com.ciis.buenojo.web.rest;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.PhotoLocationExercise;
import com.ciis.buenojo.domain.PhotoLocationExerciseGame;
import com.ciis.buenojo.exceptions.BuenOjoCSVParserException;
import com.ciis.buenojo.exceptions.BuenOjoInconsistencyException;
import com.ciis.buenojo.repository.PhotoLocationExerciseRepository;
import com.ciis.buenojo.service.PhotoLocationExerciseFactory;
import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping("/api")
public class PhotoLocationExerciseGameResource {
	
	private final Logger log = LoggerFactory.getLogger(PhotoLocationExerciseResource.class);
	
	@Inject
	private PhotoLocationExerciseFactory exerciseFactory;
	
	
	 /**
     * GET  /photoLocationExerciseGame/:id -> get the "id" photoLocationExercise.
	 * @throws BuenOjoInconsistencyException 
	 * @throws BuenOjoCSVParserException 
     */
    @RequestMapping(value = "/photoLocationExerciseGame/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PhotoLocationExerciseGame> getPhotoLocationExercise(@PathVariable Long id) throws BuenOjoCSVParserException, BuenOjoInconsistencyException {
        log.debug("REST request to get PhotoLocationExerciseGame : {}", id);
        PhotoLocationExerciseGame exerciseGame = exerciseFactory.exerciseGameModelForExercise(id);
        return Optional.ofNullable(exerciseGame)
            .map(photoLocationExerciseGame -> new ResponseEntity<>(
            		photoLocationExerciseGame,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
