package com.ciis.buenojo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ciis.buenojo.domain.HangManExercise;
import com.ciis.buenojo.domain.HangManExerciseDelimitedArea;
import com.ciis.buenojo.repository.HangManExerciseDelimitedAreaRepository;
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
 * REST controller for managing HangManExerciseDelimitedArea.
 */
@RestController
@RequestMapping("/api")
public class HangManExerciseDelimitedAreaResource {

    private final Logger log = LoggerFactory.getLogger(HangManExerciseDelimitedAreaResource.class);

    @Inject
    private HangManExerciseDelimitedAreaRepository hangManExerciseDelimitedAreaRepository;

    /**
     * POST  /hangManExerciseDelimitedAreas -> Create a new hangManExerciseDelimitedArea.
     */
    @RequestMapping(value = "/hangManExerciseDelimitedAreas",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseDelimitedArea> createHangManExerciseDelimitedArea(@Valid @RequestBody HangManExerciseDelimitedArea hangManExerciseDelimitedArea) throws URISyntaxException {
        log.debug("REST request to save HangManExerciseDelimitedArea : {}", hangManExerciseDelimitedArea);
        if (hangManExerciseDelimitedArea.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new hangManExerciseDelimitedArea cannot already have an ID").body(null);
        }
        HangManExerciseDelimitedArea result = hangManExerciseDelimitedAreaRepository.save(hangManExerciseDelimitedArea);
        return ResponseEntity.created(new URI("/api/hangManExerciseDelimitedAreas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hangManExerciseDelimitedArea", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hangManExerciseDelimitedAreas -> Updates an existing hangManExerciseDelimitedArea.
     */
    @RequestMapping(value = "/hangManExerciseDelimitedAreas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseDelimitedArea> updateHangManExerciseDelimitedArea(@Valid @RequestBody HangManExerciseDelimitedArea hangManExerciseDelimitedArea) throws URISyntaxException {
        log.debug("REST request to update HangManExerciseDelimitedArea : {}", hangManExerciseDelimitedArea);
        if (hangManExerciseDelimitedArea.getId() == null) {
            return createHangManExerciseDelimitedArea(hangManExerciseDelimitedArea);
        }
        HangManExerciseDelimitedArea result = hangManExerciseDelimitedAreaRepository.save(hangManExerciseDelimitedArea);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hangManExerciseDelimitedArea", hangManExerciseDelimitedArea.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hangManExerciseDelimitedAreas -> get all the hangManExerciseDelimitedAreas.
     */
    @RequestMapping(value = "/hangManExerciseDelimitedAreas",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<HangManExerciseDelimitedArea> getAllHangManExerciseDelimitedAreas() {
        log.debug("REST request to get all HangManExerciseDelimitedAreas");
        return hangManExerciseDelimitedAreaRepository.findAll();
    }

    /**
     * GET  /hangManExerciseDelimitedAreas/:id -> get the "id" hangManExerciseDelimitedArea.
     */
    @RequestMapping(value = "/hangManExerciseDelimitedAreas/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HangManExerciseDelimitedArea> getHangManExerciseDelimitedArea(@PathVariable Long id) {
        log.debug("REST request to get HangManExerciseDelimitedArea : {}", id);
        return Optional.ofNullable(hangManExerciseDelimitedAreaRepository.findOne(id))
            .map(hangManExerciseDelimitedArea -> new ResponseEntity<>(
                hangManExerciseDelimitedArea,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hangManExerciseDelimitedAreas/:id -> delete the "id" hangManExerciseDelimitedArea.
     */
    @RequestMapping(value = "/hangManExerciseDelimitedAreas/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHangManExerciseDelimitedArea(@PathVariable Long id) {
        log.debug("REST request to delete HangManExerciseDelimitedArea : {}", id);
        hangManExerciseDelimitedAreaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hangManExerciseDelimitedArea", id.toString())).build();
    }
    
    /**
     * GET  /hangManExercises -> get all the hangManExercises.
     */
    @RequestMapping(value = "/hangManDelimitedAreas/container/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Object> getAllHangManExerciseDelimitedAreaByContainer(@PathVariable Long id) {
        log.debug("REST request to get all HangManExercises");
        return hangManExerciseDelimitedAreaRepository.findByContainer(id);
    }
}
