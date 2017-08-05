package com.ciis.buenojo.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ciis.buenojo.domain.HangManExercise;
import com.ciis.buenojo.repository.HangManExerciseRepository;
import com.ciis.buenojo.service.HangManExerciseService;
import com.ciis.buenojo.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing HangManExercise.
 */
@RestController
@RequestMapping("/api")
public class HangManExerciseResource {

    private final Logger log = LoggerFactory.getLogger(HangManExerciseResource.class);

    @Inject
    private HangManExerciseRepository hangManExerciseRepository;

    @Inject
    private HangManExerciseService hangManExerciseService;

    /**
     * POST  /hangManExercises -> Create a new hangManExercise.
     */
    @RequestMapping(value = "/hangManExercises",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExercise> createHangManExercise(@Valid @RequestBody HangManExercise hangManExercise) throws URISyntaxException {
        log.debug("REST request to save HangManExercise : {}", hangManExercise);
        if (hangManExercise.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hangManExercise cannot already have an ID").body(null);
        }
        HangManExercise result = hangManExerciseRepository.save(hangManExercise);
        return ResponseEntity.created(new URI("/api/hangManExercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hangManExercise", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hangManExercises -> Updates an existing hangManExercise.
     */
    @RequestMapping(value = "/hangManExercises",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExercise> updateHangManExercise(@Valid @RequestBody HangManExercise hangManExercise) throws URISyntaxException {
        log.debug("REST request to update HangManExercise : {}", hangManExercise);
        if (hangManExercise.getId() == null) {
            return createHangManExercise(hangManExercise);
        }
        HangManExercise result = hangManExerciseRepository.save(hangManExercise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hangManExercise", hangManExercise.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hangManExercises -> get all the hangManExercises.
     */
    @RequestMapping(value = "/hangManExercises",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HangManExercise> getAllHangManExercises() {
        log.debug("REST request to get all HangManExercises");
        return hangManExerciseRepository.findAll();
    }

    /**
     * GET  /hangManExercises -> get all the hangManExercises.
     */
    @RequestMapping(value = "/hangManExercises/container/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HangManExercise> getAllHangManExercisesByContainer(@PathVariable Long id) {
        log.debug("REST request to get all HangManExercises");
        List<HangManExercise> output;
        output = hangManExerciseRepository.findByhangmanGameContainerIdOrderByExerciseOrder(id);
        for (HangManExercise he : output)
        	he.setTask(StringEscapeUtils.escapeHtml4(he.getTask()));
        return output;
    }
    
    /**
     * GET  /hangManExercises/:id -> get the "id" hangManExercise.
     */
    @RequestMapping(value = "/hangManExercises/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExercise> getHangManExercise(@PathVariable Long id) {
        log.debug("REST request to get HangManExercise : {}", id);
        return Optional.ofNullable(hangManExerciseRepository.findOne(id))
            .map(hangManExercise -> new ResponseEntity<>(
                hangManExercise,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hangManExercises/:id -> delete the "id" hangManExercise.
     */
    @RequestMapping(value = "/hangManExercises/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHangManExercise(@PathVariable Long id) {
        log.debug("REST request to delete HangManExercise : {}", id);
        hangManExerciseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hangManExercise", id.toString())).build();
    }
    
    @RequestMapping(value = "/hangManExercises/{id}/{responses}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<String> getIsCorrect(@PathVariable Long id, @PathVariable String responses) {
        log.debug("REST request to check responses : {} {}", id, responses);
        List<String> output=new ArrayList<String>();
        output.add(hangManExerciseService.getIsCorrect(id, responses).toString());
        return output;
    }

    @RequestMapping(value = "/hangManExercises/getIncorrects/{id}/{responses}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public List<Long> getWhichAreIncorrect(@PathVariable Long id, @PathVariable String responses) {
            log.debug("REST request to check responses : {} {}", id, responses);
            return hangManExerciseService.whichAreInCorrect(id, responses);
        }



    
}
